package one.tlph.machinist.integration.jei.crusher;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.ModBlocks;
import one.tlph.machinist.integration.jei.MachinistUIDS;

import javax.annotation.Nullable;

public class CrusherRecipeCategory<CrusherRecipe extends IRecipeWrapper> implements IRecipeCategory<CrusherRecipe> {

    protected static final int inputSlot = 0;
    protected static final int outputSlot1 = 1;
    protected static final int outputSlot2 = 2;

    private static final ResourceLocation GUI = new ResourceLocation(Machinist.MODID, "textures/gui/crusher.png");


    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public CrusherRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(GUI, 37, 34, 97, 18);
        arrow = guiHelper.drawableBuilder(GUI, 176, 12, 28, 19).buildAnimated(300, IDrawableAnimated.StartDirection.LEFT, false);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.crusher));
        localizedName = new TextComponentTranslation("gui.machinist.category.crusher").getFormattedText();
    }

    @Override
    public String getUid() {
        return MachinistUIDS.CRUSHER;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getModName() {
        return Machinist.MODNAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        arrow.draw(minecraft, 23, 0);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CrusherRecipe crusherRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
        guiItemStacks.init(inputSlot, true, 0, 0);
        guiItemStacks.init(outputSlot1, false, 57, 0);
        guiItemStacks.init(outputSlot2, false, 79, 0);

        guiItemStacks.set(iIngredients);
    }

}
