package one.tlph.machinist.inventory;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inventory extends ItemStackHandler {

    @Nullable
    private IInventoryHolder tile;

    public Inventory(int size) {
        this(size, null);
    }

    public Inventory(NonNullList<ItemStack> stacks) {
        this(stacks, null);
    }

    public Inventory(NonNullList<ItemStack> stacks, @Nullable IInventoryHolder tile) {
        super(stacks);
        this.tile = tile;
    }

    Inventory(int size, @Nullable IInventoryHolder tile) {
        super(size);
        this.tile = tile;
    }


    public static Inventory create(int size) {
        return new Inventory(size, null);
    }

    public static Inventory createBlank() {
        return new Inventory(0, null);
    }

    public void setTile(@Nullable IInventoryHolder tile) {
        this.tile = tile;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return isBlank() ? new CompoundNBT() : super.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (isBlank())
            return;
        nbt.putInt("Size", getSlots());
        super.deserializeNBT(nbt);
    }

    public Inventory set(int size) {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        onContentsChanged(0);
        return this;
    }

    public Inventory add(int size) {
        this.stacks = NonNullList.withSize(size + this.stacks.size(), ItemStack.EMPTY);
        return this;
    }

    public NonNullList<ItemStack> canPut(NonNullList<ItemStack> outputs, int... slots) {
        return canPut(outputs, fromSlotArray(slots));
    }

    public NonNullList<ItemStack> canPut(NonNullList<ItemStack> outputs, NonNullList<ItemStack> slots) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        slots.forEach(stack -> stacks.add(stack.copy()));
        Inventory inv = new Inventory(stacks);
        for (ItemStack stack : outputs) {
            if (!ItemHandlerHelper.insertItem(inv, stack.copy(), false).isEmpty()) {
                return NonNullList.create();
            }
        }
        return inv.getStacks();
    }

    public NonNullList<ItemStack> fromSlotArray(int... slots) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i : slots) {
            stacks.add(getStackInSlot(i));
        }
        return stacks;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (this.tile != null) {
            return this.tile.getSlotLimit(slot);
        }
        return super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (this.tile != null) {
            return this.tile.canInsert(slot, stack);
        }
        return super.isItemValid(slot, stack);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return canExtract(slot, getStackInSlot(slot)) ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    public ItemStack extractItemFromSlot(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public boolean canExtract(int slot, ItemStack stack) {
        if (this.tile != null) {
            return this.tile.canExtract(slot, stack);
        }
        return true;
    }

    public boolean canInsert(int slot, ItemStack stack) {
        if(this.tile != null) {
            return this.tile.canInsert(slot, stack);
        }
        return true;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.tile != null) {
            this.tile.onSlotChanged(slot);
        }
    }

    public ItemStack getFirst() {
        return getStackInSlot(0);
    }

    public ItemStack getLast() {
        return getStackInSlot(getLastSlot());
    }

    public int getLastSlot() {
        return getSlots() - 1;
    }

    public NonNullList<ItemStack> getLast(int count) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        int size = this.stacks.size();
        for (int i = size - count; i < count; i++) {
            stacks.add(this.stacks.get(i));
        }
        return stacks;
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull() {
        for (ItemStack stack : this.stacks) {
            if (stack.getCount() < stack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEmptySlot() {
        for (ItemStack stack : this.stacks) {
            if (stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSlotEmpty(int slot) {
        return this.stacks.get(slot).isEmpty();
    }

    public ItemStack setSlotEmpty(int slot) {
        ItemStack stack = this.stacks.set(slot, ItemStack.EMPTY);
        onContentsChanged(slot);
        return stack;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        ItemStack stack1 = this.stacks.set(slot, stack);
        onContentsChanged(slot);
    }

    public void clear() {
        set(getSlots());
    }

    public boolean isBlank() {
        return this.stacks.size() <= 0;
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

    public List<ItemStack> getNonEmptyStacks() {
        List<ItemStack> stacks = new ArrayList<>(this.stacks);
        stacks.removeIf(ItemStack::isEmpty);
        return stacks;
    }

    public ItemStack addNext(ItemStack stack) {
        for (int i = 0; i < getSlots(); ++i) {
            if (isItemValid(i, stack)) {
                insertItem(i, stack.copy(), false);
                return stack.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeNext() {
        for (int i = getSlots() - 1; i >= 0; --i) {
            ItemStack stack = setSlotEmpty(i);
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack insertItem(ItemStack stack, boolean simulate, int... ex) {
        if (stack.isEmpty())
            return stack;
        for (int i = 0; i < getSlots(); i++) {
            int value = i;
            if (Arrays.stream(ex).anyMatch(j -> j == value)) continue;
            stack = insertItem(i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    public void drop(World world, BlockPos pos) {
        this.stacks.forEach(stack -> {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        });
        clear();
    }

    public void drop(int index, World world, BlockPos pos) {
        ItemStack stack = getStackInSlot(index);
        if (!stack.isEmpty()) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            setStackInSlot(index, ItemStack.EMPTY);
        }
    }

    public static Inventory from(IItemHandler handler) {
        Inventory inventory = new Inventory(handler.getSlots()) {
            @Override
            public ItemStack getStackInSlot(int slot) {
                return handler.getStackInSlot(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return handler.insertItem(slot, stack, simulate);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return handler.extractItem(slot, amount, true);
            }

            @Override
            public int getSlotLimit(int slot) {
                return handler.getSlotLimit(slot);
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return handler.isItemValid(slot, stack);
            }
        };
        return inventory;
    }

    public static NonNullList<ItemStack> toList(IItemHandler handler) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < handler.getSlots(); i++) {
            stacks.set(i, handler.getStackInSlot(i));
        }
        return stacks;
    }

    public static LazyOptional<IItemHandler> get(World world, BlockPos pos, Direction side) {
        TileEntity te = world.getTileEntity(pos);
        return te != null ? te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) : LazyOptional.empty();
    }


}
