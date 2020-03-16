package one.tlph.machinist;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ObjectHolder;
import one.tlph.machinist.blocks.MachineFrame;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;

public class ModBlocks {


    @ObjectHolder("machinist:machineframe")
    public static MachineFrame machineFrame;

    @ObjectHolder("machinist:smelter")
    public static Smelter smelter;

    @ObjectHolder("machinist:crusher")
    public static Crusher crusher;

    @ObjectHolder("machinist:blastfurnacecontroller")
    public static BlastFurnaceController blastController;

    @ObjectHolder("machinist:blastfurnacecasing")
    public static BlastFurnaceCasing blastCasing;


    public static void initModels() {
        machineFrame.initModel();
        smelter.initModel();
        crusher.initModel();
        blastCasing.initModel();
        blastController.initModel();

    }
}
