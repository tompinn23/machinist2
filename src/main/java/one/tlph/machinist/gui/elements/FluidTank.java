package one.tlph.machinist.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.IFluidTank;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.gui.ScreenBase;
import one.tlph.machinist.util.RenderHelper;

public class FluidTank extends IGuiElement {

    public static final ResourceLocation FLUID_TANK = new ResourceLocation(Machinist.MODID, "textures/gui/fluidtank.png");

    protected IFluidTank tank;

    public FluidTank(ScreenBase gc, int posX, int posY, IFluidTank tank) {
        super(gc, posX, posY);
        this.tank = tank;
        this.texH = 51;
        this.texW = 28;
        this.sizeX = 14;
        this.sizeY = 51;
    }


    @Override
    public void drawBackground(MatrixStack stack) {
        RenderHelper.bindTexture(FLUID_TANK);
        drawTexturedModalRect(stack, xPos, yPos, 0, 0, sizeX, sizeY);
        drawFluid();
    }

    private void drawFluid() {
        int amt = getScaled();
        RenderHelper.setBlockTextureSheet();
        gc.drawFluid(xPos + 1, xPos + sizeY - amt - 1, tank.getFluid(), sizeX - 1, amt -1);
    }

    protected int getScaled() {

        if (tank.getCapacity() < 0) {
            return sizeY;
        }
        long fraction = (long) tank.getFluidAmount() * sizeY / tank.getCapacity();

        return MathHelper.ceil(fraction);
    }


    @Override
    public void drawForeground(MatrixStack stack, int mouseX, int mouseY) {

    }
}
