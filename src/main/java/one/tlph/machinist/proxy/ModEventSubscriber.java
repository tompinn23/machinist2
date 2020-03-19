package one.tlph.machinist.proxy;

import com.google.common.eventbus.Subscribe;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.MachineFrame;
import one.tlph.machinist.blocks.ModBlocks;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.items.BasicItem;
import one.tlph.machinist.items.Coupler;
import one.tlph.machinist.items.ModItems;
import one.tlph.machinist.tileentity.BlastFurnaceCasingTileEntity;
import one.tlph.machinist.tileentity.BlastFurnaceControllerTileEntity;
import one.tlph.machinist.tileentity.CrusherTileEntity;
import one.tlph.machinist.tileentity.SmelterTileEntity;

import javax.rmi.CORBA.Tie;
import java.util.Objects;

@EventBusSubscriber(modid = Machinist.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        Item.Properties properties = new Item.Properties().group(ModiItemGroup.MOD_ITEM_GROUP);
        event.getRegistry().registerAll(setup(new BlockItem(ModBlocks.MACHINE_FRAME, properties), "machine_frame"),
                setup(new BlockItem(ModBlocks.SMELTER, properties), "smelter"),
                setup(new BlockItem(ModBlocks.CRUSHER, properties), "crusher"),
                setup(new BlockItem(ModBlocks.BLAST_FURNACE_CASING, properties), "blast_casing"),
                setup(new BlockItem(ModBlocks.BLAST_FURNACE_CONTROLLER, properties), "blast_controller"));

        event.getRegistry().registerAll(
        		setup(new BasicItem(properties), "iron_dust"),
        		setup(new BasicItem(properties), "gold_dust"),
				setup(new BasicItem(properties), "steel_dust"),
				setup(new BasicItem(properties), "steel_ingot"),
				setup(new BasicItem(properties), "coal_dust"),
				setup(new BasicItem(properties), "copper_dust"),
				setup(new BasicItem(properties), "copper_ingot")
        		);
        
        event.getRegistry().register(setup(new Coupler(properties), "coupler"));
    }

    @SubscribeEvent
    public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(setup(new MachineFrame(), "machine_frame"),
                setup(new Smelter(), "smelter"),
                setup(new Crusher(), "crusher"),
                setup(new BlastFurnaceCasing(), "blast_furnace_casing"),
                setup(new BlastFurnaceController(), "blast_furnace_controller")
        );
    }

    @SubscribeEvent
    public static void onRegisterTileEntityType(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(setup(TileEntityType.Builder.create(SmelterTileEntity::new, ModBlocks.SMELTER).build(null), "smelter"));
        event.getRegistry().register(setup(TileEntityType.Builder.create(CrusherTileEntity::new, ModBlocks.CRUSHER).build(null), "crusher"));
        event.getRegistry().register(setup(TileEntityType.Builder.create(BlastFurnaceCasingTileEntity::new, ModBlocks.BLAST_FURNACE_CASING).build(null), "blast_furnace_casing"));
        event.getRegistry().register(setup(TileEntityType.Builder.create(BlastFurnaceControllerTileEntity::new, ModBlocks.BLAST_FURNACE_CONTROLLER).build(null), "blast_furnace_controller"));
    }

    
    
    @SubscribeEvent
    public static void onRegisterModels(RegistryEvent.Register<ContainerType<?>> event) {
    	event.getRegistry().registerAll(
    			setup(IForgeContainerType.create(CrusherContainer::new), "crusher"),
    			setup(IForgeContainerType.create(SmelterContainer::new), "smelter"));
    }

    
    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
    }


    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
    	return setup(entry, new ResourceLocation(Machinist.MODID, name));
    }

    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
    	entry.setRegistryName(registryName);
    	return entry;
    }
    
}
