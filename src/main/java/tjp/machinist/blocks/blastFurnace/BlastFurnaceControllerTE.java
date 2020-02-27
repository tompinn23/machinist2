package tjp.machinist.blocks.blastFurnace;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import tjp.machinist.api.multiblock.MultiblockControllerBase;
import tjp.machinist.api.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import tjp.machinist.api.multiblock.validation.IMultiblockValidator;
import tjp.machinist.recipes.BlastFurnaceRecipes;

public class BlastFurnaceControllerTE extends RectangularMultiblockTileEntityBase {
    private BlastFurnaceRecipes recipeHandler;

    private static final int MACHINE_SZ = 3;



    private EnumFacing facing;

    public BlastFurnaceControllerTE() {
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

    public BlastFurnaceControllerTE(EnumFacing facing) {
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
        //super.writeToNBT(nbt);
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
        return new BlastFurnaceMultiController(this.world);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        //super.readFromNBT(nbt);
        facing = EnumFacing.values()[nbt.getInteger("facing")];
    }

//    @Override
//    public void checkPattern() {
//        EnumFacing machineDirection = getFacing().getOpposite();
//        BlockPos topRight = pos.up().offset(machineDirection.rotateY());
//        BlockPos bottomLeft = pos.down().offset(machineDirection.rotateYCCW()).offset(machineDirection,2);
//        BlockPos min = WorldHelpers.getMinCoord(topRight, bottomLeft);
//        BlockPos max = WorldHelpers.getMaxCoord(topRight, bottomLeft);
//
//        BlockPos airPos = pos.offset(machineDirection);
//
//        if(world.getBlockState(airPos).getBlock() != Blocks.AIR) {
//            return;
//        }
//
//        for(BlockPos curPos : BlockPos.getAllInBox(min, max)) {
//            TileEntity te = null;
//            if(curPos == pos)
//                continue;
//            if(curPos == airPos)
//                continue;
//            if((te = world.getTileEntity(curPos)) == null) {
//                return;
//            }
//            if(te instanceof MultiblockBaseTileEntity) {
//                if(((MultiblockBaseTileEntity)te).getMultiblockControllerType() == this.getClass()) {
//                    continue;
//                }
//            }
//            else {
//                return;
//            }
//        }
//        formed = true;
//    }




    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return BlastFurnaceMultiController.class;
    }
}
