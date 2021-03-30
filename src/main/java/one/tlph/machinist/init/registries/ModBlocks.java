package one.tlph.machinist.init.registries;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.blocks.*;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.EnergyNet.EnergyConduit;
import one.tlph.machinist.blocks.PortalHeater.BlockPortalHeater;
import one.tlph.machinist.blocks.RemeltedNetherrack.RemeltedNetherrack;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;

public final class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Machinist.MODID);
    public static final DeferredRegister<Block> BASIC_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Machinist.MODID);


    public static final RegistryObject<Block> MACHINE_FRAME = BLOCKS.register("machine_frame", () -> new MachineFrame(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> SMELTER = BLOCKS.register("smelter", Smelter::new);
    public static final RegistryObject<Block> CRUSHER = BLOCKS.register("crusher", Crusher::new);
    public static final RegistryObject<Block> BLAST_FURNACE_CONTROLLER = BLOCKS.register("blast_furnace_controller", BlastFurnaceController::new);
    public static final RegistryObject<Block> BLAST_FURNACE_CASING = BLOCKS.register("blast_furnace_casing", BlastFurnaceCasing::new);
    public static final RegistryObject<Block> PORTAL_HEATER = BLOCKS.register("portal_heater", BlockPortalHeater::new);
    public static final RegistryObject<Block> REMELTED_NETHERRACK = BLOCKS.register("remelted_netherrack", RemeltedNetherrack::new);
    public static final RegistryObject<Block> PORTAL_BLOCK = BLOCKS.register("portal_block", PortalBlock::new);


    public static final RegistryObject<Block> ENERGY_CONDUIT = BLOCKS.register("conduit", () -> new EnergyConduit(AbstractBlock.Properties.create(Material.PISTON)));

    // Basic Blocks
    public static final RegistryObject<Block> COPPER_ORE = BASIC_BLOCKS.register("copper_ore", () -> new BasicBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> UNDER_NETHERRACK = BASIC_BLOCKS.register("under_netherrack", () -> new BasicBlock(AbstractBlock.Properties.create(Material.ROCK)));



    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BASIC_BLOCKS.register(modEventBus);
    }
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
