package one.tlph.machinist.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import one.tlph.machinist.gui.ScreenBase;
import one.tlph.machinist.util.RenderHelper;

public class Button extends IGuiElement {

    private IPressable onPress;
    private ResourceLocation location;

    public int xOff = 0;
    public int yOff = 0;
    public int sizeX = 0;
    public int sizeY = 0;

    public int state;



    public Button(ScreenBase gc, int posX, int posY, ResourceLocation button, int xOff, int yOff, int sizeX, int sizeY, int texW, int texH, int state, IPressable onPress) {
        super(gc, posX, posY);
        this.location = button;
        this.onPress = onPress;
        this.xOff = xOff;
        this.yOff = yOff;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texH = texH;
        this.texW = texW;
        this.state = state;
    }

    public void onPress() {
        if(this.onPress != null)
            this.onPress.onPress(this);
    }

    public void setOnPress(IPressable onPress) {
        this.onPress = onPress;
    }

    public boolean isMouseIn(double mouseX, double mouseY) {
        return (mouseX >= xPos && mouseX <= xPos + sizeX &&
                mouseY >= yPos && mouseY <= yPos + sizeY);
    }

    public boolean mouseClick(double mouseX, double mouseY, int button) {
        if(isMouseIn(mouseX, mouseY)) {
            onPress();
            return true;
        }
        return false;
    }


    @Override
    public void drawBackground(MatrixStack stack) {
        RenderHelper.bindTexture(location);
        drawBackgroundNoStateChange(stack);
    }

    @Override
    public void drawBackgroundNoStateChange(MatrixStack stack) {
        drawTexturedModalRect(stack, xPos, yPos, xOff, yOff, sizeX, sizeY);
    }

    @Override
    public void drawForeground(MatrixStack stack, int mouseX, int mouseY) {

    }

    @FunctionalInterface
    public interface IPressable {
        void onPress(Button button);
    }

}
