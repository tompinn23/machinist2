package tjp.machinist.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import tjp.machinist.integration.jei.crusher.CrusherRecipeCategory;
import tjp.machinist.integration.jei.crusher.CrusherRecipeMaker;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registry.addRecipeCategories(new CrusherRecipeCategory(guiHelper));
    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        registry.addRecipes(CrusherRecipeMaker.getCrusherRecipes(jeiHelpers), MachinistUIDS.CRUSHER);
    }
}
