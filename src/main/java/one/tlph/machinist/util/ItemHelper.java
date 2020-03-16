package one.tlph.machinist.util;

import net.minecraft.item.ItemStack;

public class ItemHelper {
    public static ItemStack cloneStack(ItemStack stack, int count) {
        ItemStack ret;
        ret = stack.copy();
        ret.setCount(count);
        return ret;
    }
}
