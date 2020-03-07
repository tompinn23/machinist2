package tjp.machinist.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import tjp.machinist.util.RenderHelper;

public abstract class GuiContainerBase extends GuiContainer {

    public GuiContainerBase(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    public void bindTexture(ResourceLocation texture) {
        this.mc.renderEngine.bindTexture(texture);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float textureWidth,
                                           float textureHeight) {
        Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
    }

    public void drawFluid(int x, int y, FluidStack fluid, int width, int height) {
        if(fluid == null) {
            return;
        }
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderHelper.setBlockTextureSheet();
        int color = fluid.getFluid().getColor(fluid);
        RenderHelper.setGLColorFromInt(color);
        drawTiledTexture(x, y, RenderHelper.getTexture(fluid.getFluid().getStill(fluid)), width, height);
        GL11.glPopMatrix();
    }

    public void drawTiledTexture(int x, int y, TextureAtlasSprite texture, int width, int height) {
        int i;
        int j;

        int drawHeight;
        int drawWidth;

        for(i = 0; i < width; i+= 16) {
            for(j = 0; j < height; j+= 16) {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                drawScaledTexturedModelRectFromIcon(x + i, y + j, texture, drawWidth, drawHeight);
            }
        }
    }

    public void drawScaledTexturedModelRectFromIcon(int x, int y, TextureAtlasSprite texture, int drawWidth, int drawHeight) {
        if (texture == null) {
            return;
        }
        double minU = texture.getMinU();
        double maxU = texture.getMaxU();
        double minV = texture.getMinV();
        double maxV = texture.getMaxV();

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, this.zLevel).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y + height, this.zLevel).tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y, this.zLevel).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
        buffer.pos(x, y, this.zLevel).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }


    @Override
    protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);

    @Override
    protected abstract void drawGuiContainerForegroundLayer(int mouseX, int mouseY);
}
