package tjp.machinist.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tjp.machinist.items.ModItems;

public class RecipeHandler {

    public static void initSmelting() {
        GameRegistry.addSmelting(new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.steelIngot), 0.5f);
        GameRegistry.addSmelting(new ItemStack(ModItems.ironDust), new ItemStack(Items.IRON_INGOT), 0.8f);
        GameRegistry.addSmelting(new ItemStack(ModItems.goldDust), new ItemStack(Items.GOLD_INGOT), 0.8f);
    }

}
