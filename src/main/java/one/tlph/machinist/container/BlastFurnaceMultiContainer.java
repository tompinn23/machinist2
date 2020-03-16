package one.tlph.machinist.container;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import one.tlph.machinist.tileentity.BlastFurnaceMultiBlockTileEntity;
import one.tlph.machinist.util.OutputSlotHandler;

import javax.annotation.Nonnull;

public class BlastFurnaceMultiContainer extends ContainerBase {



    private class BlastFurnaceInputSlotHandler extends SlotItemHandler {

        public BlastFurnaceInputSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            //TODO: logic.
            return BlastFurnaceMultiBlockTileEntity.isValidInput(stack);
        }
    }
    private class BlastFurnaceFuelSlotHandler extends SlotItemHandler {

        public BlastFurnaceFuelSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            //TODO: logic.
            return BlastFurnaceMultiBlockTileEntity.isValidFuel(stack);
        }
    }


    private BlastFurnaceMultiBlockTileEntity te;

    public BlastFurnaceMultiContainer(IInventory playerInventory, BlastFurnaceMultiBlockTileEntity te) {
        this.te = te;

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    @Override
    protected void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new BlastFurnaceInputSlotHandler(itemHandler, 0, 32, 17));
        addSlotToContainer(new BlastFurnaceInputSlotHandler(itemHandler, 1, 53, 17));
        addSlotToContainer(new BlastFurnaceFuelSlotHandler(itemHandler, 2, 43, 56));
        addSlotToContainer(new OutputSlotHandler(itemHandler, 3, 116, 35));
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(index < BlastFurnaceMultiBlockTileEntity.SIZE) {
                if(!this.mergeItemStack(itemstack1, BlastFurnaceMultiBlockTileEntity.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, BlastFurnaceMultiBlockTileEntity.SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if(itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}
