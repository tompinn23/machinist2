package tjp.machinist.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MachineRecipe {
    private ItemStack[] results = null;
    private ItemStack[] ingredients = null;

    private int time = 0;

    public MachineRecipe(ItemStack result, ItemStack[] ingredients, int processingTime) {
        this.results = new ItemStack[] {result};
        this.ingredients = ingredients;
        this.time = processingTime;
    }

    public MachineRecipe(ItemStack[] results, ItemStack[] ingredients, int processingTime) {
        this.results = results;
        this.ingredients = ingredients;
        this.time = processingTime;
    }

    public boolean isIngredient(Item item) {
        for(int i = 0; i < ingredients.length; i++) {
            if(ingredients[i].getItem() == item)
                return true;
        }
        return false;
    }

    public int getIngredientAmt(Item item) {
        for(int i = 0; i < ingredients.length; i++) {
            if(ingredients[i].getItem() == item)
                return ingredients[i].getCount();
        }
        return 0;
    }


    public int getProcessingTime() { return time; }

    public ItemStack getResult() {
        if(results != null)
            return results[0];
        return ItemStack.EMPTY;
    }

    public ItemStack[] getResults() {
        if(results == null) {
            return new ItemStack[] {ItemStack.EMPTY};
        }
        return results;
    }

    public ItemStack[] getIngredients() {
        if(ingredients == null) {
            return new ItemStack[] {ItemStack.EMPTY};
        }
        return ingredients;
    }
}
