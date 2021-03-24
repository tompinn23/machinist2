package one.tlph.machinist.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.BooleanSupplier;

public class ConfiguredOreFeature<FC extends IFeatureConfig, F extends Feature<FC>> extends ConfiguredFeature<FC, F> {

    private BooleanSupplier enabled;
    private float chance = 1.0f;

    public ConfiguredOreFeature(F featureIn, FC configIn, BooleanSupplier enabled) {
        super(featureIn, configIn);
        this.enabled = enabled;
    }

    public ConfiguredOreFeature<FC, F> setChance(float chance) {

        this.chance = chance;
        return this;
    }

    @Override
    public boolean generate(@Nonnull ISeedReader reader, @Nonnull ChunkGenerator chunkGenerator, @Nonnull Random rand, @Nonnull BlockPos pos) {
        if (enabled.getAsBoolean() && rand.nextFloat() < chance) {
            return super.generate(reader, chunkGenerator, rand, pos);
        }
        return false;
    }

}
