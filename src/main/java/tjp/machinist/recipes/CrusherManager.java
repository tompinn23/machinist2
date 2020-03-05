package tjp.machinist.recipes;

import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import tjp.machinist.blocks.crusher.Crusher;
import tjp.machinist.inventory.ComparableItemStack;
import tjp.machinist.inventory.ComparableItemStackValidated;
import tjp.machinist.inventory.OreValidator;
import tjp.machinist.util.OreDictionaryHelper;

import java.security.PublicKey;
import java.util.Map;

public class CrusherManager {

    private static Map<ComparableItemStack, CrusherRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    private static OreValidator oreValidator = new OreValidator();

    static {
        oreValidator.addPrefix(ComparableItemStack.ORE);
        oreValidator.addPrefix(ComparableItemStack.INGOT);
    }

    static int oreMultiplier = 2;

    public static CrusherRecipe getRecipe(ItemStack input) {
        if(input.isEmpty())
            return null;

        ComparableItemStackValidated query = new ComparableItemStackValidated(input, oreValidator);
        CrusherRecipe recipe = recipeMap.get(query);

        if(recipe == null) {
            query.metadata = OreDictionary.WILDCARD_VALUE;
            recipe = recipeMap.get(query);
        }

        return recipe;
    }

    public static  boolean recipeExists(ItemStack input) {
        return getRecipe(input) != null;
    }

    public static CrusherRecipe[] getRecipes() {
        return recipeMap.values().toArray(new CrusherRecipe[0]);
    }

    public static void initalise() {
        addRecipe(200, new ItemStack(Item.getItemFromBlock(Blocks.GOLD_ORE), 1), new ItemStack())
    }



    public static CrusherRecipe addRecipe(int energy, ItemStack input, ItemStack output) {
        if(input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }
        CrusherRecipe recipe = new CrusherRecipe(input, output, energy);
    }

    public static class CrusherRecipe {
        final ItemStack input;
        final ItemStack output;
        final int energy;

        CrusherRecipe(ItemStack input, ItemStack output, int energy) {
            this.input = input;
            this.output = output;
            this.energy = energy;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getOutput() {
            return output;
        }

        public  int getEnergy() {
            return  energy;
        }

    }

}
