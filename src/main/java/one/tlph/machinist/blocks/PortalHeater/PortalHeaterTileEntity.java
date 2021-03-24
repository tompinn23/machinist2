package one.tlph.machinist.blocks.PortalHeater;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import one.tlph.machinist.blocks.RemeltedNetherrack.PortalRemeltedTileEntity;
import one.tlph.machinist.energy.Energy;
import one.tlph.machinist.energy.TransferType;
import one.tlph.machinist.init.registries.ModTileEntityTypes;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;

public class PortalHeaterTileEntity extends AbstractPoweredTileEntity<BlockPortalHeater> {

    private boolean transmittingPower = false;

    public PortalHeaterTileEntity() {
        super(ModTileEntityTypes.PORTAL_HEATER.get());
        this.energyStorage.setRecieve(1000);
        this.energyStorage.setExtract(10000);
        this.energyStorage.setCapacity(50000);
        this.getSidedConfig().init(TransferType.IN);
    }

    @Override
    public int postTick() {
        if(!transmittingPower) {
            if (this.getTicks() % 100 == 0) {
                checkPortal();
            }
            return 50;
        }
        else {
                checkPortal();
                if(transmittingPower)
                    transmitPower();
            }
        return 4;
    }

    private void transmitPower() {
        BlockPos portal = pos.offset(this.getBlockState().get(BlockPortalHeater.FACING), 2).down();
        TileEntity te = world.getTileEntity(portal);
        if(te instanceof PortalRemeltedTileEntity) {
            int amt = ((PortalRemeltedTileEntity)te).haveSomeEnergy(energyStorage.extractEnergy(10000, true));
            energyStorage.extractEnergy(amt, false);
        }
    }

    private void checkPortal() {
        BlockPos portal = pos.offset(this.getBlockState().get(BlockPortalHeater.FACING), 2).down();
        TileEntity te = world.getTileEntity(portal);
        if(te instanceof PortalRemeltedTileEntity) {
            transmittingPower = true;
            world.setBlockState(pos, this.getBlockState().with(BlockPortalHeater.ACTIVE, true));
            return;
        }
        transmittingPower = false;
        world.setBlockState(pos, this.getBlockState().with(BlockPortalHeater.ACTIVE, false));

    }
}