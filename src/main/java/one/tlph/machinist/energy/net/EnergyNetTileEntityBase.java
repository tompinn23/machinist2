package one.tlph.machinist.energy.net;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.api.multiblock.IMultiblockRegistry;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EnergyNetTileEntityBase extends TileEntity implements IEnergyNetPart {

    private EnergyNetBase grid;
    private boolean visited;

    private boolean saveGridData;
    private CompoundNBT cachedGridData;
    private boolean paused;

    public EnergyNetTileEntityBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        grid = null;
        visited = false;
        saveGridData = false;
        paused = false;
        cachedGridData = null;
    }


    @Override
    public BlockPos getWorldPos() {
        return this.pos;
    }

    @Override
    public boolean isPartInvalid() {
        return this.removed;
    }

    @Override
    public Set<EnergyNetBase> attachToNeighbours() {
        Set<EnergyNetBase> controllers = null;
        EnergyNetBase bestController = null;

        // Look for a compatible controller in our neighboring parts.
        IEnergyNetPart[] partsToCheck = getNeighboringParts();
        for (IEnergyNetPart neighborPart : partsToCheck) {
            if (neighborPart.isConnected()) {
                EnergyNetBase candidate = neighborPart.getGridController();
                if (!candidate.getClass().equals(this.getGridControllerType())) {
                    // Skip multiblocks with incompatible types
                    continue;
                }

                if (controllers == null) {
                    controllers = new HashSet<>();
                    bestController = candidate;
                } else if (!controllers.contains(candidate) && candidate.shouldConsume(bestController)) {
                    bestController = candidate;
                }

                controllers.add(candidate);
            }
        }
        if(bestController != null) {
            // attachBlock will call onAttached, which will set the controller.
            this.grid = bestController;
            bestController.attachBlock(this);
        }

        return controllers;
    }

    @Override
    public abstract EnergyNetBase createNewGrid();

    @Override
    public void assertDetached() {
        if(this.grid != null) {
            BlockPos coord = this.getWorldPos();

            Machinist.logger.info("[assert] Part @ (%d, %d, %d) should be detached already, but detected that it was not. This is not a fatal error, and will be repaired, but is unusual.",
                    coord.getX(), coord.getY(), coord.getZ());
            this.grid = null;
        }
    }

    @Override
    public void onAttached(EnergyNetBase energyNetBase) {
        this.grid = energyNetBase;
    }

    @Override
    public boolean hasNetSaveData() {
        return this.cachedGridData != null;
    }

    @Override
    public void onNetDataAssimilated() {
        this.cachedGridData = null;
    }

    @Override
    public void becomeNetSaveDelegate() {
        this.saveGridData = true;
    }

    @Override
    public void forfeitNetSaveDelegate() {
        this.saveGridData = false;
    }

    @Override
    public boolean isGridSaveDelegate() {
        return this.saveGridData;
    }

    @Override
    public void onDetached(EnergyNetBase energyNetBase) {
        this.grid = null;
    }

    @Override
    public void onAssimilated(EnergyNetBase energyNetBase) {
        assert(this.grid != energyNetBase);
        this.grid = energyNetBase;
    }

    @Override
    public CompoundNBT getNetSaveData() {
        return this.cachedGridData;
    }

    @Override
    public void setUnvisited() {
        this.visited = false;
    }

    @Override
    public void setVisited() {
        this.visited = true;
    }

    @Override
    public IEnergyNetPart[] getNeighboringParts() {
        TileEntity te;
        List<IEnergyNetPart> neighborParts = new ArrayList<>();
        BlockPos neighborPosition, partPosition = this.getWorldPos();

        for (Direction facing : Direction.values()) {

            neighborPosition = partPosition.offset(facing);
            te = this.world.getTileEntity(neighborPosition);

            if (te instanceof IEnergyNetPart)
                neighborParts.add((IEnergyNetPart) te);
        }

        return neighborParts.toArray(new IEnergyNetPart[neighborParts.size()]);
    }

    @Override
    public boolean isVisited() {
        return this.visited;
    }

    @Override
    public void onOrphaned(EnergyNetBase energyNetBase, int originalSize, int visitedParts) {
        this.markDirty();
        world.markChunkDirty(this.getWorldPos(), this);
    }

    @Override
    public EnergyNetBase getGridController() {
        return this.grid;
    }

    @Override
    public boolean isConnected() {
        return (grid != null);
    }




    /*
     TileEntity Shit.
     */
    @Override
    public void validate() {
        super.validate();
        REGISTRY.onConnectorAdded(this.world, this);
    }

    @Override
    public void remove() {
        super.remove();
        detachSelf(false);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if(nbt.contains("gridData")) {
            this.cachedGridData = nbt.getCompound("gridData");
        }
    }


    @Override
    public CompoundNBT write(CompoundNBT data) {
        super.write(data);

        if(isGridSaveDelegate() && isConnected()) {
            CompoundNBT multiblockData = new CompoundNBT();
            this.grid.write(multiblockData);
            data.put("gridData", multiblockData);
        }
        return data;
    }

    @Override
    public void onDataPacket(NetworkManager network, SUpdateTileEntityPacket packet) {
        decodeDescriptionPacket(packet.getNbtCompound());
    }

    protected void encodeDescriptionPacket(CompoundNBT packetData) {
        if(this.isGridSaveDelegate() && isConnected()) {
            CompoundNBT tag = new CompoundNBT();
            getGridController().formatDescriptionPacket(tag);
            packetData.put("gridData", tag);
        }
    }

    /**
     * Override this to easily read in data from a TileEntity's description packet.
     * Encoded in encodeDescriptionPacket.
     * @param packetData The NBT data from the tile entity's description packet.
     * @see
     */
    protected void decodeDescriptionPacket(CompoundNBT packetData) {
        if(packetData.contains("gridData")) {
            CompoundNBT tag = packetData.getCompound("gridData");
            if(isConnected()) {
                getGridController().decodeDescriptionPacket(tag);
            }
            else {
                // This part hasn't been added to a machine yet, so cache the data.
                this.cachedGridData = tag;
            }
        }
    }

    /*
     * Detaches this block from its controller. Calls detachBlock() and clears the controller member.
     */
    protected void detachSelf(boolean chunkUnloading) {
        if(this.grid != null) {
            // Clean part out of controller
            this.grid.detachBlock(this, chunkUnloading);

            // The above should call onDetached, but, just in case...
            this.grid = null;
        }

        // Clean part out of lists in the registry
        REGISTRY.onConnectorRemoved(world, this);
    }

    private static final IEnergyNetRegistry REGISTRY;

    static {
        REGISTRY = Machinist.initEnergyNetRegistry();
    }


}
