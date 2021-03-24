package one.tlph.machinist.init;


import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.gui.BlastFurnaceGui;
import one.tlph.machinist.gui.CrusherGui;
import one.tlph.machinist.gui.SmelterGui;
import one.tlph.machinist.init.registries.ModBlocks;
import one.tlph.machinist.init.registries.ModContainerTypes;

@Mod.EventBusSubscriber(modid = Machinist.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ScreenManager.registerFactory(ModContainerTypes.SMELTER.get(), SmelterGui::new);
            ScreenManager.registerFactory(ModContainerTypes.CRUSHER.get(), CrusherGui::new);
            ScreenManager.registerFactory(ModContainerTypes.BLAST_FURNACE.get(), BlastFurnaceGui::new);
        });
        RenderTypeLookup.setRenderLayer(ModBlocks.MACHINE_FRAME.get(), RenderType.getCutout());
    }

}
