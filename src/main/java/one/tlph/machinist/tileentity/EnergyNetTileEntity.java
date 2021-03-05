package one.tlph.machinist.tileentity;

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.validation.IMultiblockValidator;
import one.tlph.machinist.energy.EnergyUtils;
import one.tlph.machinist.energy.ResizableEnergyStore;
import one.tlph.machinist.energy.net.EnergyNetBase;
import one.tlph.machinist.energy.net.IEnergyNetPart;
import one.tlph.machinist.energy.net.IEnergyNetRegistry;
import org.lwjgl.system.CallbackI;

import java.util.*;
import java.util.stream.Collectors;

public class EnergyNetTileEntity extends EnergyNetBase {

    protected EnergyNetTileEntity(World world) {
        super(world);
        energyStorage = new ResizableEnergyStore(1000, 1000, 1000);
        acceptors = new HashMap<>();
        suppliers = new HashMap<>();
    }


    private IEnergyNetPart delegatePart = null;
    private HashMap<BlockPos, TileEntity> acceptors;
    private HashMap<BlockPos, TileEntity> suppliers;
    private ResizableEnergyStore energyStorage;
    private final int transferRate = 1000;
    private int ticksSinceLastChange = 0;



    @Override
    public void onAttachedPartWithNetData(IEnergyNetPart part, CompoundNBT data) {
        if(part != delegatePart)
            delegatePart = part;
        read(data);
    }

    @Override
    protected void onBlockAdded(IEnergyNetPart newPart) {
        BlockPos pos = newPart.getWorldPos();
        for(Direction d : Direction.values()) {
            if(WORLD.isBlockPresent(pos.offset(d))) {
                TileEntity te = WORLD.getTileEntity(pos.offset(d));
                if(EnergyUtils.isPresent(te, d)) {
                    if(EnergyUtils.canReceive(te, d)) {
                        acceptors.put(pos.offset(d), te);
                    }
                }
                if(EnergyUtils.canExtract(te, d)) {
                    suppliers.put(pos, te);
                }
            }
        }
        recalculateEnergy();
    }

    private void recalculateEnergy() {
        energyStorage.setCapacity(connectedParts.size() * 1000);
    }

    public void onNeighbourBlockChanged(BlockPos pos, Direction side) {
        if(WORLD.isBlockPresent(pos.offset(side))) {
            TileEntity te = WORLD.getTileEntity(pos.offset(side));
            if(te instanceof IEnergyNetPart)
                return;
            acceptors.remove(pos.offset(side));
            suppliers.remove(pos.offset(side));
            if(EnergyUtils.isPresent(te,side)) {
                if(EnergyUtils.canExtract(te, side)) {
                    suppliers.put(pos.offset(side), te);
                }
                if(EnergyUtils.canReceive(te, side)) {
                    acceptors.put(pos.offset(side), te);
                }
            }
        }
        else {
            acceptors.remove(pos.offset(side));
            suppliers.remove(pos.offset(side));
        }
    }

    public void onNeighbourTileChanged(BlockPos pos, Direction side) {
//        if(WORLD.isBlockPresent(pos.offset(side))) {
//            TileEntity te = WORLD.getTileEntity(pos.offset(side));
//            if(te == null || te instanceof EnergyConduitTileEntity)
//                return;
//            if(te instanceof IEnergyStorage)
//                acceptors.get(pos).set(side.getIndex(), (IEnergyStorage)te);
//            else {
//                acceptors.get(pos).set(side.getIndex(), null);
//            }
//        }
//        else {
//            acceptors.get(pos).set(side.getIndex(), null);
//        }
    }

    @Override
    protected void onBlockRemoved(IEnergyNetPart oldPart) {
        if(acceptors.containsKey(oldPart.getWorldPos())) {
            acceptors.remove(oldPart.getWorldPos());
        }
        recalculateEnergy();
    }

    @Override
    protected void onAssimilate(EnergyNetBase assimilated) {

    }

    @Override
    protected void onAssimilated(EnergyNetBase assimilator) {

    }


    @Override
    protected boolean updateServer() {
        boolean ret = false;
        if(suppliers.size() > 0) {


            Iterator<Map.Entry<BlockPos, TileEntity>> iterator = suppliers.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<BlockPos, TileEntity> kv = iterator.next();
                IEnergyStorage store = kv.getValue().getCapability(CapabilityEnergy.ENERGY).orElse(null);
                if(store == null) {
                    iterator.remove();
                    continue;
                }
                int actual = energyStorage.receiveEnergy(store.extractEnergy(transferRate, true), false);
                store.extractEnergy(actual, false);
            }
            ret = true;
        }

        if(acceptors.size() > 0) {
            int perMachine = energyStorage.getEnergyStored() / acceptors.size();
            Iterator<Map.Entry<BlockPos, TileEntity>> iterator = acceptors.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<BlockPos, TileEntity> kv = iterator.next();
                if(!energyStorage.canExtract())
                    break;
                IEnergyStorage store = kv.getValue().getCapability(CapabilityEnergy.ENERGY).orElse(null);
                if(store == null) {
                    iterator.remove();
                    continue;
                }
                int actual = energyStorage.extractEnergy(store.receiveEnergy(perMachine, true), false);
                store.receiveEnergy(actual, false);
            }
            ret = true;
        }
        return ret;
    }

    @Override
    protected void updateClient() {

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
