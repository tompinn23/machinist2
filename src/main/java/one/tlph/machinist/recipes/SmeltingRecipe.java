package one.tlph.machinist.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.init.registries.ModBlocks;

public class SmeltingRecipe extends ItemStackToItemStackRecipe {

    public SmeltingRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        super(id, input, output);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return MachinistRecipeType.SMELTING;
    }

    @Override
    public String getGroup() {
        return ModBlocks.SMELTER.get().getTranslationKey();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.SMELTER.get());
    }
}
