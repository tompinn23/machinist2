package one.tlph.machinist.init.registries;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasingTileEntity;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceControllerTileEntity;
import one.tlph.machinist.blocks.EnergyNet.EnergyConduitTileEntity;
import one.tlph.machinist.blocks.PortalHeater.PortalHeaterTileEntity;
import one.tlph.machinist.blocks.RemeltedNetherrack.PortalRemeltedTileEntity;
import one.tlph.machinist.blocks.crusher.CrusherTileEntity;
import one.tlph.machinist.blocks.smelter.SmelterTileEntity;
import one.tlph.machinist.container.FunctionalIntReferenceHolder;

public class ModTileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Machinist.MODID);

    public static final RegistryObject<TileEntityType<SmelterTileEntity>> SMELTER = TILE_ENTITY_TYPES.register("smelter", () ->
            TileEntityType.Builder.create(SmelterTileEntity::new, ModBlocks.SMELTER.get()).build(null));

    public static final RegistryObject<TileEntityType<CrusherTileEntity>> CRUSHER = TILE_ENTITY_TYPES.register("crusher", () ->
            TileEntityType.Builder.create(CrusherTileEntity::new, ModBlocks.CRUSHER.get()).build(null));

    public static final RegistryObject<TileEntityType<BlastFurnaceCasingTileEntity>> BLAST_FURNACE_CASING = TILE_ENTITY_TYPES.register("blast_furnace_casing", () ->
            TileEntityType.Builder.create(BlastFurnaceCasingTileEntity::new, ModBlocks.BLAST_FURNACE_CASING.get()).build(null));

    public static final RegistryObject<TileEntityType<BlastFurnaceControllerTileEntity>> BLAST_FURNACE_CONTROLLER = TILE_ENTITY_TYPES.register("blast_furnace_controller", () ->
            TileEntityType.Builder.create(BlastFurnaceControllerTileEntity::new, ModBlocks.BLAST_FURNACE_CONTROLLER.get()).build(null));

    public static final RegistryObject<TileEntityType<EnergyConduitTileEntity>> CABLE_TILE_ENTITY = TILE_ENTITY_TYPES.register("cable", () ->
            TileEntityType.Builder.create(EnergyConduitTileEntity::new, ModBlocks.ENERGY_CONDUIT.get()).build(null));
    public static final RegistryObject<TileEntityType<PortalHeaterTileEntity>> PORTAL_HEATER = TILE_ENTITY_TYPES.register("portal_heater", () ->
            TileEntityType.Builder.create(PortalHeaterTileEntity::new, ModBlocks.PORTAL_HEATER.get()).build(null));
    public static final RegistryObject<TileEntityType<PortalRemeltedTileEntity>> PORTAL_REMELTED = TILE_ENTITY_TYPES.register("portal_remelted", () ->
            TileEntityType.Builder.create(PortalRemeltedTileEntity::new, ModBlocks.REMELTED_NETHERRACK.get()).build(null));
}
