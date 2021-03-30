package one.tlph.machinist.blocks.PortalHeater;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import one.tlph.machinist.blocks.RemeltedNetherrack.PortalRemeltedTileEntity;
import one.tlph.machinist.client.renderer.PortalHeater;
import one.tlph.machinist.energy.Energy;
import one.tlph.machinist.energy.TransferType;
import one.tlph.machinist.init.registries.ModTileEntityTypes;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;

public class PortalHeaterTileEntity extends AbstractPoweredTileEntity<BlockPortalHeater> {

    private boolean transmittingPower = false;

    public int laserDistance;

    public PortalHeaterTileEntity() {
        super(ModTileEntityTypes.PORTAL_HEATER.get());
        this.energyStorage.setRecieve(10000);
        this.energyStorage.setExtract(10000);
        this.energyStorage.setCapacity(50000);
        this.getSidedConfig().init(TransferType.IN);
    }

    @Override
    public int postTick() {
        if(!isRemote()) {
            if (!transmittingPower) {
                if (this.getTicks() % 100 == 0) {
                    checkPortal();
                    sync(4);
                }
                return 50;
            } else {
                checkPortal();
                if (transmittingPower)
                    transmitPower();
            }
        }
        return 4;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos, pos.offset(getBlockState().get(BlockPortalHeater.FACING), laserDistance));
    }

    @Override
    protected void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        laserDistance = nbt.getInt("laserDistance");
    }

    @Override
    protected CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putInt("laserDistance", laserDistance);
        return super.writeSync(nbt);
    }

    private void transmitPower() {
        BlockPos portal = pos.offset(this.getBlockState().get(BlockPortalHeater.FACING), laserDistance).down();
        TileEntity te = world.getTileEntity(portal);
        if(te instanceof PortalRemeltedTileEntity) {
            int amt = ((PortalRemeltedTileEntity)te).haveSomeEnergy(energyStorage.extractEnergy(10000, true));
            energyStorage.extractEnergy(amt, false);
        }
    }

    private void checkPortal() {
        for(int i = 2; i < 16; i++) {
            BlockPos portal = pos.offset(this.getBlockState().get(BlockPortalHeater.FACING), i).down();
            TileEntity te = world.getTileEntity(portal);
            if (te instanceof PortalRemeltedTileEntity) {
                laserDistance = i;
                transmittingPower = true;
                world.setBlockState(pos, this.getBlockState().with(BlockPortalHeater.ACTIVE, true));
                return;
            }
        }
        laserDistance = -1;
        transmittingPower = false;
        world.setBlockState(pos, this.getBlockState().with(BlockPortalHeater.ACTIVE, false));

    }
}