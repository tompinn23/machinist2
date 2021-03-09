package one.tlph.machinist.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.gui.ScreenBase;

public class Button extends IGuiElement {

    public Button(ScreenBase gc, int posX, int posY, ResourceLocation[] buttons) {
        super(gc, posX, posY);
    }

    @Override
    public void drawBackground(MatrixStack stack) {

    }

    @Override
    public void drawForeground(MatrixStack stack, int mouseX, int mouseY) {

    }
}
