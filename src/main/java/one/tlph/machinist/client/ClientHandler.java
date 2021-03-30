package one.tlph.machinist.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ColorHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.init.registries.ModEffects;

@Mod.EventBusSubscriber(modid = Machinist.MODID, value = Dist.CLIENT)
public class ClientHandler {

    public float heatStrokeAlpha;


    @SuppressWarnings("deprecation")
    private void renderHeatStroke(float timeInPortal) {
        if (timeInPortal < 1.0F) {
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * 0.8F + 0.2F;
        }

        Minecraft mc = Minecraft.getInstance();
        double scaledHeight = mc.getMainWindow().getScaledHeight();
        double scaledWidth = mc.getMainWindow().getScaledWidth();

        RenderSystem.disableAlphaTest();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, timeInPortal);
        mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite textureatlassprite = mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.NETHER_PORTAL.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)scaledHeight, -90.0D).tex(f, f3).endVertex();
        bufferbuilder.pos((double)scaledWidth, (double)scaledHeight, -90.0D).tex(f2, f3).endVertex();
        bufferbuilder.pos((double)scaledWidth, 0.0D, -90.0D).tex(f2, f1).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(f, f1).endVertex();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        if (event.phase != TickEvent.Phase.END || clientPlayer == null) {
            return;
        }
        if(clientPlayer.getActivePotionEffect(ModEffects.HEATSTROKE.get()) != null) {
            heatStrokeAlpha = (1.0f / clientPlayer.getHealth()) + 0.2f;
            if(heatStrokeAlpha > 0.8f) {
                heatStrokeAlpha = 0.8f;
            }
        } else {
            heatStrokeAlpha = 0.0f;
        }
    }


    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if(heatStrokeAlpha > 0.0F) {
                int scaledHeight = event.getWindow().getScaledHeight();
                int scaledWidth = event.getWindow().getScaledWidth();
                AbstractGui.fill(event.getMatrixStack(), 0, 0, scaledWidth, scaledHeight, ColorHelper.PackedColor.packColor((int)(heatStrokeAlpha * 255), 245, 99, 66));
            }
        }
    }
}
