package one.tlph.machinist.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.common.registry.GameRegistry;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.ModBlocks;
import one.tlph.machinist.api.multiblock.IMultiblockRegistry;
import one.tlph.machinist.api.multiblock.MultiblockEventHandler;
import one.tlph.machinist.api.multiblock.MultiblockRegistry;
import one.tlph.machinist.blocks.MachineFrame;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;
import one.tlph.machinist.items.BasicItem;
import one.tlph.machinist.items.Coupler;
import one.tlph.machinist.tileentity.BlastFurnaceCasingTileEntity;
import one.tlph.machinist.tileentity.BlastFurnaceControllerTileEntity;
import one.tlph.machinist.tileentity.CrusherTileEntity;
import one.tlph.machinist.tileentity.SmelterTileEntity;

@Mod.EventBusSubscriber
public class CommonProxy {

    private static MultiblockEventHandler s_multiblockHandler = null;

//    public void preInit(FMLPreInitializationEvent e) {
//
//    }

//    public void Init(FMLInitializationEvent e) {
//    	NetworkRegistry.INSTANCE.registerGuiHandler(Machinist.instance, new GuiProxy());
//        // OreDict
//		OreDictionary.registerOre("dustCoal", ModItems.coalDust);
//        OreDictionary.registerOre("dustIron", ModItems.ironDust);
//        OreDictionary.registerOre("dustGold",ModItems.goldDust);
//        OreDictionary.registerOre("ingotSteel", ModItems.steelIngot);
//    }
//
//    public void postInit(FMLPostInitializationEvent e) {
//
//    }




//    @SubscribeEvent
//    public static void registerBlocks(RegistryEvent.Register<Block> event) {
//        event.getRegistry().registerAll(new MachineFrame(),
//                                        new Smelter(),
//                                        new Crusher(),
//                                        new BlastFurnaceCasing(),
//                                        new BlastFurnaceController()
//                                        );
//        GameRegistry.registerTileEntity(SmelterTileEntity.class, new ResourceLocation(Machinist.MODID + "_smelter"));
//        GameRegistry.registerTileEntity(CrusherTileEntity.class, new ResourceLocation(Machinist.MODID + "_crusher"));
//        GameRegistry.registerTileEntity(BlastFurnaceControllerTileEntity.class, new ResourceLocation(Machinist.MODID + "_blastfurnacecontroller"));
//        GameRegistry.registerTileEntity(BlastFurnaceCasingTileEntity.class, new ResourceLocation(Machinist.MODID + "_blastfurnacecasing"));
//
//    }
//
//
//    @SubscribeEvent
//    public static void registerItems(RegistryEvent.Register<Item> event) {
//        event.getRegistry().registerAll(new ItemBlock(ModBlocks.machineFrame).setRegistryName(ModBlocks.machineFrame.getRegistryName()),
//                                        new ItemBlock(ModBlocks.smelter).setRegistryName(ModBlocks.smelter.getRegistryName()),
//                                        new ItemBlock(ModBlocks.crusher).setRegistryName(ModBlocks.crusher.getRegistryName()),
//                                        new ItemBlock(ModBlocks.blastCasing).setRegistryName(ModBlocks.blastCasing.getRegistryName()),
//                                        new ItemBlock(ModBlocks.blastController).setRegistryName(ModBlocks.blastController.getRegistryName()));
//
//
//        event.getRegistry().register(new Coupler());
//        BasicItem.InitBasicItems(event);
//
//
//
//    }
}
