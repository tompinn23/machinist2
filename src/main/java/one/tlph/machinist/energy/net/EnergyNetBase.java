package one.tlph.machinist.energy.net;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.util.WorldHelpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public abstract class EnergyNetBase {

    public final World WORLD;


    private BlockPos referenceCoord;

    private boolean shouldCheckForDisconnections;


    protected HashSet<IEnergyNetPart> connectedParts;

    protected EnergyNetBase(World world) {
        WORLD = world;
        connectedParts = new HashSet<>();

        referenceCoord = null;
        shouldCheckForDisconnections = true;
    }

    public abstract void onNeighbourBlockChanged(BlockPos pos, Direction direction);

    /**
     * Call when a block with cached save-delegate data is added to the multiblock.
     * The part will be notified that the data has been used after this call completes.
     * @param part The NBT tag containing this controller's data.
     */
    public abstract void onAttachedPartWithNetData(IEnergyNetPart part, CompoundNBT data);

    /**
     * Check if a block is being tracked by this machine.
     * @param blockCoord Coordinate to check.
     * @return True if the tile entity at blockCoord is being tracked by this machine, false otherwise.
     */
    public boolean hasBlock(BlockPos blockCoord) {
        return connectedParts.contains(blockCoord);
    }

    /**
     * Attach a new part to this machine.
     * @param part The part to add.
     */
    public void attachBlock(IEnergyNetPart part) {
        IEnergyNetPart candidate;
        BlockPos coord = part.getWorldPos();

        if(!connectedParts.add(part))
            Machinist.logger.warn("[%s] EnergyGrid %s is double-adding part %d @ %s. This is unusual. If you encounter odd behavior, please tear down the machine and rebuild it.",
                    (WORLD.isRemote ? "CLIENT" : "SERVER"), hashCode(), part.hashCode(), coord);

        part.onAttached(this);
        this.onBlockAdded(part);

        if(part.hasNetSaveData()) {
            CompoundNBT savedData = part.getNetSaveData();
            onAttachedPartWithNetData(part, savedData);
            part.onNetDataAssimilated();
        }

        if(this.referenceCoord == null) {
            referenceCoord = coord;
            part.becomeNetSaveDelegate();
        }
        else if(coord.compareTo(referenceCoord) < 0) {
            TileEntity te = this.WORLD.getTileEntity(referenceCoord);
            ((IEnergyNetPart)te).forfeitNetSaveDelegate();

            referenceCoord = coord;
            part.becomeNetSaveDelegate();
        }
        else {
            part.forfeitNetSaveDelegate();
        }


        REGISTRY.addDirtyGrid(WORLD, this);
    }
    /**
     * Called when a new part is added to the machine. Good time to register things into lists.
     * @param newPart The part being added.
     */
    protected abstract void onBlockAdded(IEnergyNetPart newPart);

    /**
     * Called when a part is removed from the machine. Good time to clean up lists.
     * @param oldPart The part being removed.
     */
    protected abstract void onBlockRemoved(IEnergyNetPart oldPart);

    public boolean isEmpty() {
        return connectedParts.isEmpty();
    }

    /**
     * Callback whenever a part is removed (or will very shortly be removed) from a controller.
     * Do housekeeping/callbacks, also nulls min/max coords.
     * @param part The part being removed.
     */
    private void onDetachBlock(IEnergyNetPart part) {
        // Strip out this part
        part.onDetached(this);
        this.onBlockRemoved(part);
        part.forfeitNetSaveDelegate();

        if(referenceCoord != null && referenceCoord.equals(part.getWorldPos())) {
            referenceCoord = null;
        }

        shouldCheckForDisconnections = true;
    }

    /**
     * Call to detach a block from this machine. Generally, this should be called
     * when the tile entity is being released, e.g. on block destruction.
     * @param part The part to detach from this machine.
     * @param chunkUnloading Is this entity detaching due to the chunk unloading? If true, the multiblock will be paused instead of broken.
     */
    public void detachBlock(IEnergyNetPart part, boolean chunkUnloading) {
//        if(chunkUnloading && this.assemblyState == MultiblockControllerBase.AssemblyState.Assembled) {
//            this.assemblyState = MultiblockControllerBase.AssemblyState.Paused;
//            this.onMachinePaused();
//        }

        // Strip out this part
        onDetachBlock(part);
        if(!connectedParts.remove(part)) {
            BlockPos position = part.getWorldPos();

            Machinist.logger.warn("[%s] Double-removing part (%d) @ %d, %d, %d, this is unexpected and may cause problems. If you encounter anomalies, please tear down the reactor and rebuild it.",
                    this.WORLD.isRemote ? "CLIENT" : "SERVER", part.hashCode(), position.getX(), position.getY(), position.getZ());
        }

        if(connectedParts.isEmpty()) {
            // Destroy/unregister
            REGISTRY.addDeadGrid(this.WORLD, this);
            return;
        }

        REGISTRY.addDirtyGrid(this.WORLD,  this);

        // Find new save delegate if we need to.
        if(referenceCoord == null) {
            selectNewReferenceCoord();
        }
    }
    /**
     * Assimilate another controller into this controller.
     * Acquire all of the other controller's blocks and attach them
     * to this one.
     *
     * @param other The controller to merge into this one.
     */
    public void assimilate(EnergyNetBase other) {
        BlockPos otherReferenceCoord = other.getReferenceCoord();
        if(otherReferenceCoord != null && getReferenceCoord().compareTo(otherReferenceCoord) >= 0) {
            throw new IllegalArgumentException("The controller with the lowest minimum-coord value must consume the one with the higher coords");
        }

        TileEntity te;
        Set<IEnergyNetPart> partsToAcquire = new HashSet<>(other.connectedParts);

        // releases all blocks and references gently so they can be incorporated into another multiblock
        other._onAssimilated(this);

        for(IEnergyNetPart acquiredPart : partsToAcquire) {
            // By definition, none of these can be the minimum block.
            if(acquiredPart.isPartInvalid()) { continue; }

            connectedParts.add(acquiredPart);
            acquiredPart.onAssimilated(this);
            this.onBlockAdded(acquiredPart);
        }

        this.onAssimilate(other);
        other.onAssimilated(this);
    }

    protected BlockPos getReferenceCoord() {
        if(referenceCoord == null) { selectNewReferenceCoord(); }
        return referenceCoord;
    }

    /**
     * Called when this machine is consumed by another controller.
     * Essentially, forcibly tear down this object.
     * @param otherController The controller consuming this controller.
     */
    private void _onAssimilated(EnergyNetBase otherController) {
        if(referenceCoord != null) {
            if (this.WORLD.isBlockLoaded(this.referenceCoord)) {
                TileEntity te = this.WORLD.getTileEntity(this.referenceCoord);
                if(te instanceof IMultiblockPart) {
                    ((IEnergyNetPart)te).forfeitNetSaveDelegate();
                }
            }
            this.referenceCoord = null;
        }

        connectedParts.clear();
    }

    /**
     * Callback. Called after this controller assimilates all the blocks
     * from another controller.
     * Use this to absorb that controller's game data.
     * @param assimilated The controller whose uniqueness was added to our own.
     */
    protected abstract void onAssimilate(EnergyNetBase assimilated);

    /**
     * Callback. Called after this controller is assimilated into another controller.
     * All blocks have been stripped out of this object and handed over to the
     * other controller.
     * This is intended primarily for cleanup.
     * @param assimilator The controller which has assimilated this controller.
     */
    protected abstract void onAssimilated(EnergyNetBase assimilator);


    public final void updateGrid() {
        if(connectedParts.isEmpty()) {
            REGISTRY.addDeadGrid(this.WORLD, this);
            return;
        }

        if(WORLD.isRemote)
            updateClient();
        else if(updateServer()) {
            IChunk chunkToSave = this.WORLD.getChunk(referenceCoord);
            chunkToSave.setModified(true);
        }


    }

    protected abstract boolean updateServer();

    protected abstract void updateClient();

    public abstract CompoundNBT write(CompoundNBT data);
    public abstract void read(CompoundNBT data);

    /**
     * Called when the save delegate's tile entity is being asked for its description packet
     * @param data A fresh compound tag to write your multiblock data into
     */
    public abstract void formatDescriptionPacket(CompoundNBT data);

    /**
     * Called when the save delegate's tile entity receiving a description packet
     * @param data A compound tag containing multiblock data to import
     */
    public abstract void decodeDescriptionPacket(CompoundNBT data);

    /**
     * Tests whether this multiblock should consume the other multiblock
     * and become the new multiblock master when the two multiblocks
     * are adjacent. Assumes both multiblocks are the same type.
     * @param otherController The other multiblock controller.
     * @return True if this multiblock should consume the other, false otherwise.
     */
    public boolean shouldConsume(EnergyNetBase otherController) {
        if(!otherController.getClass().equals(getClass())) {
            throw new IllegalArgumentException("Attempting to merge two energy grid with different master classes - this should never happen!");
        }

        if(otherController == this) { return false; } // Don't be silly, don't eat yourself.

        int res = _shouldConsume(otherController);
        if(res < 0) { return true; }
        else if(res > 0) { return false; }
        else {
            // Strip dead parts from both and retry
            Machinist.logger.warn("[%s] Encountered two grids with the same reference coordinate. Auditing connected parts and retrying.", this.WORLD.isRemote ? "CLIENT" : "SERVER");
            auditParts();
            otherController.auditParts();

            res = _shouldConsume(otherController);
            if(res < 0) { return true; }
            else if(res > 0) { return false; }
            else {
                Machinist.logger.error("My Controller (%d): size (%d), parts: %s", hashCode(), this.connectedParts.size(), this.getPartsListString());
                Machinist.logger.error("Other Controller (%d): size (%d), coords: %s", otherController.hashCode(), otherController.connectedParts.size(), otherController.getPartsListString());
                throw new IllegalArgumentException("[" + (this.WORLD.isRemote ? "CLIENT" : "SERVER") + "] Two controllers with the same reference coord that somehow both have valid parts - this should never happen!");
            }

        }
    }


    /**
     * Checks all of the parts in the controller. If any are dead or do not exist in the world, they are removed.
     */
    private void auditParts() {
        HashSet<IEnergyNetPart> deadParts = new HashSet<IEnergyNetPart>();
        for(IEnergyNetPart part : connectedParts) {
            if(part.isPartInvalid() || WORLD.getTileEntity(part.getWorldPos()) != part) {
                onDetachBlock(part);
                deadParts.add(part);
            }
        }

        connectedParts.removeAll(deadParts);
        Machinist.logger.warn("[%s] Grid found %d dead parts during an audit, %d parts remain attached", this.WORLD.isRemote ? "CLIENT" : "SERVER", deadParts.size(), this.connectedParts.size());
    }

    private String getPartsListString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        BlockPos partPos;
        for(IEnergyNetPart part : connectedParts) {
            if(!first) {
                sb.append(", ");
            }
            partPos = part.getWorldPos();
            sb.append(String.format("(%d: %d, %d, %d)", part.hashCode(), partPos.getX(), partPos.getY(), partPos.getZ()));
            first = false;
        }

        return sb.toString();
    }

    private int _shouldConsume(EnergyNetBase otherController) {
        BlockPos myCoord = getReferenceCoord();
        BlockPos theirCoord = otherController.getReferenceCoord();

        // Always consume other controllers if their reference coordinate is null - this means they're empty and can be assimilated on the cheap
        if(theirCoord == null) { return -1; }
        else { return myCoord.compareTo(theirCoord); }
    }

    private static final IEnergyNetRegistry REGISTRY;

    static {
        REGISTRY = Machinist.initEnergyNetRegistry();
    }

    /**
     * Called when this machine may need to check for blocks that are no
     * longer physically connected to the reference coordinate.
     * @return
     */
    public Set<IEnergyNetPart> checkForDisconnections() {
        if(!this.shouldCheckForDisconnections) {
            return null;
        }

        if(this.isEmpty()) {
            REGISTRY.addDeadGrid(WORLD, this);
            return null;
        }

        TileEntity te;
        AbstractChunkProvider chunkProvider = WORLD.getChunkProvider();

        // Invalidate our reference coord, we'll recalculate it shortly
        referenceCoord = null;

        // Reset visitations and find the minimum coordinate
        Set<IEnergyNetPart> deadParts = new HashSet<IEnergyNetPart>();
        BlockPos position;
        IEnergyNetPart referencePart = null;

        int originalSize = connectedParts.size();

        for(IEnergyNetPart part : connectedParts) {
            position = part.getWorldPos();
            // This happens during chunk unload.
            if (!this.WORLD.isBlockLoaded(position) || part.isPartInvalid()) {
                deadParts.add(part);
                onDetachBlock(part);
                continue;
            }

            if(WORLD.getTileEntity(position) != part) {
                deadParts.add(part);
                onDetachBlock(part);
                continue;
            }

            part.setUnvisited();
            part.forfeitNetSaveDelegate();

            if(referenceCoord == null) {
                referenceCoord = position;
                referencePart = part;
            }
            else if(position.compareTo(referenceCoord) < 0) {
                referenceCoord = position;
                referencePart = part;
            }
        }

        connectedParts.removeAll(deadParts);
        deadParts.clear();

        if(referencePart == null || isEmpty()) {
            // There are no valid parts remaining. The entire multiblock was unloaded during a chunk unload. Halt.
            shouldCheckForDisconnections = false;
            REGISTRY.addDeadGrid(WORLD, this);
            return null;
        }
        else {
            referencePart.becomeNetSaveDelegate();
        }

        // Now visit all connected parts, breadth-first, starting from reference coord's part
        IEnergyNetPart part;
        LinkedList<IEnergyNetPart> partsToCheck = new LinkedList<IEnergyNetPart>();
        IEnergyNetPart[] nearbyParts = null;
        int visitedParts = 0;

        partsToCheck.add(referencePart);

        while(!partsToCheck.isEmpty()) {
            part = partsToCheck.removeFirst();
            part.setVisited();
            visitedParts++;

            nearbyParts = part.getNeighboringParts(); // Chunk-safe on server, but not on client
            for(IEnergyNetPart nearbyPart : nearbyParts) {
                // Ignore different machines
                if(nearbyPart.getGridController() != this) {
                    continue;
                }

                if(!nearbyPart.isVisited()) {
                    nearbyPart.setVisited();
                    partsToCheck.add(nearbyPart);
                }
            }
        }

        // Finally, remove all parts that remain disconnected.
        Set<IEnergyNetPart> removedParts = new HashSet<IEnergyNetPart>();
        for(IEnergyNetPart orphanCandidate : connectedParts) {
            if (!orphanCandidate.isVisited()) {
                deadParts.add(orphanCandidate);
                orphanCandidate.onOrphaned(this, originalSize, visitedParts);
                onDetachBlock(orphanCandidate);
                removedParts.add(orphanCandidate);
            }
        }

        // Trim any blocks that were invalid, or were removed.
        connectedParts.removeAll(deadParts);

        // Cleanup. Not necessary, really.
        deadParts.clear();

        // Juuuust in case.
        if(referenceCoord == null) {
            selectNewReferenceCoord();
        }

        // We've run the checks from here on out.
        shouldCheckForDisconnections = false;

        return removedParts;
    }

    /**
     * Detach all parts. Return a set of all parts which still
     * have a valid tile entity. Chunk-safe.
     * @return A set of all parts which still have a valid tile entity.
     */
    public Set<IEnergyNetPart> detachAllBlocks() {
        if(WORLD == null) { return new HashSet<IEnergyNetPart>(); }

        AbstractChunkProvider chunkProvider = WORLD.getChunkProvider();
        for(IEnergyNetPart part : connectedParts) {
            if(this.WORLD.isBlockLoaded(part.getWorldPos())) {
                onDetachBlock(part);
            }
        }

        Set<IEnergyNetPart> detachedParts = connectedParts;
        connectedParts = new HashSet<IEnergyNetPart>();
        return detachedParts;
    }

    private void selectNewReferenceCoord() {
        AbstractChunkProvider chunkProvider = WORLD.getChunkProvider();
        IEnergyNetPart theChosenOne = null;
        BlockPos position;

        referenceCoord = null;

        for(IEnergyNetPart part : connectedParts) {
            position = part.getWorldPos();
            if(part.isPartInvalid() || !this.WORLD.isBlockLoaded(position)) {
                // Chunk is unloading, skip this coord to prevent chunk thrashing
                continue;
            }

            if(referenceCoord == null || referenceCoord.compareTo(position) > 0) {
                referenceCoord = position;
                theChosenOne = part;
            }
        }

        if(theChosenOne != null) {
            theChosenOne.becomeNetSaveDelegate();
        }
    }

    /**
     * Marks the reference coord dirty & updateable.
     *
     * On the server, this will mark the for a data-update, so that
     * nearby clients will receive an updated description packet from the server
     * after a short time. The block's chunk will also be marked dirty and the
     * block's chunk will be saved to disk the next time chunks are saved.
     *
     * On the client, this will mark the block for a rendering update.
     */
    protected void markReferenceCoordForUpdate() {

        BlockPos rc = this.getReferenceCoord();

        if ((this.WORLD != null) && (rc != null))
            WorldHelpers.notifyBlockUpdate(this.WORLD, rc, null, null);
    }

    /**
     * Marks the reference coord dirty.
     *
     * On the server, this marks the reference coord's chunk as dirty; the block (and chunk)
     * will be saved to disk the next time chunks are saved. This does NOT mark it dirty for
     * a description-packet update.
     *
     * On the client, does nothing.
     * @see EnergyNetBase#markReferenceCoordForUpdate()
     */
    protected void markReferenceCoordDirty() {
        if(WORLD == null || WORLD.isRemote) { return; }

        BlockPos referenceCoord = this.getReferenceCoord();
        if(referenceCoord == null) { return; }

        TileEntity saveTe = WORLD.getTileEntity(referenceCoord);
        WORLD.markChunkDirty(referenceCoord, saveTe);
    }

    /**
     * Marks the whole multiblock for a render update on the client. On the server, this does nothing
     */
    protected void markMultiblockForRenderUpdate() {
//        for(int x = getMinimumCoord().getX(); x <= this.getMaximumCoord().getX(); x++)
//            for(int y = getMinimumCoord().getY(); y <= this.getMaximumCoord().getY(); y++)
//                for(int z = getMinimumCoord().getZ(); z <= this.getMaximumCoord().getZ(); z++) {
//                    BlockPos p = new BlockPos(x, y, z);
//                    this.WORLD.markBlockRangeForRenderUpdate(p, WORLD.getBlockState(p), WORLD.getBlockState(p));
//                }
    }
}
