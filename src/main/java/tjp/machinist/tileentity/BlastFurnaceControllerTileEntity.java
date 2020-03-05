package tjp.machinist.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import tjp.machinist.api.multiblock.MultiblockControllerBase;
import tjp.machinist.api.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import tjp.machinist.api.multiblock.validation.IMultiblockValidator;
import tjp.machinist.recipes.BlastFurnaceRecipes;

import javax.annotation.Nullable;

public class BlastFurnaceControllerTileEntity extends RectangularMultiblockTileEntityBase {
    private BlastFurnaceRecipes recipeHandler;

    private static final int MACHINE_SZ = 3;



    private EnumFacing facing;

    public BlastFurnaceControllerTileEntity() {
        super();
        this.facing = EnumFacing.NORTH;
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

    public BlastFurnaceControllerTileEntity(EnumFacing facing) {
        super();
        this.facing = facing;
    }


    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }

    public EnumFacing getFacing() {
        return facing;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("facing", facing.getIndex());
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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facing = EnumFacing.values()[nbt.getInteger("facing")];
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }




    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return BlastFurnaceMultiBlockTileEntity.class;
    }
}
