package one.tlph.machinist.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.init.registries.ModBlocks;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;

import static one.tlph.machinist.init.ModFlags.GEN_COPPER;
import static one.tlph.machinist.init.ModFlags.getFlag;

//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Features {

    private Features() {}

    public static void setup() {
        ORE_COPPER = register("ore_copper",
                new ConfiguredOreFeature<>(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.COPPER_ORE.get().getDefaultState(), 9), getFlag(GEN_COPPER))
                .withPlacement(Placement.RANGE.configure(topRange(40, 80)))
                .square()
                .func_242731_b(6));


    }


    private static DepthAverageConfig depthRange(int base, int spread) {

        return new DepthAverageConfig(base, spread);
    }

    private static TopSolidRangeConfig topRange(int min, int max) {

        return new TopSolidRangeConfig(min, min, max);
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {

        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Machinist.MODID, key), configuredFeature);
    }

    public static ConfiguredFeature<?, ?> ORE_COPPER;


}
