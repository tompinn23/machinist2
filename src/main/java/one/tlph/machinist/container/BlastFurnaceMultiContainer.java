package one.tlph.machinist.container;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.init.ModContainerTypes;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceMultiBlockTileEntity;
import one.tlph.machinist.util.OutputSlotHandler;

import java.util.Objects;

import javax.annotation.Nonnull;

public class BlastFurnaceMultiContainer extends ContainerTileBase<BlastFurnaceMultiBlockTileEntity> {



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
    
    
	private IWorldPosCallable canInteractWithCallable;

    
    public BlastFurnaceMultiContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    public BlastFurnaceMultiContainer(final int windowId, final PlayerInventory playerInventory, final BlastFurnaceMultiBlockTileEntity tileEntity) {
        super(ModContainerTypes.BLAST_FURNACE.get(), windowId, );
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.WORLD, tileEntity.getReferenceCoord());
        this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntity.cookTime, v -> tileEntity.cookTime = (short) v));

        IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().get();

        this.addPlayerSlots(playerInventory);
        this.addSlot(new BlastFurnaceFuelSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.FUEL_SLOT, 43, 56));
        this.addSlot(new BlastFurnaceInputSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.INPUT_SLOT_1, 32, 17));
        this.addSlot(new BlastFurnaceInputSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.INPUT_SLOT_2, 53, 17));
        this.addSlot(new OutputSlotHandler(handler, BlastFurnaceMultiBlockTileEntity.OUTPUT_SLOT, 116, 35));


    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}
