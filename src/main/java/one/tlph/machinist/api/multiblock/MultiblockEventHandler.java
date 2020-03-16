package one.tlph.machinist.api.multiblock;

import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.Machinist;

@Mod.EventBusSubscriber(modid = Machinist.MODID)
public final class MultiblockEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onChunkLoad(final ChunkEvent.Load loadEvent) {

        IChunk chunk = loadEvent.getChunk();

        MultiblockRegistry.INSTANCE.onChunkLoaded(loadEvent.getWorld(), chunk.getPos().x, chunk.getPos().z);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onWorldUnload(final WorldEvent.Unload unloadWorldEvent) {
        MultiblockRegistry.INSTANCE.onWorldUnloaded(unloadWorldEvent.getWorld());
    }

    @SubscribeEvent
    public void onWorldTick(final TickEvent.WorldTickEvent event) {

        if (TickEvent.Phase.START == event.phase)
            MultiblockRegistry.INSTANCE.tickStart(event.world);
    }

    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {

        if (TickEvent.Phase.START == event.phase)
            MultiblockRegistry.INSTANCE.tickStart(Minecraft.getInstance().world);
    }
}
