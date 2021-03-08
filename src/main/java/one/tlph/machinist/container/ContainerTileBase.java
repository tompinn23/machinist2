package one.tlph.machinist.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import one.tlph.machinist.inventory.IInventoryHolder;
import one.tlph.machinist.tileentity.AbstractTileEntity;

import javax.annotation.Nullable;

public class ContainerTileBase<T extends AbstractTileEntity<?> & IInventoryHolder> extends ContainerBase {
    public final T te;

    public ContainerTileBase(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(type, id, inventory, getTile(inventory.player, buffer.readBlockPos()));
    }

    @SuppressWarnings("unchecked")
    protected static <T extends AbstractTileEntity<?>> T getTile(PlayerEntity player, BlockPos pos) {
        TileEntity tile = player.world.getTileEntity(pos);
        if(tile instanceof AbstractTileEntity<?>) {
            return (T) tile;
        }
        return (T) new AbstractTileEntity<>(TileEntityType.SIGN);
    }

    public ContainerTileBase(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, T te) {
        super(type, id, inventory);
        this.te = te;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            int size = this.te.getInventory().getSlots();
            if (index < size) {
                if (!mergeItemStack(stack1, size, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(stack1, 0, size, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
                slot.onTake(this.player, stack);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }
}
