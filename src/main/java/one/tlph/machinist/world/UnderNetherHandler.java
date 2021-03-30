package one.tlph.machinist.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.init.registries.Dimensions;
import one.tlph.machinist.init.registries.ModEffects;

@Mod.EventBusSubscriber(modid = Machinist.MODID)
public class UnderNetherHandler {

    public UnderNetherHandler() {

    }

    public void tickStart(World world) {
        for(PlayerEntity entity : world.getPlayers()) {
            if(entity.getActivePotionEffect(ModEffects.HEATSTROKE.get()) == null)
                entity.addPotionEffect(new EffectInstance(ModEffects.HEATSTROKE.get(), 1000, 1, true, true));
        }
    }


    @SubscribeEvent
    public void onDimensionChannge(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.getFrom() == Dimensions.UNDER_NETHER_WORLD && event.getTo() != Dimensions.UNDER_NETHER_WORLD) {
            if(event.getPlayer().getActivePotionEffect(ModEffects.HEATSTROKE.get()) != null)
                event.getPlayer().removePotionEffect(ModEffects.HEATSTROKE.get());
        }
    }

    @SubscribeEvent
    public void tickStart(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START && event.world.getDimensionKey() == Dimensions.UNDER_NETHER_WORLD) {
            this.tickStart(event.world);
        }
    }




}
