package one.tlph.machinist.blocks.BlastFurnace;


import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import one.tlph.machinist.api.multiblock.validation.IMultiblockValidator;
import one.tlph.machinist.init.ModTileEntityTypes;


public class BlastFurnaceCasingTileEntity extends RectangularMultiblockTileEntityBase {
    public BlastFurnaceCasingTileEntity() {
        super(ModTileEntityTypes.BLAST_FURNACE_CASING.get());
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
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return BlastFurnaceMultiBlockTileEntity.class;
    }

    @Override
    public boolean isGoodForFrame(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    public boolean isGoodForSides(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    public boolean isGoodForTop(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    public boolean isGoodForBottom(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    public boolean isGoodForInterior(IMultiblockValidator validatorCallback) {
        return false;
    }



}
