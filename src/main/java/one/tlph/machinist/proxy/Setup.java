package one.tlph.machinist.proxy;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import one.tlph.machinist.items.ModItems;
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
