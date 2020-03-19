package one.tlph.machinist.proxy;


import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.gui.CrusherGui;
import one.tlph.machinist.gui.SmelterGui;

@Mod.EventBusSubscriber(modid = Machinist.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerTypes.SMELTER, SmelterGui::new);
        ScreenManager.registerFactory(ModContainerTypes.CRUSHER, CrusherGui::new);

    }

}
