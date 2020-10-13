package one.tlph.machinist.init;

import net.minecraftforge.fml.DeferredWorkQueue;
import one.tlph.machinist.recipes.BlastFurnaceManager;
import one.tlph.machinist.recipes.CrusherManager;

public class Setup {

    public void init() {
        DeferredWorkQueue.runLater(() -> {
            CrusherManager.initalise();
            BlastFurnaceManager.initialise();
        });

    }
}
