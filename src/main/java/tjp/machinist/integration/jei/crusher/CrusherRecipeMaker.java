package tjp.machinist.integration.jei.crusher;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import tjp.machinist.recipes.CrusherManager;

import java.util.ArrayList;
import java.util.List;

public class CrusherRecipeMaker {
    private CrusherRecipeMaker() {}

    public static List<CrusherRecipe> getCrusherRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        CrusherManager.CrusherRecipe[] crusherRecipes = CrusherManager.getRecipes();

        List<CrusherRecipe> recipes = new ArrayList<>();

       for(int i = 0; i < crusherRecipes.length; i++) {
           CrusherManager.CrusherRecipe recipe = crusherRecipes[i];
           List<ItemStack> inputs = stackHelper.getSubtypes(recipe.getInput());
           CrusherRecipe cRecipe = new CrusherRecipe(inputs, recipe.getOutput());
           recipes.add(cRecipe);
       }
       return recipes;
    }
}
