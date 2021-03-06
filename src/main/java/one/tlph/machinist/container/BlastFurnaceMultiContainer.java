package one.tlph.machinist.container;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.init.registries.ModContainerTypes;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceMultiBlockTileEntity;
import one.tlph.machinist.util.OutputSlotHandler;

import java.util.Objects;

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
    
    public BlastFurnaceMultiBlockTileEntity tileEntity;

    public BlastFurnaceMultiContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private static BlastFurnaceMultiBlockTileEntity getTileEntity(PlayerInventory playerInventory, PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileEntity = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if(tileEntity instanceof IMultiblockPart)
            return (BlastFurnaceMultiBlockTileEntity)((IMultiblockPart)tileEntity).getMultiblockController();
        throw new IllegalStateException("Tile entity is not correct! " + tileEntity);
    }



    public BlastFurnaceMultiContainer(final int windowId, final PlayerInventory playerInventory, final BlastFurnaceMultiBlockTileEntity tileEntity) {
        super(ModContainerTypes.BLAST_FURNACE.get(), windowId, playerInventory);
        this.tileEntity = tileEntity;
        this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntity.cookTime, v -> tileEntity.cookTime = (short) v));

        IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().get();

        this.addPlayerSlots(playerInventory);
        this.addSlot(new BlastFurnaceFuelSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.FUEL_SLOT, 43, 56));
        this.addSlot(new BlastFurnaceInputSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.INPUT_SLOT_1, 32, 17));
        this.addSlot(new BlastFurnaceInputSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.INPUT_SLOT_2, 53, 17));
        this.addSlot(new OutputSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.OUTPUT_SLOT, 116, 35));

    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            int size = BlastFurnaceMultiBlockTileEntity.SIZE;
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

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}
