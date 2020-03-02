package tjp.machinist.blocks.BlastFurnace;


import tjp.machinist.api.multiblock.MultiblockControllerBase;
import tjp.machinist.api.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import tjp.machinist.api.multiblock.validation.IMultiblockValidator;


public class BlastFurnaceCasingTE extends RectangularMultiblockTileEntityBase {
    public BlastFurnaceCasingTE() {
        super();
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
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return BlastFurnaceMultiController.class;
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
