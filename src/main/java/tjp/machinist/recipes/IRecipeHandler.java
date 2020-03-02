package tjp.machinist.recipes;

import net.minecraft.item.ItemStack;

public abstract class IRecipeHandler {
    public abstract boolean hasRecipe(ItemStack stack);
    public abstract ItemStack[] getResult(ItemStack stack);
    public abstract void addRecipe(ItemStack result, ItemStack ingredient);
    public abstract void addRecipe(ItemStack result, ItemStack... Ingredients);

    protected boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
}
