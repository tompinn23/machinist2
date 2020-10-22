package one.tlph.machinist.tileentity;

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.validation.IMultiblockValidator;
import one.tlph.machinist.energy.ResizableEnergyStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EnergyNetTileEntity extends MultiblockControllerBase {

    protected EnergyNetTileEntity(World world) {
        super(world);
        energyStorage = new ResizableEnergyStore(1000, 1000, 1000);
        acceptors = new HashMap<>();
    }

    private IMultiblockPart delegatePart = null;
    private HashMap<BlockPos, List<IEnergyStorage>> acceptors;
    private ResizableEnergyStore energyStorage;
    private final int transferRate = 1000;
    private int ticksSinceLastChange = 0;

    @Override
    public void onBlockActivated(BlockPos pos) {

    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart part, CompoundNBT data) {
        if(part != delegatePart)
            delegatePart = part;
        read(data);
    }

    @Override
    protected void onBlockAdded(IMultiblockPart newPart) {
        List<IEnergyStorage> acceptors = new ArrayList<>();
        BlockPos pos = newPart.getWorldPosition();
        for(Direction d : Direction.values()) {
            if(WORLD.isBlockPresent(pos.offset(d))) {
                TileEntity te = WORLD.getTileEntity(pos.offset(d));
                if(te instanceof IEnergyStorage) {
                    if(((IEnergyStorage) te).canReceive()) {
                        acceptors.add((IEnergyStorage)te);
                    }
                }
            }
        }
        this.acceptors.put(pos, acceptors);
        recalculateEnergy();
    }

    private void recalculateEnergy() {
        energyStorage.setCapacity(connectedParts.size() * 100);
    }

    public void onNeighbourBlockChanged(BlockPos pos, Direction side) {
        if(WORLD.isBlockPresent(pos.offset(side))) {
            TileEntity te = WORLD.getTileEntity(pos.offset(side));
            if(te == null || te instanceof CableTileEntity)
                return;
            if(te instanceof IEnergyStorage)
                acceptors.get(pos).set(side.getIndex(), (IEnergyStorage)te);
            else {
                acceptors.get(pos).set(side.getIndex(), null);
            }
        }
        else {
            acceptors.get(pos).set(side.getIndex(), null);
        }
    }

    public void onNeighbourTileChanged(BlockPos pos, Direction side) {
        if(WORLD.isBlockPresent(pos.offset(side))) {
            TileEntity te = WORLD.getTileEntity(pos.offset(side));
            if(te == null || te instanceof CableTileEntity)
                return;
            if(te instanceof IEnergyStorage)
                acceptors.get(pos).set(side.getIndex(), (IEnergyStorage)te);
            else {
                acceptors.get(pos).set(side.getIndex(), null);
            }
        }
        else {
            acceptors.get(pos).set(side.getIndex(), null);
        }
    }

    @Override
    protected void onBlockRemoved(IMultiblockPart oldPart) {
        if(acceptors.containsKey(oldPart.getWorldPosition())) {
            acceptors.remove(oldPart.getWorldPosition());
        }
        recalculateEnergy();
    }

    @Override
    protected void onMachineAssembled() {

    }

    @Override
    protected void onMachineRestored() {

    }

    @Override
    protected void onMachinePaused() {

    }

    @Override
    protected void onMachineDisassembled() {

    }

    @Override
    protected int getMinimumNumberOfBlocksForAssembledMachine() {
        return 0;
    }

    @Override
    protected int getMaximumXSize() {
        return 0;
    }

    @Override
    protected int getMaximumZSize() {
        return 0;
    }

    @Override
    protected int getMaximumYSize() {
        return 0;
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    protected void onAssimilate(MultiblockControllerBase assimilated) {

    }

    @Override
    protected void onAssimilated(MultiblockControllerBase assimilator) {

    }

    @Override
    protected boolean updateServer() {
            energyStorage.receiveEnergy(200, false);
        if(!energyStorage.canExtract()) {
            return false;
        }
        List<IEnergyStorage> flattened = acceptors.values().stream()
                .filter(s -> s != null)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if(flattened.size() == 0)
            return false;
        Collections.shuffle(flattened);
        flattened.forEach((IEnergyStorage eStore) -> {
            int drain = Math.min(energyStorage.getEnergyStored(), transferRate);
            if(drain > 0 && eStore.receiveEnergy(drain, true) > 0) {
                int move = eStore.receiveEnergy(drain, false);
                energyStorage.extractEnergy(move, false);
            }
        });
        return true;
    }

    @Override
    protected void updateClient() {

    }

    @Override
    protected boolean isBlockGoodForFrame(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForTop(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForBottom(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForSides(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        return null;
    }

    @Override
    public void read(CompoundNBT data) {

    }

    @Override
    public void formatDescriptionPacket(CompoundNBT data) {

    }

    @Override
    public void decodeDescriptionPacket(CompoundNBT data) {

    }
}
