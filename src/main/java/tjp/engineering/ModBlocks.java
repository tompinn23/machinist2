package tjp.engineering;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tjp.engineering.blocks.MachineFrame;
import tjp.engineering.blocks.smelter.Smelter;

public class ModBlocks {

    @GameRegistry.ObjectHolder("engineering:machineframe")
    public static MachineFrame machineFrame;

    @GameRegistry.ObjectHolder("engineering:smelter")
    public static Smelter smelter;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        machineFrame.initModel();
        smelter.initModel();

    }
}
