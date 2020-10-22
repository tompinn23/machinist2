package one.tlph.machinist.tileentity;

import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.MultiblockTileEntityBase;
import one.tlph.machinist.init.ModTileEntityTypes;

public class CableTileEntity extends MultiblockTileEntityBase {
    public CableTileEntity() {
        super(ModTileEntityTypes.CABLE_TILE_ENTITY.get());
    }

    @Override
    public void onMachineAssembled(MultiblockControllerBase multiblockControllerBase) {

    }

    @Override
    public void onMachineBroken() {

    }

    @Override
    public void onMachineActivated() {

    }

    @Override
    public void onMachineDeactivated() {

    }

    @Override
    public MultiblockControllerBase createNewMultiblock() {
        return new EnergyNetTileEntity(this.world);
    }

    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return EnergyNetTileEntity.class;
    }
}
