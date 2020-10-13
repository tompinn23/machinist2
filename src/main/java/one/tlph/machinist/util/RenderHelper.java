package one.tlph.machinist.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.lwjgl.opengl.GL11;

/**
 * Contains various helper functions to assist with rendering.
 *
 * @author King Lemming
 */
public final class RenderHelper {

    public static final double RENDER_OFFSET = 1.0D / 512.0D;
    public static final ResourceLocation MC_BLOCK_SHEET = new ResourceLocation("textures/atlas/blocks.png");
    public static final ResourceLocation MC_FONT_DEFAULT = new ResourceLocation("textures/font/ascii.png");
    public static final ResourceLocation MC_FONT_ALTERNATE = new ResourceLocation("textures/font/ascii_sga.png");
    public static final ResourceLocation MC_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private RenderHelper() {

    }

    public static TextureManager engine() {

        return Minecraft.getInstance().textureManager;
    }


    public static Tessellator tessellator() {

        return Tessellator.getInstance();
    }

    public static ItemRenderer renderItem() {

        return Minecraft.getInstance().getItemRenderer();
    }

    public static void setGLColorFromInt(int color) {

        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.color4f(red, green, blue, 1.0F);
    }

    public static void resetColor() {

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderIcon(TextureAtlasSprite icon, double z) {

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(0, 16, z).tex(icon.getMinU(), icon.getMaxV());
        buffer.pos(16, 16, z).tex(icon.getMaxU(), icon.getMaxV());
        buffer.pos(16, 0, z).tex(icon.getMaxU(), icon.getMinV());
        buffer.pos(0, 0, z).tex(icon.getMinU(), icon.getMinV());
        Tessellator.getInstance().draw();

    }

    public static void renderIcon(double x, double y, double z, TextureAtlasSprite icon, int width, int height) {

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, z).tex(icon.getMinU(), icon.getMaxV());
        buffer.pos(x + width, y + height, z).tex(icon.getMaxU(), icon.getMaxV());
        buffer.pos(x + width, y, z).tex(icon.getMaxU(), icon.getMinV());
        buffer.pos(x, y, z).tex(icon.getMinU(), icon.getMinV());
        Tessellator.getInstance().draw();
    }

    public static TextureAtlasSprite getFluidTexture(Fluid fluid) {

        if (fluid == null) {
            fluid = Fluids.LAVA;
        }
        return getTexture(fluid.getRegistryName());
    }

    public static TextureAtlasSprite getFluidTexture(FluidStack fluid) {

        if (fluid == null || fluid.getFluid() == null) {
            fluid = new FluidStack(Fluids.LAVA, 1);
        }
        return getTexture(fluid.getFluid().getRegistryName());
    }

    public static void bindTexture(ResourceLocation texture) {

        engine().bindTexture(texture);
    }

    public static void setBlockTextureSheet() {

        bindTexture(MC_BLOCK_SHEET);
    }

    public static void setDefaultFontTextureSheet() {

        bindTexture(MC_FONT_DEFAULT);
    }

    public static TextureAtlasSprite getTexture(ResourceLocation location) {
        return Minecraft.getInstance().getAtlasSpriteGetter(location).apply(location);
    }

    public static void setSGAFontTextureSheet() {

        bindTexture(MC_FONT_ALTERNATE);
    }
    public static void enableStandardItemLighting() {

        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
    }

}