package one.tlph.machinist.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import one.tlph.machinist.api.multiblock.validation.IMultiblockValidator;
import one.tlph.machinist.proxy.TileEntityTypes;

import javax.annotation.Nullable;

public class BlastFurnaceControllerTileEntity extends RectangularMultiblockTileEntityBase {


    private static final int MACHINE_SZ = 3;




    public BlastFurnaceControllerTileEntity() {
        super(TileEntityTypes.BLAST_FURNACE_CONTROLLER.get());
    }

    @Override
    public boolean isGoodForFrame(IMultiblockValidator validatorCallback) {
        return false;
    }

    @Override
    public boolean isGoodForSides(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    public boolean isGoodForTop(IMultiblockValidator validatorCallback) {
        return false;
    }

    @Override
    public boolean isGoodForBottom(IMultiblockValidator validatorCallback) {
        return false;
    }

    @Override
    public boolean isGoodForInterior(IMultiblockValidator validatorCallback) {
        return false;
    }

//    public BlastFurnaceControllerTileEntity(EnumFacing facing) {
//        super();
//        this.facing = facing;
//    }


//    public void setFacing(EnumFacing facing) {
//        this.facing = facing;
//    }
//
//    public EnumFacing getFacing() {
//        return facing;
//    }


    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        return nbt;
    }

    @Override
    public void onMachineActivated() {

    }

    @Override
    public void onMachineDeactivated() {

    }

    @Override
    public MultiblockControllerBase createNewMultiblock() {
        return new BlastFurnaceMultiBlockTileEntity(this.world);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        //facing = EnumFacing.values()[nbt.getInteger("facing")];
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }




    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return BlastFurnaceMultiBlockTileEntity.class;
    }
}
