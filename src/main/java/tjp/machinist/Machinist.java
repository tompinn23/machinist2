package tjp.machinist;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import tjp.machinist.proxy.CommonProxy;
import tjp.machinist.recipes.BlastFurnaceManager;
import tjp.machinist.recipes.CrusherManager;


@Mod(modid = Machinist.MODID, name = Machinist.MODNAME, version = Machinist.MODVERSION, dependencies = "required-after:forge@[14.23.5.2768,)", useMetadata = true)
public class Machinist {
    public static final String MODID = "machinist";
    public static final String MODNAME = "Machinist";
    public static final String MODVERSION = "0.0.1";

    @SidedProxy(clientSide = "tjp.machinist.proxy.ClientProxy", serverSide = "tjp.machinist.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Machinist instance;

    public static boolean ic2Loaded = false;
    
    public static Logger logger;
    public static boolean debug = true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.Init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        initManagers();


    	ic2Loaded = Loader.isModLoaded("ic2");
        proxy.postInit(e);
    }

    private void initManagers() {
        CrusherManager.initalise();
        BlastFurnaceManager.initialise();
    }


}
