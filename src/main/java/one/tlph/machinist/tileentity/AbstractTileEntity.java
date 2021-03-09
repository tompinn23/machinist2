package one.tlph.machinist.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import one.tlph.machinist.blocks.AbstractBlock;
import one.tlph.machinist.inventory.IInventoryHolder;
import one.tlph.machinist.inventory.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class AbstractTileEntity<B extends AbstractBlock> extends TileEntity {

    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

    protected final Inventory inv = Inventory.createBlank();
    private final LazyOptional<Inventory> invHolder = LazyOptional.of(() -> this.inv);

    public AbstractTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        if (this instanceof IInventoryHolder) {
            this.inv.setTile((IInventoryHolder) this);
        }
    }

    public B getBlock() {
        return (B) getBlockState().getBlock();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        readSync(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        return writeSync(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 3, writeSync(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        readSync(pkt.getNbtCompound());
    }

    protected void readSync(CompoundNBT nbt) {
        if(this instanceof IInventoryHolder && !keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
        readStorable(nbt);
    }

    public void readStorable(CompoundNBT nbt) {
        if(this instanceof IInventoryHolder && keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
    }

    protected CompoundNBT writeSync(CompoundNBT nbt) {
        if(this instanceof IInventoryHolder && !keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }

        return writeStorable(nbt);
    }

    public CompoundNBT writeStorable(CompoundNBT nbt) {
        if(this instanceof IInventoryHolder && keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }
        return nbt;
    }

    public boolean keepStorable() {
        return true;
    }

    protected boolean keepInventory() {
        return false;
    }

    protected boolean isRemote() {
        return world.isRemote();
    }

    public void sync() {
        if (this.world instanceof ServerWorld) {
            final BlockState state = getBlockState();
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
            this.world.markChunkDirty(this.pos, this);
        }
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ITEM_HANDLER_CAPABILITY && this instanceof IInventoryHolder && !this.inv.isBlank()) {
            return invHolder.cast();
        }
        return super.getCapability(cap, side);
    }
}
