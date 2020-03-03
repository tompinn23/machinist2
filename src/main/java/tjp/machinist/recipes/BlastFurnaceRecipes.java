package tjp.machinist.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import tjp.machinist.items.ModItems;

import java.util.ArrayList;

public class BlastFurnaceRecipes extends RecipeHandler {

    private static final BlastFurnaceRecipes SMELTING_BASE = new BlastFurnaceRecipes();
    private ArrayList<MachineRecipe> machineRecipes = new ArrayList<>();
    public static BlastFurnaceRecipes instance() { return SMELTING_BASE; }

    public BlastFurnaceRecipes() {
        AddRecipe(new MachineRecipe(new ItemStack(ModItems.steelIngot, 1), new ItemStack[] {new ItemStack(Items.IRON_INGOT, 1)}, 600));
        AddRecipe(new MachineRecipe(new ItemStack(ModItems.steelIngot, 1), new ItemStack[] {new ItemStack(Items.IRON_INGOT, 1), new ItemStack(ModItems.coalDust, 1)}, 400));
    }



    @Override
    public MachineRecipe GetRecipe(ItemStack ingredients) {
        for(MachineRecipe rec : machineRecipes) {
            if(compareItemStacks(ingredients, rec.getIngredients()[0]))
                return rec;
        }
        return null;
    }

    @Override
    public MachineRecipe GetRecipe(ItemStack[] ingredients) {
        for(MachineRecipe rec : machineRecipes) {
            if(compareItemStackArrays(ingredients, rec.getIngredients()))
                return rec;
        }
        return null;
    }

    @Override
    public void AddRecipe(MachineRecipe recipe) {
        machineRecipes.add(recipe);
    }
}
