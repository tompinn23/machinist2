package tjp.machinist.recipes;

import net.minecraft.item.ItemStack;

public interface IRecipeHandler {
    boolean hasRecipe(ItemStack stack);
    ItemStack[] getResult(ItemStack stack);
    void addRecipe(ItemStack result, ItemStack ingredient);
    void addRecipe(ItemStack result, ItemStack... Ingredients);
}
