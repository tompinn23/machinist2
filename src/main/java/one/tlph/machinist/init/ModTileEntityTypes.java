package one.tlph.machinist.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.tileentity.*;

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

    public static final RegistryObject<TileEntityType<CableTileEntity>> CABLE_TILE_ENTITY = TILE_ENTITY_TYPES.register("cable", () ->
            TileEntityType.Builder.create(CableTileEntity::new, ModBlocks.CABLE.get()).build(null));
}
