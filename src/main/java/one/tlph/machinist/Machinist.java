package one.tlph.machinist;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import one.tlph.machinist.api.multiblock.IMultiblockRegistry;
import one.tlph.machinist.api.multiblock.MultiblockEventHandler;
import one.tlph.machinist.api.multiblock.MultiblockRegistry;
import one.tlph.machinist.client.Client;
import one.tlph.machinist.client.ClientHandler;
import one.tlph.machinist.energy.net.EnergyNetEventHandler;
import one.tlph.machinist.energy.net.EnergyNetRegistry;
import one.tlph.machinist.energy.net.IEnergyNetRegistry;
import one.tlph.machinist.init.*;
import one.tlph.machinist.init.registries.*;
import one.tlph.machinist.network.Network;

import one.tlph.machinist.world.Features;
import one.tlph.machinist.world.UnderNetherHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

import static one.tlph.machinist.init.ModFlags.GEN_COPPER;
import static one.tlph.machinist.init.ModFlags.setFlag;

@Mod(Machinist.MODID)
public class Machinist {
    public static final String MODID = "machinist";
    public static final String MODNAME = "Machinist";
    public static final String MODVERSION = "0.1.0";

    public static final Network NETWORK = new Network(MODID);
    public static Setup setup = new Setup();

    public static boolean ic2Loaded = false;
    
    public static final Logger logger = LogManager.getLogger();
    public static boolean debug = true;

    public static MultiblockEventHandler s_multiblockHandler;
    public static EnergyNetEventHandler energyNetEventHandler;
    public static UnderNetherHandler underNetherHandler;



    public Machinist() {
        setFeatureFlags();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        PointOfInterest.POI.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);

        loadListeners();
        
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        Config.register();
    }

    private void setFeatureFlags() {
        setFlag(GEN_COPPER, true);
    }

    private void loadListeners() {
        Client mod = getClient();
        if(mod != null) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(mod::client);
            FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<FMLClientSetupEvent>) evt -> evt.enqueueWork(() -> mod.syncClient(evt)));
        }
        MinecraftForge.EVENT_BUS.register(new ClientHandler());
        MinecraftForge.EVENT_BUS.register(underNetherHandler = new UnderNetherHandler());
    }

    public Client getClient() {
        return Client.INSTANCE;
    }


    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        event.enqueueWork(Features::setup);
    }


    public static IEnergyNetRegistry initEnergyNetRegistry() {
        if(energyNetEventHandler == null) {
            MinecraftForge.EVENT_BUS.register(energyNetEventHandler = new EnergyNetEventHandler());
        }
        return EnergyNetRegistry.INSTANCE;
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

//
//    	ic2Loaded = Loader.isModLoaded("ic2");
//        proxy.postInit(e);
//    }





}
