package one.tlph.machinist.init.registries.util;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

public class FeatureRegistryObject<CONFIG extends IFeatureConfig, FEATURE extends Feature<CONFIG>> extends WrappedRegistryObject<FEATURE> {

    public FeatureRegistryObject(RegistryObject<FEATURE> registryObject) {
    super(registryObject);
}

    @Nonnull
    public FEATURE getFeature() {
        return get();
    }

}
