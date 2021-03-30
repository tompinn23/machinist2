package one.tlph.machinist.init.registries;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.potion.HeatstrokeEffect;

public class ModEffects {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Machinist.MODID);

    public static final RegistryObject<Effect> HEATSTROKE = EFFECTS.register("heatstroke", () -> new HeatstrokeEffect(16081730));
}
