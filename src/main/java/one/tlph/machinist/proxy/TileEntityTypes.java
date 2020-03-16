package one.tlph.machinist.proxy;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.ModBlocks;
import one.tlph.machinist.tileentity.BlastFurnaceCasingTileEntity;
import one.tlph.machinist.tileentity.BlastFurnaceControllerTileEntity;
import one.tlph.machinist.tileentity.CrusherTileEntity;
import one.tlph.machinist.tileentity.SmelterTileEntity;

@ObjectHolder(Machinist.MODID)
public class TileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Machinist.MODID);

    public static final RegistryObject<TileEntityType<SmelterTileEntity>> SMELTER = TILE_ENTITY_TYPES.register("smelter", () ->
            TileEntityType.Builder.create(SmelterTileEntity::new, ModBlocks.smelter).build(null));

    public static final RegistryObject<TileEntityType<CrusherTileEntity>> CRUSHER = TILE_ENTITY_TYPES.register("crusher", () ->
            TileEntityType.Builder.create(CrusherTileEntity::new, ModBlocks.crusher).build(null));

    public static final RegistryObject<TileEntityType<BlastFurnaceCasingTileEntity>> BLAST_FURNACE_CASING = TILE_ENTITY_TYPES.register("blast_furnace_casing", () ->
            TileEntityType.Builder.create(BlastFurnaceCasingTileEntity::new, ModBlocks.blastCasing).build(null));

    public static final RegistryObject<TileEntityType<BlastFurnaceControllerTileEntity>> BLAST_FURNACE_CONTROLLER = TILE_ENTITY_TYPES.register("blast_furnace_controller", () ->
            TileEntityType.Builder.create(BlastFurnaceControllerTileEntity::new, ModBlocks.blastController).build(null));



}
