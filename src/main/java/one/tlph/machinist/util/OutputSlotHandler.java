package one.tlph.machinist.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OutputSlotHandler extends SlotItemHandler {
    public OutputSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        //return SmelterTileEntity.isItemValidOutput(stack);
        // NO!
        return false;
    }
}