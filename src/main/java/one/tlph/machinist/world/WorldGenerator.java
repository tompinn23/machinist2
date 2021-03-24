package one.tlph.machinist.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.Machinist;

import javax.crypto.Mac;

import static one.tlph.machinist.world.Features.ORE_COPPER;

@Mod.EventBusSubscriber(modid = Machinist.MODID)
public class WorldGenerator {


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void biomeloadingEvent(final BiomeLoadingEvent event) {
        addOreGeneration(event);
    }

    public static void addOreGeneration(final BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generationSettingsBuilder = event.getGeneration();
        Biome.Category category = event.getCategory();
        if(isOverworldBiome(category)) {
            withCopperOre(generationSettingsBuilder);
        }
    }

    private static void withCopperOre(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ORE_COPPER);
    }

    private static boolean isOverworldBiome(Biome.Category category) {
        return category != Biome.Category.NONE && category != Biome.Category.THEEND && category != Biome.Category.NETHER;
    }


}
