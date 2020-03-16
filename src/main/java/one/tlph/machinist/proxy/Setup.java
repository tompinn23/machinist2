package one.tlph.machinist.proxy;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import one.tlph.machinist.items.ModItems;

public class Setup {

    public ItemGroup itemGroup = new ItemGroup("machinist") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.coupler);
        }
    };

    public void init() {
    }


}
