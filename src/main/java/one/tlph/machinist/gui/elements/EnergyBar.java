package one.tlph.machinist.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraftforge.energy.IEnergyStorage;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.gui.ScreenBase;
import one.tlph.machinist.util.RenderHelper;

import java.util.ArrayList;
import java.util.List;

public class EnergyBar extends IGuiElement {

    private static final ResourceLocation energyBar = new ResourceLocation(Machinist.MODID, "textures/gui/energy.png");





    private IEnergyStorage energyStorage;

    public EnergyBar(ScreenBase gc, IEnergyStorage energyStorage, int xPos, int yPos) {
        super(gc, xPos, yPos);
        this.energyStorage = energyStorage;
        this.texH = 58;
        this.texW = 12;
    }


    public void drawBackground(MatrixStack stack) {
        RenderHelper.bindTexture(energyBar);

        drawTexturedModalRect(stack, xPos, yPos, 0, 0, 6, 58);

        double fraction = energyStorage.getEnergyStored() / (double)energyStorage.getMaxEnergyStored();
        fraction = MathHelper.clamp(fraction, 0.0, 1.0);
        RenderHelper.bindTexture(energyBar);
        drawTexturedModalRect(stack, xPos, yPos + (int)((1.0 - fraction) * 58), 6, (int)((1.0 - fraction) * 58), 6,58 - ((int)((1.0 - fraction) * 58)));

    }


    public void drawForeground(MatrixStack stack, int mouseX, int mouseY) {
        List<StringTextComponent> hoveringText = new ArrayList<StringTextComponent>();


        // If the mouse is over one of the burn time indicator add the burn time indicator hovering text
        if (isInRect(xPos , yPos, 6, 58, mouseX, mouseY)) {
            hoveringText.add(new StringTextComponent("Energy:"));
            hoveringText.add(new StringTextComponent(energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored() + "RF"));
        }

        // If hoveringText is not empty draw the hovering text
        if (!hoveringText.isEmpty()){
            gc.renderFloatingTooltip(stack, hoveringText, mouseX, mouseY);
        }
    }


}
