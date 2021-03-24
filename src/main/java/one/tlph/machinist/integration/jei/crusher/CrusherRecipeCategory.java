package one.tlph.machinist.integration.jei.crusher;




import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.blocks.crusher.CrusherTileEntity;
import one.tlph.machinist.init.registries.ModBlocks;
import one.tlph.machinist.integration.jei.MachinistUIDS;
import one.tlph.machinist.recipes.CrusherRecipe;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class CrusherRecipeCategory implements IRecipeCategory<CrusherRecipe> {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int OUTPUT_SLOT_2 = 2;
    public static final ResourceLocation UID = new ResourceLocation(Machinist.MODID, MachinistUIDS.CRUSHER);
    private static final ResourceLocation GUI = new ResourceLocation(Machinist.MODID, "textures/gui/crusher.png");

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;


    public CrusherRecipeCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.CRUSHER.get()));
        background = helper.createDrawable(GUI, 6, 6, 163, 73);
        this.progress = helper.drawableBuilder(GUI, 176, 12, 28, 19)
                .buildAnimated(CrusherTileEntity.COOK_TIME_FOR_COMPLETION, IDrawableAnimated.StartDirection.LEFT, false);
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CrusherRecipe> getRecipeClass() {
        return CrusherRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.machinist.category.crusher");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(CrusherRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        progress.draw(matrixStack, 54, 28);
    }

    @Override
    public void setIngredients(CrusherRecipe crusherRecipe, IIngredients ingredients) {
        List<Ingredient> ingredientList = Lists.newArrayList();
        ingredientList.add(crusherRecipe.getInput());
        ingredients.setInputIngredients(ingredientList);
        ingredientList.clear();
        List<ItemStack> outputs = Lists.newArrayList();
        outputs.add(crusherRecipe.getOutput());
        if(!crusherRecipe.getExtraOutput().isEmpty())
            outputs.add(crusherRecipe.getExtraOutput());
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CrusherRecipe crusherRecipe, IIngredients iIngredients) {
        ItemStack[] inputs = crusherRecipe.getInput().getMatchingStacks();
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
        guiItemStacks.init(INPUT_SLOT, true, 31, 28);
        guiItemStacks.set(INPUT_SLOT, Arrays.asList(inputs));

        ItemStack output = crusherRecipe.getOutput();
        ItemStack extraOutput = crusherRecipe.getExtraOutput();
        guiItemStacks.init(OUTPUT_SLOT, false, 88, 28);

        guiItemStacks.set(OUTPUT_SLOT, output);
        if(!extraOutput.isEmpty()) {
            guiItemStacks.init(OUTPUT_SLOT_2, false, 110, 28);
            guiItemStacks.set(OUTPUT_SLOT_2, extraOutput);
        }
        guiItemStacks.set(iIngredients);
    }
}
