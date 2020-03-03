package tjp.machinist.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import tjp.machinist.Machinist;
import tjp.machinist.ModBlocks;
import tjp.machinist.api.multiblock.IMultiblockRegistry;
import tjp.machinist.api.multiblock.MultiblockEventHandler;
import tjp.machinist.api.multiblock.MultiblockRegistry;
import tjp.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import tjp.machinist.blocks.BlastFurnace.BlastFurnaceController;
import tjp.machinist.blocks.MachineFrame;
import tjp.machinist.blocks.crusher.Crusher;
import tjp.machinist.blocks.smelter.Smelter;
import tjp.machinist.items.*;
import tjp.machinist.recipes.RecipeHandler;
import tjp.machinist.tileentity.BlastFurnaceCasingTileEntity;
import tjp.machinist.tileentity.BlastFurnaceControllerTileEntity;
import tjp.machinist.tileentity.CrusherTileEntity;
import tjp.machinist.tileentity.SmelterTileEntity;

@Mod.EventBusSubscriber
public class CommonProxy {

    private static MultiblockEventHandler s_multiblockHandler = null;

    public void preInit(FMLPreInitializationEvent e) {

    }

    public void Init(FMLInitializationEvent e) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(Machinist.instance, new GuiProxy());
    	RecipeHandler.initCustomSmelting();
        // OreDict
		OreDictionary.registerOre("dustCoal", ModItems.coalDust);
        OreDictionary.registerOre("dustIron", ModItems.ironDust);
        OreDictionary.registerOre("dustGold",ModItems.goldDust);
        OreDictionary.registerOre("ingotSteel", ModItems.steelIngot);
    }

    public void postInit(FMLPostInitializationEvent e) {

    }

    public IMultiblockRegistry initMultiblockRegistry() {
        if(null == s_multiblockHandler)
            MinecraftForge.EVENT_BUS.register(s_multiblockHandler = new MultiblockEventHandler());
        return MultiblockRegistry.INSTANCE;
    }


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(new MachineFrame(),
                                        new Smelter(),
                                        new Crusher(),
                                        new BlastFurnaceCasing(),
                                        new BlastFurnaceController()
                                        );
        GameRegistry.registerTileEntity(SmelterTileEntity.class, new ResourceLocation(Machinist.MODID + "_smelter"));
        GameRegistry.registerTileEntity(CrusherTileEntity.class, new ResourceLocation(Machinist.MODID + "_crusher"));
        GameRegistry.registerTileEntity(BlastFurnaceControllerTileEntity.class, new ResourceLocation(Machinist.MODID + "_blastfurnacecontroller"));
        GameRegistry.registerTileEntity(BlastFurnaceCasingTileEntity.class, new ResourceLocation(Machinist.MODID + "_blastfurnacecasing"));

    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBlock(ModBlocks.machineFrame).setRegistryName(ModBlocks.machineFrame.getRegistryName()),
                                        new ItemBlock(ModBlocks.smelter).setRegistryName(ModBlocks.smelter.getRegistryName()),
                                        new ItemBlock(ModBlocks.crusher).setRegistryName(ModBlocks.crusher.getRegistryName()),
                                        new ItemBlock(ModBlocks.blastCasing).setRegistryName(ModBlocks.blastCasing.getRegistryName()),
                                        new ItemBlock(ModBlocks.blastController).setRegistryName(ModBlocks.blastController.getRegistryName()));


        event.getRegistry().register(new Coupler());
        event.getRegistry().register(new SteelIngot());
        event.getRegistry().register(new GoldDust());
        event.getRegistry().register(new IronDust());
        BasicItem.InitBasicItems(event);



    }
}
