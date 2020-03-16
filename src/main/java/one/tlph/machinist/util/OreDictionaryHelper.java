package one.tlph.machinist.util;

import com.google.common.primitives.Ints;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictionaryHelper {



    public static int getOreID(String name) {
        if(!oreNameExist(name))
            return -1;
        return OreDictionary.getOreID(name);
    }

    public static boolean oreNameExist(String name) {
        return OreDictionary.doesOreNameExist(name) && OreDictionary.getOres(name, false).size() > 0;
    }

    public static String getOreName(ItemStack stack) {

        int[] ids = OreDictionary.getOreIDs(stack);
        if (ids != null && ids.length >= 1) {
            return OreDictionary.getOreName(ids[0]);
        }
        return "";
    }

    public static String getOreName(int oreID) {
        return OreDictionary.getOreName(oreID);
    }

    public static List<Integer> getAllOreIDs(ItemStack stack) {

        return Ints.asList(OreDictionary.getOreIDs(stack));
    }

    public static ItemStack getOre(String oreName) {
        if(!oreNameExist(oreName))
            return ItemStack.EMPTY;
        ItemStack ore =  OreDictionary.getOres(oreName, false).get(0).copy();
        ore.setCount(1);
        return ore;
    }
}
