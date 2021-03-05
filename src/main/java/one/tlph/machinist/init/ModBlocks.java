package one.tlph.machinist.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.EnergyNet.EnergyConduit;
import one.tlph.machinist.blocks.MachineFrame;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;

public final class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Machinist.MODID);


    public static final RegistryObject<Block> MACHINE_FRAME = BLOCKS.register("machine_frame", () -> new MachineFrame(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> SMELTER = BLOCKS.register("smelter", () -> new Smelter());
    public static final RegistryObject<Block> CRUSHER = BLOCKS.register("crusher", () -> new Crusher());
    public static final RegistryObject<Block> BLAST_FURNACE_CONTROLLER = BLOCKS.register("blast_furnace_controller", () -> new BlastFurnaceController());
    public static final RegistryObject<Block> BLAST_FURNACE_CASING = BLOCKS.register("blast_furnace_casing", () -> new BlastFurnaceCasing());

    public static final RegistryObject<Block> ENERGY_CONDUIT = BLOCKS.register("conduit", () -> new EnergyConduit(AbstractBlock.Properties.create(Material.PISTON)));
/*


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

*/

}
