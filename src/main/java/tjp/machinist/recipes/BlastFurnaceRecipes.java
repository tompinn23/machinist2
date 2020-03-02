package tjp.machinist.recipes;

import com.google.common.collect.Maps;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import tjp.machinist.items.ModItems;

import java.util.Map;

public class BlastFurnaceRecipes extends IRecipeHandler {

    private static final BlastFurnaceRecipes SMELTING_BASE = new BlastFurnaceRecipes();
    private static final ItemStack[] EMPTY_RESULT = {ItemStack.EMPTY};
    /** The list of smelting results. */
    private final Map<ItemStack, BlastFurnaceRecipe> crushingList = Maps.newHashMap();

    public static BlastFurnaceRecipes instance() { return SMELTING_BASE; }

    public BlastFurnaceRecipes() {
        addRecipe(true, new ItemStack(ModItems.steelIngot, 1), new ItemStack(Items.IRON_INGOT, 1));
    }


    @Override
    public boolean hasRecipe(ItemStack stack) {
        return getResult(stack) != EMPTY_RESULT;
    }

    @Override
    public ItemStack[] getResult(ItemStack stack) {
        for(Map.Entry<ItemStack, BlastFurnaceRecipe> entry : this.crushingList.entrySet()) {
            if(this.compareItemStacks(entry.getKey(), stack)) {
                return new ItemStack[] {entry.getValue().getResult()};
            }
        }
        return EMPTY_RESULT;
    }

    public BlastFurnaceRecipe getRecipe(ItemStack stack) {
        return crushingList.get(stack);
    }

    @Override
    public void addRecipe(ItemStack result, ItemStack ingredient) {
        crushingList.put(ingredient, new BlastFurnaceRecipe(result, result));
    }

    @Override
    public void addRecipe(ItemStack result, ItemStack... Ingredients) {
        crushingList.put(Ingredients[0], new BlastFurnaceRecipe(result, Ingredients));
    }

    public void addRecipe(boolean carbon, ItemStack result, ItemStack... Ingredients) {
        crushingList.put(Ingredients[0], new BlastFurnaceRecipe(result, Ingredients).needsCarbon(carbon));
    }

    public void addRecipe(boolean carbon, ItemStack result, ItemStack ingredient){
        crushingList.put(ingredient, new BlastFurnaceRecipe(result, ingredient).needsCarbon(carbon));
    }
}
