package one.tlph.machinist.blocks.RemeltedNetherrack;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.blocks.PortalHeater.PortalHeaterTileEntity;
import one.tlph.machinist.energy.Energy;
import one.tlph.machinist.energy.TransferType;
import one.tlph.machinist.init.registries.Dimensions;
import one.tlph.machinist.init.registries.ModBlocks;
import one.tlph.machinist.init.registries.ModTileEntityTypes;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;
import one.tlph.machinist.tileentity.AbstractTileEntity;

import static one.tlph.machinist.blocks.RemeltedNetherrack.RemeltedNetherrack.*;

public class PortalRemeltedTileEntity extends AbstractPoweredTileEntity<RemeltedNetherrack> {

    private boolean portalActive = false;
    private boolean portalActiveClient = false;

    private long activeTicks = 0;

    public PortalRemeltedTileEntity() {
        super(ModTileEntityTypes.PORTAL_REMELTED.get());
        this.energyStorage.setCapacity(2000000);
        this.energyStorage.setRecieve(2000000);
        this.getSidedConfig().init(TransferType.NONE);
    }


    @Override
    protected int postTick() {
        if(!isRemote()) {
            if (energyStorage.getEnergyStored() >= 150000)
                if (!portalActive)
                    activatePortal();
            if (portalActive)
                maintainPortal();
        }
        return 4;
    }

    @Override
    protected void clientTick() {
        if(portalActiveClient == false && portalActive == true) {
            world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, true));
            if(world.getBlockState(pos.up(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get())
                world.setBlockState(pos.up(2), world.getBlockState(pos.up(2)).with(ACTIVE, true));
            portalActiveClient = true;
        }
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        super.readStorable(nbt);
        portalActive = nbt.getBoolean("portalActive");
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        nbt.putBoolean("portalActive", portalActive);
        return super.writeStorable(nbt);
    }

    private void maintainPortal() {
        if(world.getDimensionKey() == Dimensions.UNDER_NETHER_WORLD)
            return;
        int amt = (int) energyStorage.consume(50);
        if(amt < 50 && activeTicks >= 200)
            deactivatePortal();
        this.activeTicks++;
    }


    public int haveSomeEnergy(int energyRecieved) {
        int ret = energyStorage.receiveEnergy(energyRecieved, false);
        return ret;
    }

    private void activatePortal() {
        //world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
        world.createExplosion(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1.5F, Explosion.Mode.NONE);
        world.setBlockState(pos.up(), ModBlocks.PORTAL_BLOCK.get().getDefaultState());
       // world.setBlockState(pos, getBlockState().with(ACTIVE, true));
        portalActive = true;
        this.activeTicks = 0;
        sync();
        energyStorage.consume(150000);
    }

    private void deactivatePortal() {
        world.setBlockState(pos, this.getBlockState().with(ACTIVE, false));
        world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
        if(world.getBlockState(pos.up(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get())
            world.setBlockState(pos.up(2), world.getBlockState(pos.up(2)).with(ACTIVE, false));
    }

}
