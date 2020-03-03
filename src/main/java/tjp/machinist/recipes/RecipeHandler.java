package tjp.machinist.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tjp.machinist.items.ModItems;

public abstract class RecipeHandler {

    public abstract MachineRecipe GetRecipe(ItemStack result);
    public abstract MachineRecipe GetRecipe(ItemStack[] results);

    public abstract void AddRecipe(MachineRecipe recipe);


    public boolean HasRecipe(ItemStack result) {
        return GetRecipe(result) != null;
    }

    public boolean HasRecipe(ItemStack[] results) {
        return GetRecipe(results) != null;
    }

    public static void initCustomSmelting() {
        GameRegistry.addSmelting(new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.steelIngot), 0.5f);
        GameRegistry.addSmelting(new ItemStack(ModItems.ironDust), new ItemStack(Items.IRON_INGOT), 0.8f);
        GameRegistry.addSmelting(new ItemStack(ModItems.goldDust), new ItemStack(Items.GOLD_INGOT), 0.8f);
    }

    protected static boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    protected static boolean compareItemStackArrays(ItemStack[] stacks1, ItemStack[] stacks2) {
        if(stacks1 == stacks2)
            return true;
        if(stacks1 == null || stacks2 == null)
            return false;
        if(stacks1.length != stacks2.length)
            return false;

        for(int i = 0; i < stacks1.length; i++) {
            if(!compareItemStacks(stacks1[i], stacks2[i]))
                return false;
        }
        return true;
    }

}
