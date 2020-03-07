package tjp.machinist.recipes;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import tjp.machinist.inventory.ComparableItemStack;
import tjp.machinist.inventory.ComparableItemStackValidated;
import tjp.machinist.inventory.OreValidator;
import tjp.machinist.items.ModItems;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class BlastFurnaceManager {

    private static Map<List<ComparableItemStackValidated>, BlastFurnaceRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    private static OreValidator oreValidator = new OreValidator();


    static {
        oreValidator.addPrefix(ComparableItemStack.BLOCK);
        oreValidator.addPrefix(ComparableItemStack.INGOT);
        oreValidator.addPrefix(ComparableItemStack.DUST);
    }


    public static final int DEFAULT_COOKTIME = 400;

    public static BlastFurnaceRecipe getRecipe(ItemStack primaryInput, ItemStack secondaryInput) {
        if(primaryInput.isEmpty())
            return null;
        ComparableItemStackValidated query = new ComparableItemStackValidated(primaryInput, oreValidator);
        ComparableItemStackValidated secondQuery = new ComparableItemStackValidated(secondaryInput, oreValidator);
        BlastFurnaceRecipe recipe = recipeMap.get(asList(query, secondQuery));
        if(recipe == null) {
            recipe = recipeMap.get(asList(secondQuery, query));
        }
        if(recipe == null) {
            query.metadata = OreDictionary.WILDCARD_VALUE;
            secondQuery.metadata = OreDictionary.WILDCARD_VALUE;
            recipe = recipeMap.get(asList(query, secondQuery));
            if(recipe == null) {
                recipe = recipeMap.get(asList(secondQuery, query));
            }
        }
        return recipe;
    }

    public static boolean recipeExists(ItemStack primaryInput, ItemStack secondInput) {
        return getRecipe(primaryInput, secondInput) != null;
    }

    public static void initialise() {
        addRecipe(new ItemStack(Items.IRON_INGOT), ItemStack.EMPTY, new ItemStack(ModItems.steelIngot), 600);
    }

    public static BlastFurnaceRecipe addRecipe(ItemStack primary, ItemStack secondary, ItemStack output, int cookTime) {
        if(recipeExists(primary, secondary))
            return null;
        if(primary.isEmpty())
            return null;
        BlastFurnaceRecipe recipe = new BlastFurnaceRecipe(primary, secondary, output, cookTime);
        recipeMap.put(asList(new ComparableItemStackValidated(primary, oreValidator), new ComparableItemStackValidated(secondary, oreValidator)), recipe);
        return recipe;
    }




    public static class BlastFurnaceRecipe {
        final ItemStack firstInput;
        final ItemStack secondInput;
        final ItemStack output;
        final int cookTime;



        public BlastFurnaceRecipe(ItemStack firstInput, ItemStack secondInput, ItemStack output, int cookTime) {
            this.firstInput = firstInput;
            this.secondInput = secondInput;
            this.output = output;
            this.cookTime = cookTime;
        }

        public ItemStack getFirstInput() {
            return  firstInput;
        }

        public ItemStack getSecondInput() {
            return secondInput;
        }

        public ItemStack getOutput() {
            return output;
        }

        public int getCookTime() {
            return cookTime;
        }



        public boolean checkIngredients(ItemStack stackInSlot, ItemStack stackInSlot1) {

            if(reversed(stackInSlot)) {
                return stackInSlot.getCount() > secondInput.getCount() && stackInSlot1.getCount() > firstInput.getCount();
            }
            return stackInSlot.getCount() >= firstInput.getCount() && stackInSlot1.getCount() >= secondInput.getCount();
        }

        public boolean reversed(ItemStack firstInput) {
            return new ComparableItemStackValidated(firstInput, oreValidator) == new ComparableItemStackValidated(secondInput, oreValidator);
        }
    }
}
