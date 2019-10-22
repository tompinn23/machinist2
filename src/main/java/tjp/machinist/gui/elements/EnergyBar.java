package tjp.machinist.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import tjp.machinist.Machinist;

import java.util.ArrayList;
import java.util.List;

public class EnergyBar {

    private static final ResourceLocation energyBar = new ResourceLocation(Machinist.MODID, "textures/gui/energy.png");

    private int xPos;
    private int yPos;


    private GuiContainer gc;
    private IEnergyStorage energyStorage;

    public EnergyBar(GuiContainer gc, IEnergyStorage energyStorage, int xPos, int yPos) {
        this.gc = gc;
        this.energyStorage = energyStorage;
        this.xPos = xPos;
        this.yPos = yPos;
    }


    public void drawBackground(double energyFraction) {
        Minecraft.getMinecraft().renderEngine.bindTexture(energyBar);

        gc.drawTexturedModalRect(xPos, yPos, 0, 0, 6, 58);

        gc.drawTexturedModalRect(xPos, yPos + (int)((1.0 - energyFraction) * 58), 6, (int)((1.0 - energyFraction) * 58) + 0, 6,58 - ((int)((1.0 - energyFraction) * 58)));

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
            gc.drawHoveringText(hoveringText, mouseX - xPos, mouseY - yPos);
        }
    }

    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
        return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
    }

}
