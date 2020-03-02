package tjp.machinist.recipes;

import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import tjp.machinist.items.ModItems;
import tjp.machinist.Machinist;

import java.util.Map;

public class CrusherRecipes extends IRecipeHandler {

    private static final CrusherRecipes SMELTING_BASE = new CrusherRecipes();
    private static final ItemStack[] EMPTY_RESULT = {ItemStack.EMPTY};
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> crushingList = Maps.<ItemStack, ItemStack>newHashMap();

    public static CrusherRecipes instance() { return SMELTING_BASE; }

    private CrusherRecipes() {
        this.initCrushingRecipes();
    }


    private void initCrushingRecipes() {
        this.addRecipe(new ItemStack(ModItems.ironDust, 2), new ItemStack(Blocks.IRON_ORE));
        this.addRecipe(new ItemStack(ModItems.goldDust, 2), new ItemStack(Blocks.GOLD_ORE));
    }

    @Override
    public boolean hasRecipe(ItemStack stack) {
        return getResult(stack) != EMPTY_RESULT;
    }

    @Override
    public ItemStack[] getResult(ItemStack stack) {
        for(Map.Entry<ItemStack, ItemStack> entry : this.crushingList.entrySet()) {
            if(this.compareItemStacks(entry.getKey(), stack)) {
                return new ItemStack[] {entry.getValue()};
            }
        }
        return EMPTY_RESULT;
    }

    @Override
    public void addRecipe(ItemStack result, ItemStack ingredient) {
        this.crushingList.put(ingredient, result);
    }

    @Override
    public void addRecipe(ItemStack result, ItemStack... Ingredients) {
        Machinist.logger.warn("Invalid recipe registered for crusher.");
    }

}
