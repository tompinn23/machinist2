package one.tlph.machinist.init.registries.util;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class FeatureDeferredRegistry extends WrappedDeferredRegister<Feature<?>> {

    public FeatureDeferredRegistry(String modid) {
        super(modid,ForgeRegistries.FEATURES);
    }

    public <CONFIG extends IFeatureConfig, FEATURE extends Feature<CONFIG>> FeatureRegistryObject<CONFIG, FEATURE> register(String name, Supplier<FEATURE> sup) {
        return register(name, sup, FeatureRegistryObject::new);
    }

}
