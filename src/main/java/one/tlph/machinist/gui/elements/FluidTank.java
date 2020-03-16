package one.tlph.machinist.gui.elements;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.IFluidTank;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.gui.GuiContainerBase;
import one.tlph.machinist.util.RenderHelper;

public class FluidTank extends IGuiElement {

    public static final ResourceLocation FLUID_TANK = new ResourceLocation(Machinist.MODID, "textures/gui/fluidtank.png");

    protected IFluidTank tank;

    public FluidTank(GuiContainerBase gc, int posX, int posY, IFluidTank tank) {
        super(gc, posX, posY);
        this.tank = tank;
        this.texH = 51;
        this.texW = 28;
        this.sizeX = 14;
        this.sizeY = 51;
    }


    @Override
    public void drawBackground() {
        RenderHelper.bindTexture(FLUID_TANK);
        drawTexturedModalRect(xPos, yPos, 0, 0, sizeX, sizeY);
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
    public void drawForeground(int mouseX, int mouseY) {

    }
}
