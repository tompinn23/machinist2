package tjp.engineering.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tjp.engineering.ModBlocks;

public class RecipeHandler {

    public static void initSmelting() {
        GameRegistry.addSmelting(new ItemStack(Items.IRON_INGOT), new ItemStack());
    }

}
