package one.tlph.machinist.blocks.RemeltedNetherrack;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import one.tlph.machinist.blocks.PortalHeater.PortalHeaterTileEntity;
import one.tlph.machinist.energy.Energy;
import one.tlph.machinist.energy.TransferType;
import one.tlph.machinist.init.registries.ModTileEntityTypes;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;
import one.tlph.machinist.tileentity.AbstractTileEntity;

import static one.tlph.machinist.blocks.RemeltedNetherrack.RemeltedNetherrack.BOTTOM;
import static one.tlph.machinist.blocks.RemeltedNetherrack.RemeltedNetherrack.TOP;

public class PortalRemeltedTileEntity extends AbstractPoweredTileEntity<RemeltedNetherrack> {

    private boolean portalActive = false;

    public PortalRemeltedTileEntity() {
        super(ModTileEntityTypes.PORTAL_REMELTED.get());
        this.energyStorage.setCapacity(2000000);
        this.energyStorage.setRecieve(2000000);
        this.getSidedConfig().init(TransferType.NONE);
    }


    @Override
    protected int postTick() {
        if(energyStorage.getEnergyStored() >= 1500000)
            if(!portalActive)
                activatePortal();
        if(portalActive)
            maintainPortal();
        return 4;
    }

    private void maintainPortal() {
        int amt = energyStorage.extractEnergy(50, false);
        if(amt < 50)
            deactivatePortal();

    }


    public int haveSomeEnergy(int energyRecieved) {
        int ret = energyStorage.receiveEnergy(energyRecieved, false);
        return ret;
    }

    private void activatePortal() {
        world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
        world.createExplosion(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1.5F, Explosion.Mode.NONE);
        portalActive = true;
        energyStorage.consume(1500000);
    }

    private void deactivatePortal() {

    }

}
