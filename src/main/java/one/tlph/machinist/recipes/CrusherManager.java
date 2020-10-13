package one.tlph.machinist.recipes;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.inventory.ComparableItemStack;
import one.tlph.machinist.inventory.ComparableItemStackValidated;
import one.tlph.machinist.inventory.OreValidator;
import one.tlph.machinist.items.ModItems;
import one.tlph.machinist.util.ItemHelper;

import java.util.Locale;
import java.util.Map;

public class CrusherManager {

    private static Map<ComparableItemStack, CrusherRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    private static OreValidator oreValidator = new OreValidator();

    public static final int DEFAULT_ENERGY = 2000;

    static {
        oreValidator.addGroup(new ResourceLocation("forge", ComparableItemStack.ORE));
        oreValidator.addGroup(new ResourceLocation("forge", ComparableItemStack.ORE));
    }

    static int oreMultiplier = 2;

    public static CrusherRecipe getRecipe(ItemStack input) {
        if(input.isEmpty())
            return null;

        ComparableItemStackValidated query = new ComparableItemStackValidated(input, oreValidator);
        CrusherRecipe recipe = recipeMap.get(query);

        if(recipe == null) {
            //query.metadata = OreDictionary.WILDCARD_VALUE;
            query.oreID = query.getOreID(input);
            recipe = recipeMap.get(query);
        }

        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        return getRecipe(input) != null;
    }

    public static CrusherRecipe[] getRecipes() {
        return recipeMap.values().toArray(new CrusherRecipe[0]);
    }

    public static void initalise() {
        addRecipe(200, new ItemStack(Item.getItemFromBlock(Blocks.GOLD_ORE), 1), new ItemStack(ModItems.goldDust, 2));
        addRecipe(200, new ItemStack(Item.getItemFromBlock(Blocks.IRON_ORE), 1), new ItemStack(ModItems.ironDust,2));
        addRecipe(400, new ItemStack(ModItems.steelIngot), new ItemStack(ModItems.steelDust, 1));
        //String oreTy pe;
        //TODO: Fix ore dict
//        for(String oreName : OreDictionary.getOreNames()) {
//            if(oreName.startsWith("ore")) {
//                oreType = oreName.substring(3);
//                addDefaultRecipe(oreType);
//            }
//        }
    } 

    private static void addDefaultRecipe(String oreType) {
//        if(oreType == null || oreType.isEmpty()) {
//            return;
//        }
//
//        String suffix = oreType.substring(0,1).toUpperCase(Locale.US) + oreType.substring(1);
//
//        String oreName = "ore" + suffix;
//        String gemName = "gem" + suffix;
//        String dustName = "dust" + suffix;
//        String ingotName = "ingot" + suffix;
//
//        ItemStack ore = OreDictionaryHelper.getOre(oreName);
//        ItemStack gem = OreDictionaryHelper.getOre(gemName);
//        ItemStack dust = OreDictionaryHelper.getOre(dustName);
//        ItemStack ingot = OreDictionaryHelper.getOre(ingotName);
//
//
//        if(!gem.isEmpty()) {
//            addRecipe(DEFAULT_ENERGY, ore, ItemHelper.cloneStack(gem, 2));
//            if(!dust.isEmpty())
//                addRecipe(DEFAULT_ENERGY / 2, gem, ItemHelper.cloneStack(dust, 1));
//        } else {
//            if(!dust.isEmpty())
//                addRecipe(DEFAULT_ENERGY, ore, ItemHelper.cloneStack(dust, 2));
//                addRecipe(DEFAULT_ENERGY / 2, ingot, ItemHelper.cloneStack(dust, 1));
//        }
//


    }


    public static CrusherRecipe addRecipe(int energy, ItemStack input, ItemStack output) {
        if(input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }
        CrusherRecipe recipe = new CrusherRecipe(input, output, energy);
        recipeMap.put(new ComparableItemStackValidated(input, oreValidator), recipe);
        return recipe;
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
