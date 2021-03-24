package one.tlph.machinist.init;

import net.minecraftforge.fml.DeferredWorkQueue;
import one.tlph.machinist.network.Packets;

public class Setup {

    public void init() {
        DeferredWorkQueue.runLater(() -> {
            //CrusherManager.initalise();
            //BlastFurnaceManager.initialise();
        });
        Packets.register();

    }
}
