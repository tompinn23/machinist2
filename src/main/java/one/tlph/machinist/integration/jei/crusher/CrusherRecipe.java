package one.tlph.machinist.integration.jei.crusher;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class CrusherRecipe implements IRecipeWrapper {

    private final List<List<ItemStack>> input;
    private final ItemStack output;

    public CrusherRecipe(List<ItemStack> input, ItemStack output) {
        this.input = Collections.singletonList(input);
        this.output = output;
    }



    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, input);
        iIngredients.setOutput(VanillaTypes.ITEM, output);
    }

}
