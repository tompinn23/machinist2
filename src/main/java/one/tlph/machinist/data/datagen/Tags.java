package one.tlph.machinist.data.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.data.MachinistTags;
import one.tlph.machinist.init.registries.ModItems;
import one.tlph.machinist.init.registries.ModBlocks;


public class Tags extends ItemTagsProvider {

    public Tags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, Machinist.MODID,  existingFileHelper);
    }


    @Override
    protected void registerTags() {
        registerIngots();
        registerOres();
        registerDusts();
    }

    private void registerIngots() {
        this.getOrCreateBuilder(MachinistTags.Items.STEEL_INGOT).add(ModItems.STEEL_INGOT.get());
        this.getOrCreateBuilder(MachinistTags.Items.COPPER_INGOT).add(ModItems.COPPER_INGOT.get());
    }

    private void registerOres() {
        this.getOrCreateBuilder(MachinistTags.Items.COPPER_ORE).add(ModBlocks.COPPER_ORE.get().asItem());

    }

    private void registerDusts() {
        this.getOrCreateBuilder(MachinistTags.Items.COPPER_DUST).add(ModItems.COPPER_DUST.get());
        this.getOrCreateBuilder(MachinistTags.Items.GOLD_DUST).add(ModItems.GOLD_DUST.get());
        this.getOrCreateBuilder(MachinistTags.Items.IRON_DUST).add(ModItems.IRON_DUST.get());
        this.getOrCreateBuilder(MachinistTags.Items.STEEL_DUST).add(ModItems.STEEL_DUST.get());
        this.getOrCreateBuilder(MachinistTags.Items.COAL_DUST).add(ModItems.COAL_DUST.get());

    }


    public static class BlockTags extends BlockTagsProvider {

        public BlockTags(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void registerTags() {
            this.getOrCreateBuilder(MachinistTags.Blocks.COPPER_ORE).add(ModBlocks.COPPER_ORE.get());
        }
    }
}
