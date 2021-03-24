package one.tlph.machinist.data.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.init.registries.ModBlocks;

import java.util.function.Supplier;


public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Machinist.MODID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for(RegistryObject<Block> block : ModBlocks.BASIC_BLOCKS.getEntries()) {
            oreBlock(block);
        }
        oreBlock(ModBlocks.REMELTED_NETHERRACK);
    }

    private void oreBlock(Supplier<Block> blockSupplier) {
        simpleBlock(blockSupplier.get(), cubeAll(blockSupplier.get()));
        simpleBlockItem(blockSupplier.get(), cubeAll(blockSupplier.get()));
    }

}
