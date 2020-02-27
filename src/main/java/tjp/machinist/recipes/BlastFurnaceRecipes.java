package tjp.machinist.recipes;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class BlastFurnaceRecipes implements IRecipeHandler {
    private static final BlastFurnaceRecipes SMELTING_BASE = new BlastFurnaceRecipes();
    private static final ItemStack[] EMPTY_RESULT = {ItemStack.EMPTY};
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> crushingList = Maps.<ItemStack, ItemStack>newHashMap();

    public static BlastFurnaceRecipes instance() { return SMELTING_BASE; }

    @Override
    public boolean hasRecipe(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack[] getResult(ItemStack stack) {
        return new ItemStack[0];
    }

    @Override
    public void addRecipe(ItemStack result, ItemStack ingredient) {

    }

    @Override
    public void addRecipe(ItemStack result, ItemStack... Ingredients) {

    }
}
