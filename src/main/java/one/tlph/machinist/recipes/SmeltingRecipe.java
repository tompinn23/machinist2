package one.tlph.machinist.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.blocks.MachineFrame;
import one.tlph.machinist.init.ModBlocks;

public class SmeltingRecipe extends ItemStackToItemStackRecipe {

    public SmeltingRecipe(ResourceLocation id, ItemStackIngredient input, ItemStack output) {
        super(id, input, output);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return MachinistRecipeSerializers.SMELTING.getRecipeSerializer();
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
