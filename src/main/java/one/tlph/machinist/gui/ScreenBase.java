package one.tlph.machinist.gui;


import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import one.tlph.machinist.util.RenderHelper;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

public abstract class ScreenBase<T extends Container> extends ContainerScreen<T> {

    public ScreenBase(final T container, final PlayerInventory inventorySlotsIn, final ITextComponent title) {
        super(container, inventorySlotsIn, title);
    }

    
    
    

    @Override
	public void render(int mouseX, int mouseY, float partialTicks) {
    	this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}



//    public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float textureWidth,
//                                           float textureHeight) {
//        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
//    }

    public void drawFluid(int x, int y, FluidStack fluid, int width, int height) {
        if(fluid == null) {
            return;
        }
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderHelper.setBlockTextureSheet();
        int color = fluid.getFluid().getAttributes().getColor();
        RenderHelper.setGLColorFromInt(color);
        drawTiledTexture(x, y, RenderHelper.getTexture(fluid.getFluid().getAttributes().getStill(fluid)), width, height);
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
        buffer.pos(x, y + height, this.itemRenderer.zLevel).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y + height, this.itemRenderer.zLevel).tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y, this.itemRenderer.zLevel).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
        buffer.pos(x, y, this.itemRenderer.zLevel).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }


    @Override
    protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);

    @Override
    protected abstract void drawGuiContainerForegroundLayer(int mouseX, int mouseY);




}
