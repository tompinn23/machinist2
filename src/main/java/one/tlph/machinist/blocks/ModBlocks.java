package one.tlph.machinist.blocks;

import net.minecraftforge.registries.ObjectHolder;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;

public class ModBlocks {


    @ObjectHolder("machinist:machine_frame")
    public static MachineFrame MACHINE_FRAME;

    @ObjectHolder("machinist:smelter")
    public static Smelter SMELTER;

    @ObjectHolder("machinist:crusher")
    public static Crusher CRUSHER;

    @ObjectHolder("machinist:blast_furnace_controller")
    public static BlastFurnaceController BLAST_FURNACE_CONTROLLER;

    @ObjectHolder("machinist:blast_furnace_casing")
    public static BlastFurnaceCasing BLAST_FURNACE_CASING;



}
