package tjp.engineering;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import tjp.engineering.proxy.CommonProxy;


@Mod(modid = Engineering.MODID, name = Engineering.MODNAME, version = Engineering.MODVERSION, dependencies = "required-after:forge@[14.23.5.2768,)", useMetadata = true)
public class Engineering {
    public static final String MODID = "engineering";
    public static final String MODNAME = "Engineering";
    public static final String MODVERSION = "0.0.1";

    @SidedProxy(clientSide = "tjp.engineering.proxy.ClientProxy", serverSide = "tjp.engineering.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Engineering instance;

    public static boolean ic2Loaded = false;
    
    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	ic2Loaded = Loader.isModLoaded("ic2");
        proxy.postInit(e);
    }


}
