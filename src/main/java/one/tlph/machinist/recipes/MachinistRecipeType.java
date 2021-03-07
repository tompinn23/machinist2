package one.tlph.machinist.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.IForgeRegistry;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.inventory.IgnoredIInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MachinistRecipeType<T extends MachinistRecipe> implements IRecipeType<T> {


    private static final List<MachinistRecipeType<? extends MachinistRecipe>> types = new ArrayList<>();

    public static final MachinistRecipeType<BlastFurnaceRecipe> BLAST_FURNACE = create("blast_furnace");
    public static final MachinistRecipeType<SmeltingRecipe> SMELTING = create("smelting");
    public static final MachinistRecipeType<CrusherRecipe> CRUSHER = create("crusher");



    private static <T extends MachinistRecipe> MachinistRecipeType create(String name) {
        MachinistRecipeType<T> type = new MachinistRecipeType<>(name);
        types.add(type);
        return type;
    }

    //TODO: Convert this to using the proper forge registry once we stop needing to directly use the vanilla registry as a work around
    public static void registerRecipeTypes(IForgeRegistry<IRecipeSerializer<?>> registry) {
        types.forEach(type -> Registry.register(Registry.RECIPE_TYPE, type.registryName, type));
    }

    public static void clearCache() {
        //TODO: Does this need to also get cleared on disconnect
        types.forEach(type -> type.cachedRecipes.clear());
    }

    private List<T> cachedRecipes = Collections.emptyList();
    private final ResourceLocation registryName;

    private MachinistRecipeType(String name) {
        this.registryName = new ResourceLocation(Machinist.MODID, name);
    }

    @Override
    public String toString() {
        return registryName.toString();
    }

    @Nonnull
    public List<T> getRecipes(@Nullable World world) {
        if (world == null) {
            //Try to get a fallback world if we are in a context that may not have one
            world = ServerLifecycleHooks.getCurrentServer().func_241755_D_();
            if (world == null) {
                //If we failed, then return no recipes
                return Collections.emptyList();
            }
        }
        if (cachedRecipes.isEmpty()) {
            RecipeManager recipeManager = world.getRecipeManager();
            //TODO: Should we use the getRecipes(RecipeType) that we ATd so that our recipes don't have to always return true for matching?
            List<T> recipes = recipeManager.getRecipes(this, IgnoredIInventory.INSTANCE, world);
            if (this == SMELTING) {
                Map<ResourceLocation, IRecipe<IInventory>> smeltingRecipes = recipeManager.getRecipes(IRecipeType.SMELTING);
                //Copy recipes our recipes to make sure it is mutable
                recipes = new ArrayList<>(recipes);
                for (Map.Entry<ResourceLocation, IRecipe<IInventory>> entry : smeltingRecipes.entrySet()) {
                    IRecipe<IInventory> smeltingRecipe = entry.getValue();
                    //TODO: Allow for specifying not copying all smelting recipes, maybe do it by the resource location
                    ItemStack recipeOutput = smeltingRecipe.getRecipeOutput();
                    if (!smeltingRecipe.isDynamic() && !recipeOutput.isEmpty()) {
                        //TODO: Can Smelting recipes even "dynamic", if so can we add some sort of checker to make getOutput return the correct result
                        NonNullList<Ingredient> ingredients = smeltingRecipe.getIngredients();
                        ItemStackIngredient input;
                        if (ingredients.isEmpty()) {
                            //Something went wrong
                            continue;
                        } else if (ingredients.size() == 1) {
                            input = ItemStackIngredient.from(ingredients.get(0));
                        } else {
                            ItemStackIngredient[] itemIngredients = new ItemStackIngredient[ingredients.size()];
                            for (int i = 0; i < ingredients.size(); i++) {
                                itemIngredients[i] = ItemStackIngredient.from(ingredients.get(i));
                            }
                            input = ItemStackIngredient.createMulti(itemIngredients);
                        }
                        recipes.add((T) new SmeltingRecipe(entry.getKey(), input, recipeOutput));
                    }
                }
            }
            cachedRecipes = recipes;
        }
        return cachedRecipes;
    }

    public Stream<T> stream(@Nullable World world) {
        return getRecipes(world).stream();
    }

    @Nullable
    public T findFirst(@Nullable World world, Predicate<T> matchCriteria) {
        return stream(world).filter(matchCriteria).findFirst().orElse(null);
    }

    public boolean contains(@Nullable World world, Predicate<T> matchCriteria) {
        return stream(world).anyMatch(matchCriteria);
    }
}
