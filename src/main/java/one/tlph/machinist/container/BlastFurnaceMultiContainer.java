package one.tlph.machinist.container;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.proxy.ModContainerTypes;
import one.tlph.machinist.tileentity.BlastFurnaceMultiBlockTileEntity;
import one.tlph.machinist.tileentity.CrusherTileEntity;
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
    
    
    private BlastFurnaceMultiBlockTileEntity te;
	private IWorldPosCallable canInteractWithCallable;

    
    public BlastFurnaceMultiContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    public BlastFurnaceMultiContainer(final int windowId, final PlayerInventory playerInventory, final BlastFurnaceMultiBlockTileEntity tileEntity) {
        super(ModContainerTypes.BLAST_FURNACE, windowId);
        this.te = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.WORLD, tileEntity.getReferenceCoord());
        this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntity.cookTime, v -> tileEntity.cookTime = (short) v));

        this.addPlayerSlots(playerInventory);
    }

    private static BlastFurnaceMultiBlockTileEntity getTileEntity(PlayerInventory playerInventory, PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileEntity = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if(tileEntity instanceof IMultiblockPart)
            return (BlastFurnaceMultiBlockTileEntity)((IMultiblockPart)tileEntity).getMultiblockController();
        throw new IllegalStateException("Tile entity is not correct! " + tileEntity);
    }




    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
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
    public boolean canInteractWith(PlayerEntity playerIn) {
        return te.canInteractWith(playerIn);
    }
}
