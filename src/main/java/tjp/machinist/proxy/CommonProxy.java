package tjp.machinist.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import scala.tools.nsc.transform.patmat.Logic;
import tjp.machinist.Machinist;
import tjp.machinist.blocks.crusher.Crusher;
import tjp.machinist.blocks.crusher.CrusherTileEntity;
import tjp.machinist.items.*;
import tjp.machinist.ModBlocks;
import tjp.machinist.blocks.MachineFrame;
import tjp.machinist.blocks.smelter.Smelter;
import tjp.machinist.blocks.smelter.SmelterTileEntity;
import tjp.machinist.recipes.RecipeHandler;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {

    }

    public void init(FMLInitializationEvent e) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(Machinist.instance, new GuiProxy());
    	RecipeHandler.initSmelting();
        // OreDict
		OreDictionary.registerOre("dustCoal", ModItems.coalDust);
        OreDictionary.registerOre("dustIron", ModItems.ironDust);
        OreDictionary.registerOre("dustGold",ModItems.goldDust);
        OreDictionary.registerOre("ingotSteel", ModItems.steelIngot);
    }

    public void postInit(FMLPostInitializationEvent e) {

    }



    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(new MachineFrame(),
                                        new Smelter(),
                                        new Crusher());
        GameRegistry.registerTileEntity(SmelterTileEntity.class, new ResourceLocation(Machinist.MODID + "_smelter"));
        GameRegistry.registerTileEntity(CrusherTileEntity.class, new ResourceLocation(Machinist.MODID + "_crusher"));

    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBlock(ModBlocks.machineFrame).setRegistryName(ModBlocks.machineFrame.getRegistryName()),
                                        new ItemBlock(ModBlocks.smelter).setRegistryName(ModBlocks.smelter.getRegistryName()),
                                        new ItemBlock(ModBlocks.crusher).setRegistryName(ModBlocks.crusher.getRegistryName()));


        event.getRegistry().register(new Coupler());
        event.getRegistry().register(new SteelIngot());
        event.getRegistry().register(new GoldDust());
        event.getRegistry().register(new IronDust());
        BasicItem.InitBasicItems(event);



    }
}
