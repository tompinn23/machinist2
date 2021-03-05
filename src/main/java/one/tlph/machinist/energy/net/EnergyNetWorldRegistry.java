package one.tlph.machinist.energy.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraftforge.fml.network.IContainerFactory;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.util.WorldHelpers;

import java.util.*;

final class EnergyNetWorldRegistry {

    private IWorld world;

    private final Set<EnergyNetBase> grids;
    private final Set<EnergyNetBase> dirtyGrids;
    private final Set<EnergyNetBase> deadGrids;

    private Set<IEnergyNetPart> orphanedParts;

    private final Set<IEnergyNetPart> detachedParts;

    private final HashMap<Long, Set<IEnergyNetPart>> partsAwaitingChunkLoad;

    private final Object partsAwaitingChunkLoadMutex;
    private final Object orphanedPartsMutex;

    public EnergyNetWorldRegistry(final IWorld world) {
        this.world = world;
        grids = new HashSet<>();
        deadGrids = new HashSet<>();
        dirtyGrids = new HashSet<>();

        detachedParts = new HashSet<>();
        orphanedParts = new HashSet<>();
        partsAwaitingChunkLoad = new HashMap<>();

        partsAwaitingChunkLoadMutex = new Object();
        orphanedPartsMutex = new Object();
    }

    public void tickStart() {
        if(grids.size() > 0) {
            for(EnergyNetBase grid : grids) {
                if(grid.WORLD == world && grid.WORLD.isRemote == world.isRemote()) {
                    if(grid.isEmpty()) {
                        deadGrids.add(grid);
                    }
                    else {
                        grid.updateGrid();
                    }
                }
            }
        }
    }

    public void processGridChanges() {
        BlockPos coord;
        List<Set<EnergyNetBase>> mergePools = null;

        if(orphanedParts.size() > 0) {
            Set<IEnergyNetPart> orphansToProcess = null;
            synchronized (orphanedPartsMutex) {
                if(orphanedParts.size() > 0) {
                    orphansToProcess = orphanedParts;
                    orphanedParts = new HashSet<>();
                }
            }

            if(orphansToProcess != null && orphansToProcess.size() > 0) {
                AbstractChunkProvider provider = this.world.getChunkProvider();
                Set<EnergyNetBase> compatibleGrids;
                for(IEnergyNetPart orphan : orphansToProcess) {
                    coord = orphan.getWorldPos();
                    if(!this.world.isBlockLoaded(coord)) {
                        continue;
                    }

                    if(orphan.isPartInvalid()) { continue; }

                    if(this.getEnergyGridPartFromWorld(world, coord) != orphan) {
                        continue;
                    }

                    // This is the only place parts attach to form grids.
                    compatibleGrids = orphan.attachToNeighbours();
                    if(compatibleGrids == null) {
                        //NO GRIDS :(
                        // This is the only place new controllers are created.
                        EnergyNetBase newGrid = orphan.createNewGrid();
                        newGrid.attachBlock(orphan);
                        this.grids.add(newGrid);
                    }
                    else if(compatibleGrids.size() > 1) {
                        if(mergePools == null) {
                            mergePools = new ArrayList<>();
                        }

                        // This is where merges are detected.
                        boolean hasAddedToPool = false;
                        List<Set<EnergyNetBase>> candidatePools = new ArrayList<>();
                        for(Set<EnergyNetBase> candidatePool : mergePools) {
                            if(!Collections.disjoint(candidatePool, compatibleGrids)) {
                                // They share at least one element, so that means they will all touch after the merge
                                candidatePools.add(candidatePool);
                            }
                        }

                        if(candidatePools.size() <= 0) {
                            // No pools nearby, create a new merge pool
                            mergePools.add(compatibleGrids);
                        }
                        else if(candidatePools.size() == 1) {
                            // Only one pool nearby, simply add to that one
                            candidatePools.get(0).addAll(compatibleGrids);
                        }
                        else {
                            // Multiple pools- merge into one, then add the compatible controllers
                            Set<EnergyNetBase> masterPool = candidatePools.get(0);
                            Set<EnergyNetBase> consumedPool;
                            for(int i = 1; i < candidatePools.size(); i++) {
                                consumedPool = candidatePools.get(i);
                                masterPool.addAll(consumedPool);
                                mergePools.remove(consumedPool);
                            }
                            masterPool.addAll(compatibleGrids);
                        }
                    }
                }
            }

            if(mergePools != null && mergePools.size() > 0) {
                // Process merges - any machines that have been marked for merge should be merged
                // into the "master" machine.
                // To do this, we combine lists of machines that are touching one another and therefore
                // should voltron the fuck up.
                for(Set<EnergyNetBase> mergePool : mergePools) {
                    // Search for the new master machine, which will take over all the blocks contained in the other machines
                    EnergyNetBase newMaster = null;
                    for(EnergyNetBase controller : mergePool) {
                        if(newMaster == null || controller.shouldConsume(newMaster)) {
                            newMaster = controller;
                        }
                    }

                    if(newMaster == null) {
                        Machinist.logger.error("Multiblock system checked a merge pool of size %d, found no master candidates. This should never happen.", mergePool.size());
                    }
                    else {
                        // Merge all the other machines into the master machine, then unregister them
                        addDirtyGrid(newMaster);
                        for(EnergyNetBase controller : mergePool) {
                            if(controller != newMaster) {
                                newMaster.assimilate(controller);
                                addDeadGrid(controller);
                                addDirtyGrid(newMaster);
                            }
                        }
                    }
                }
            }

            // Process splits and assembly
            // Any controllers which have had parts removed must be checked to see if some parts are no longer
            // physically connected to their master.
            if(dirtyGrids.size() > 0) {
                Set<IEnergyNetPart> newlyDetachedParts = null;
                for(EnergyNetBase controller : dirtyGrids) {
                    // Tell the machine to check if any parts are disconnected.
                    // It should return a set of parts which are no longer connected.
                    // POSTCONDITION: The controller must have informed those parts that
                    // they are no longer connected to this machine.
                    newlyDetachedParts = controller.checkForDisconnections();

                    if(!controller.isEmpty()) {
                        //controller.recalculateMinMaxCoords();
                        //controller.checkIfMachineIsWhole();
                    }
                    else {
                        addDeadGrid(controller);
                    }

                    if(newlyDetachedParts != null && newlyDetachedParts.size() > 0) {
                        // Controller has shed some parts - add them to the detached list for delayed processing
                        detachedParts.addAll(newlyDetachedParts);
                    }
                }

                dirtyGrids.clear();
            }

            // Unregister dead controllers
            if(deadGrids.size() > 0) {
                for(EnergyNetBase controller : deadGrids) {
                    // Go through any controllers which have marked themselves as potentially dead.
                    // Validate that they are empty/dead, then unregister them.
                    if(!controller.isEmpty()) {
                        Machinist.logger.error("Found a non-empty controller. Forcing it to shed its blocks and die. This should never happen!");
                        detachedParts.addAll(controller.detachAllBlocks());
                    }

                    // THIS IS THE ONLY PLACE WHERE CONTROLLERS ARE UNREGISTERED.
                    this.grids.remove(controller);
                }

                deadGrids.clear();
            }

            // Process detached blocks
            // Any blocks which have been detached this tick should be moved to the orphaned
            // list, and will be checked next tick to see if their chunk is still loaded.
            for(IEnergyNetPart part : detachedParts) {
                // Ensure parts know they're detached
                part.assertDetached();
            }

            addAllOrphanedPartsThreadsafe(detachedParts);
            detachedParts.clear();
            }

        }

    private void addOrphanedPartThreadsafe(final IEnergyNetPart part) {
        synchronized(orphanedPartsMutex) {
            orphanedParts.add(part);
        }
    }

    private void addAllOrphanedPartsThreadsafe(final Collection<? extends IEnergyNetPart> parts) {
        synchronized(orphanedPartsMutex) {
            orphanedParts.addAll(parts);
        }
    }

    public void addDeadGrid(EnergyNetBase grid) {
        this.deadGrids.add(grid);
    }

    public void addDirtyGrid(EnergyNetBase grid) {
        this.dirtyGrids.add(grid);
    }

    private IEnergyNetPart getEnergyGridPartFromWorld(IWorld world, BlockPos coord) {
        TileEntity te = world.getTileEntity(coord);
        return te instanceof IEnergyNetPart ? (IEnergyNetPart)te : null;
    }

    public void onPartAdded(IEnergyNetPart part) {
        BlockPos worldLocation = part.getWorldPos();
        if(!this.world.isBlockLoaded(worldLocation)) {
            Set<IEnergyNetPart> partSet;
            long chunkHash = WorldHelpers.getChunkXZHashFromBlock(worldLocation);
            synchronized (partsAwaitingChunkLoadMutex) {
                if(!partsAwaitingChunkLoad.containsKey(chunkHash)) {
                    partSet = new HashSet<>();
                    partsAwaitingChunkLoad.put(chunkHash, partSet);
                }
                else {
                    partSet = partsAwaitingChunkLoad.get(chunkHash);
                }

                partSet.add(part);
            }
        } else {
            addOrphanedPartThreadsafe(part);
        }

    }

    public void onConnectorRemoved(IEnergyNetPart part) {
        final BlockPos coord = part.getWorldPos();
        if(coord != null) {
            long hash = WorldHelpers.getChunkXZHashFromBlock(coord);

            if(partsAwaitingChunkLoad.containsKey(hash)) {
                synchronized(partsAwaitingChunkLoadMutex) {
                    if(partsAwaitingChunkLoad.containsKey(hash)) {
                        partsAwaitingChunkLoad.get(hash).remove(part);
                        if(partsAwaitingChunkLoad.get(hash).size() <= 0) {
                            partsAwaitingChunkLoad.remove(hash);
                        }
                    }
                }
            }
        }

        detachedParts.remove(part);
        if(orphanedParts.contains(part)) {
            synchronized(orphanedPartsMutex) {
                orphanedParts.remove(part);
            }
        }

        part.assertDetached();
    }


    public void onWorldUnloaded() {
        grids.clear();
        deadGrids.clear();
        dirtyGrids.clear();
        detachedParts.clear();
        synchronized (partsAwaitingChunkLoadMutex) {
            partsAwaitingChunkLoad.clear();
        }
        synchronized (orphanedPartsMutex) {
            orphanedParts.clear();
        }
        world = null;
    }


    public void onChunkLoaded(int chunkX, int chunkZ) {
        final long chunkHash = new ChunkPos(chunkX, chunkZ).hashCode();
        if(partsAwaitingChunkLoad.containsKey(chunkHash)) {
            synchronized(partsAwaitingChunkLoadMutex) {
                if(partsAwaitingChunkLoad.containsKey(chunkHash)) {
                    addAllOrphanedPartsThreadsafe(partsAwaitingChunkLoad.get(chunkHash));
                    partsAwaitingChunkLoad.remove(chunkHash);
                }
            }
        }
    }


}
