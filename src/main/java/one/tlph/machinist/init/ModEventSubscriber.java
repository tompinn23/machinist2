package one.tlph.machinist.init;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.MachineFrame;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.items.BasicItem;
import one.tlph.machinist.items.Coupler;
import one.tlph.machinist.tileentity.BlastFurnaceCasingTileEntity;
import one.tlph.machinist.tileentity.BlastFurnaceControllerTileEntity;
import one.tlph.machinist.tileentity.CrusherTileEntity;
import one.tlph.machinist.tileentity.SmelterTileEntity;

@EventBusSubscriber(modid = Machinist.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(block -> {
                    final Item.Properties properties = new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP);
                    final BlockItem blockItem = new BlockItem(block, properties);
                    blockItem.setRegistryName(block.getRegistryName());
                    registry.register(blockItem);
                });
    }
/*
    @SubscribeEvent
    public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(setup(new MachineFrame(), "machine_frame"),
                setup(new Smelter(), "smelter"),
                setup(new Crusher(), "crusher"),
                setup(new BlastFurnaceCasing(), "blast_furnace_casing"),
                setup(new BlastFurnaceController(), "blast_furnace_controller")
        );
    }
*/


    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
    	return setup(entry, new ResourceLocation(Machinist.MODID, name));
    }

    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
    	entry.setRegistryName(registryName);
    	return entry;
    }
    
}
