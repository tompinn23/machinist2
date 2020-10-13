package one.tlph.machinist.init;


import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.gui.BlastFurnaceGui;
import one.tlph.machinist.gui.CrusherGui;
import one.tlph.machinist.gui.SmelterGui;
import sun.awt.X11.Screen;

@Mod.EventBusSubscriber(modid = Machinist.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ScreenManager.registerFactory(ModContainerTypes.SMELTER.get(), SmelterGui::new);
            ScreenManager.registerFactory(ModContainerTypes.CRUSHER.get(), CrusherGui::new);
            ScreenManager.registerFactory(ModContainerTypes.BLAST_FURNACE.get(), BlastFurnaceGui::new);
        });
    }

}
