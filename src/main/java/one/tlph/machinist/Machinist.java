package one.tlph.machinist;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import one.tlph.machinist.api.multiblock.IMultiblockRegistry;
import one.tlph.machinist.api.multiblock.MultiblockEventHandler;
import one.tlph.machinist.api.multiblock.MultiblockRegistry;
import one.tlph.machinist.proxy.CommonProxy;
import one.tlph.machinist.proxy.Setup;
import one.tlph.machinist.recipes.BlastFurnaceManager;
import one.tlph.machinist.recipes.CrusherManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Machinist.MODID)
public class Machinist {
    public static final String MODID = "machinist";
    public static final String MODNAME = "Machinist";
    public static final String MODVERSION = "0.1.0";

    public static Setup setup = new Setup();

    public static boolean ic2Loaded = false;
    
    public static final Logger logger = LogManager.getLogger();
    public static boolean debug = true;

    public static MultiblockEventHandler s_multiblockHandler;



    public Machinist() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
    }

    public static IMultiblockRegistry initMultiblockRegistry() {
        if(null == s_multiblockHandler)
            MinecraftForge.EVENT_BUS.register(s_multiblockHandler = new MultiblockEventHandler());
        return MultiblockRegistry.INSTANCE;
    }


//    @Mod.EventHandler
//    public void preInit(FMLPreInitializationEvent event) {
//        logger = event.getModLog();
//        proxy.preInit(event);
//    }
//
//    @Mod.EventHandler
//    public void init(FMLInitializationEvent e) {
//        proxy.Init(e);
//    }
//
//    @Mod.EventHandler
//    public void postInit(FMLPostInitializationEvent e) {
//        initManagers();
//
//
//    	ic2Loaded = Loader.isModLoaded("ic2");
//        proxy.postInit(e);
//    }

    private void initManagers() {
        CrusherManager.initalise();
        BlastFurnaceManager.initialise();
    }


}