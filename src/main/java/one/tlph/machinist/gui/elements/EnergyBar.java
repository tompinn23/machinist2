package one.tlph.machinist.gui.elements;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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


    public void drawBackground() {
        RenderHelper.bindTexture(energyBar);

        drawTexturedModalRect(xPos, yPos, 0, 0, 6, 58);

        double fraction = energyStorage.getEnergyStored() / (double)energyStorage.getMaxEnergyStored();
        fraction = MathHelper.clamp(fraction, 0.0, 1.0);
        RenderHelper.bindTexture(energyBar);
        drawTexturedModalRect(xPos, yPos + (int)((1.0 - fraction) * 58), 6, (int)((1.0 - fraction) * 58), 6,58 - ((int)((1.0 - fraction) * 58)));

    }


    public void drawForeground(int mouseX, int mouseY) {
        List<String> hoveringText = new ArrayList<String>();


        // If the mouse is over one of the burn time indicator add the burn time indicator hovering text
        if (isInRect(xPos , yPos, 6, 58, mouseX, mouseY)) {
            hoveringText.add("Energy:");
            hoveringText.add(energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored() + "RF");
        }
        // If hoveringText is not empty draw the hovering text
        if (!hoveringText.isEmpty()){
            gc.renderTooltip(hoveringText, mouseX - xPos, mouseY - yPos);
        }
    }


}
