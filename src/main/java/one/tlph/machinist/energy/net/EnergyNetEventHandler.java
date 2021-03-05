package one.tlph.machinist.energy.net;

import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.api.multiblock.MultiblockRegistry;

@Mod.EventBusSubscriber(modid = Machinist.MODID)
public final class EnergyNetEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onChunkLoad(final ChunkEvent.Load loadEvent) {

        IChunk chunk = loadEvent.getChunk();

        EnergyNetRegistry.INSTANCE.onChunkLoaded(loadEvent.getWorld(), chunk.getPos().x, chunk.getPos().z);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onWorldUnload(final WorldEvent.Unload unloadWorldEvent) {
        EnergyNetRegistry.INSTANCE.onWorldUnloaded(unloadWorldEvent.getWorld());
    }

    @SubscribeEvent
    public void onWorldTick(final TickEvent.WorldTickEvent event) {

        if (TickEvent.Phase.START == event.phase)
            EnergyNetRegistry.INSTANCE.tickStart(event.world);
    }

    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.START == event.phase)
            EnergyNetRegistry.INSTANCE.tickStart(Minecraft.getInstance().world);
    }
}
