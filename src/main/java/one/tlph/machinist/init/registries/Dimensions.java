package one.tlph.machinist.init.registries;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class Dimensions {
    public static final RegistryKey<DimensionType> UNDER_NETHER_DIMENSION = RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, new ResourceLocation("machinist", "under_nether"));
    public static final RegistryKey<World> UNDER_NETHER_WORLD = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("machinist", "under_nether"));

}
