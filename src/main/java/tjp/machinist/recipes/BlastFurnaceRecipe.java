package tjp.machinist.recipes;

import net.minecraft.item.ItemStack;

public class BlastFurnaceRecipe {
    public static final tjp.machinist.recipes.BlastFurnaceRecipe NO_RECIPE = new tjp.machinist.recipes.BlastFurnaceRecipe(ItemStack.EMPTY, null);

        private ItemStack result = ItemStack.EMPTY;
        private ItemStack[] ingredients = null;
        private boolean needsCarbon = false;

        public BlastFurnaceRecipe(ItemStack result, ItemStack... ingredients) {
            result = result;

        }


        public tjp.machinist.recipes.BlastFurnaceRecipe needsCarbon(boolean carbon) {
            needsCarbon = carbon;
            return this;
        }

        public ItemStack getResult() { return result; }
        public ItemStack[] getIngredients() { return ingredients; }
        public boolean needCarbon() { return needsCarbon; }
}
