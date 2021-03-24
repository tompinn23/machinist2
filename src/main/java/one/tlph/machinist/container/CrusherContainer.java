package one.tlph.machinist.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import one.tlph.machinist.init.registries.ModContainerTypes;
import one.tlph.machinist.blocks.crusher.CrusherTileEntity;
import one.tlph.machinist.util.OutputSlotHandler;

import javax.annotation.Nonnull;

public class CrusherContainer extends ContainerTileBase<CrusherTileEntity> {

    public CrusherContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTile(playerInventory.player, data.readBlockPos()));
    }

    public CrusherContainer(final int windowId, final PlayerInventory playerInventory, CrusherTileEntity te) {
        super(ModContainerTypes.CRUSHER.get(), windowId, playerInventory, te);
        this.addPlayerSlots(playerInventory);

        this.addSlot(new CrushableSlotHandler(te.getInventory(), 0, 38, 35));
        this.addSlot(new OutputSlotHandler(te.getInventory(), 1, 95, 35));
        this.addSlot(new OutputSlotHandler(te.getInventory(), 2, 117, 35));
    }


//    @Override
//    public boolean canInteractWith(PlayerEntity playerIn) {
//        return playerIn.world.getBlockState(tileEntity.getPos()).getBlock() == ModBlocks.CRUSHER.get().getBlock()
//                && playerIn.getDistanceSq(tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5) <= 64.0;
//    }

    private class CrushableSlotHandler extends SlotItemHandler {
        public CrushableSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return te.isItemValidInput(stack);
        }

    }
}
