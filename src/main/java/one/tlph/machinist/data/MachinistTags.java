package one.tlph.machinist.data;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import one.tlph.machinist.data.datagen.Tags;
import org.lwjgl.system.CallbackI;

public class MachinistTags {

    public static class Blocks {
        public static final ITag.INamedTag<Block> COPPER_ORE = getForgeOreTag("copper");

        private static ITag.INamedTag<Block> getForgeOreTag(String copper) {
            return BlockTags.makeWrapperTag("forge:ores/" + copper);
        }
    }


    public static class Items {

        /*
            TAGS
         */
        public static final ITag.INamedTag<Item> STEEL_INGOT = getForgeIngotTag("steel");
        public static final ITag.INamedTag<Item> COPPER_INGOT = getForgeIngotTag("copper");
        public static final ITag.INamedTag<Item> LEAD_INGOT = getForgeIngotTag("lead");

        public static final ITag.INamedTag<Item> COPPER_DUST = getForgeDustTag("copper");
        public static final ITag.INamedTag<Item> IRON_DUST = getForgeDustTag("iron");
        public static final ITag.INamedTag<Item> GOLD_DUST = getForgeDustTag("gold");
        public static final ITag.INamedTag<Item> STEEL_DUST = getForgeDustTag("steel");
        public static final ITag.INamedTag<Item> COAL_DUST = getForgeDustTag("coal");


        public static final ITag.INamedTag<Item> COPPER_ORE = getForgeOreTag("copper");



    /*
        HELPERS
     */

        public static ITag.INamedTag<Item> getForgeOreTag(String name) {
            return ItemTags.makeWrapperTag("forge:ores/" + name);
        }

        public static ITag.INamedTag<Item> getForgeIngotTag(String name) {
            return ItemTags.makeWrapperTag("forge:ingots/" + name);
        }


        public static ITag.INamedTag<Item> getForgeDustTag(String name) {
            return ItemTags.makeWrapperTag("forge:dusts/" + name);
        }
    }

}
