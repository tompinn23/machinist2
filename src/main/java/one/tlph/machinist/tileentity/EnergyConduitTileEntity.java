package one.tlph.machinist.tileentity;

import one.tlph.machinist.energy.net.EnergyNetBase;
import one.tlph.machinist.energy.net.EnergyNetTileEntityBase;
import one.tlph.machinist.init.ModTileEntityTypes;

public class EnergyConduitTileEntity extends EnergyNetTileEntityBase {
    public EnergyConduitTileEntity() {
        super(ModTileEntityTypes.CABLE_TILE_ENTITY.get());
    }

    @Override
    public EnergyNetBase createNewGrid() {
        return new EnergyNetTileEntity(this.world);
    }

    @Override
    public Class<? extends EnergyNetBase> getGridControllerType() {
        return EnergyNetTileEntity.class;
    }





}
