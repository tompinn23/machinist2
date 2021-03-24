package one.tlph.machinist.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static one.tlph.machinist.init.ModFlags.*;


public class Config {
    private static boolean registered = false;

    public static void register() {
        if(registered)
            return;

        String path = createConfigDir("machinist");

        FMLJavaModLoadingContext.get().getModEventBus().register(Config.class);
        registered = true;

        genServerConfig();
        genClientConfig();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, serverSpec, path + "/machinist.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec);
    }

    public static String createConfigDir(String path) {
        try {
            Path configDir = Paths.get(FMLPaths.CONFIGDIR.get()
                    .toAbsolutePath().toString(), path);
            Files.createDirectories(configDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    private Config() {}


    private static final ForgeConfigSpec.Builder SERVER_CONFIG = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec serverSpec;

    private static final ForgeConfigSpec.Builder CLIENT_CONFIG = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec clientSpec;



    private static void genClientConfig() {
        clientSpec = CLIENT_CONFIG.build();
        refreshClientConfig();
    }



    private static void genServerConfig() {
        genWorldConfig();
        serverSpec = SERVER_CONFIG.build();
        refreshServerConfig();
    }


    private static void genWorldConfig() {
        SERVER_CONFIG.push("World Generation");

        flagGenCopper = SERVER_CONFIG.comment("Set to FALSE to prevent generation of Copper Ore")
                .define("Copper", true);

        SERVER_CONFIG.pop();
    }

    private static void refreshClientConfig() {
    }

    private static void refreshServerConfig() {
        refreshWorldConfig();
    }

    private static void refreshWorldConfig() {
        setFlag(ModFlags.GEN_COPPER, flagGenCopper.get());
    }


    private static BooleanValue flagGenCopper;


    @SubscribeEvent
    public static void configLoading(final ModConfig.Loading evt) {
        switch (evt.getConfig().getType()) {
            case CLIENT:
                refreshClientConfig();
                break;
            case SERVER:
                refreshServerConfig();
        }
    }

    @SubscribeEvent
    public static void configReloading(ModConfig.Reloading event) {

        switch (event.getConfig().getType()) {
            case CLIENT:
                refreshClientConfig();
                break;
            case SERVER:
                refreshServerConfig();
        }
    }




}
