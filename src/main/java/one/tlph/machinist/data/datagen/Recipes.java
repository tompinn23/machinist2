package one.tlph.machinist.data.datagen;

import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.data.MachinistTags;
import one.tlph.machinist.init.registries.ModBlocks;
import one.tlph.machinist.init.registries.ModItems;
import one.tlph.machinist.recipes.SmeltingRecipe;


import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generator) {
        super(generator);

    }

    @Override
    public String getName() {
        return super.getName() + Machinist.MODID;
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        addCrusherRecipes(consumer);
        addSmeltingRecipes(consumer);
    }

    private void addSmeltingRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.COPPER_ORE.get()), ModItems.COPPER_INGOT.get(), 0.5F, 200).addCriterion("has_copper_ore", hasItem(ModBlocks.COPPER_ORE.get())).build(consumer);
    }

    private void addCrusherRecipes(Consumer<IFinishedRecipe> consumer) {
        for(RegistryObject<Item> registryItem : ModItems.ITEMS.getEntries()) {
            registryItem.ifPresent((Item item) -> {
                if(item.getRegistryName().getPath().contains("dust") && item != ModItems.STEEL_DUST.get()) { // Dust can be crushed.
                    String name = item.getRegistryName().getPath();
                    String mat = name.substring(0, name.indexOf("_"));
                    CrusherRecipeBuilder.crusher(Ingredient.fromTag( ItemTags.createOptional(new ResourceLocation("forge", "ores/" + mat))), new ItemStack(item, 2)).build(consumer, new ResourceLocation(Machinist.MODID, item.getRegistryName().getPath()));
                }
            });
        }
    }
}
