package one.tlph.machinist.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import one.tlph.machinist.client.renderer.CableRenderer;
import one.tlph.machinist.client.renderer.PortalHeater;
import one.tlph.machinist.init.registries.ModTileEntityTypes;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public enum Client {
    INSTANCE;

    /**
     * Setting up the client in parallel.
     * Only for code that is safe to run in parallel.
     * e.g. uses a {@link ConcurrentHashMap}.
     *
     * @see Client#syncClient(FMLClientSetupEvent)
     */
    public void client(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.CABLE_TILE_ENTITY.get(), CableRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.PORTAL_HEATER.get(), PortalHeater::new);

    }

    /**
     * Setting up the client on the main thread.
     * For code that is unsafe to run in parallel.
     * e.g. uses a {@link HashMap}.
     */
    public void syncClient(FMLClientSetupEvent event) {

    }
}
