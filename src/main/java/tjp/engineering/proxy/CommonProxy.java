package tjp.engineering.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tjp.engineeering.items.Coupler;
import tjp.engineering.Engineering;
import tjp.engineering.ModBlocks;
import tjp.engineering.blocks.MachineFrame;
import tjp.engineering.blocks.smelter.Smelter;
import tjp.engineering.blocks.smelter.SmelterTileEntity;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {

    }

    public void init(FMLInitializationEvent e) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(Engineering.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(new MachineFrame(),
                                        new Smelter());
        GameRegistry.registerTileEntity(SmelterTileEntity.class, new ResourceLocation(Engineering.MODID + "_smelter"));

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBlock(ModBlocks.machineFrame).setRegistryName(ModBlocks.machineFrame.getRegistryName()),
                                        new ItemBlock(ModBlocks.smelter).setRegistryName(ModBlocks.smelter.getRegistryName()));
        
        event.getRegistry().registerAll(new Coupler());
    }
}
