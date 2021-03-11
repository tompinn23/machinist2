package one.tlph.machinist.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.energy.IEnergyStorage;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.energy.Energy;
import one.tlph.machinist.gui.ScreenBase;
import one.tlph.machinist.util.RenderHelper;

import java.util.ArrayList;
import java.util.List;

public class EnergyBar extends IGuiElement {

    private static final ResourceLocation energyBar = new ResourceLocation(Machinist.MODID, "textures/gui/energy.png");





    private TileEntity te;

    public EnergyBar(ScreenBase gc, TileEntity te, int xPos, int yPos) {
        super(gc, xPos, yPos);
        this.te = te;
        this.texH = 64;
        this.texW = 16;
    }


    public void drawBackground(MatrixStack stack) {
        RenderHelper.bindTexture(energyBar);

        drawTexturedModalRect(stack, xPos, yPos, 0, 0, 6, 58);

        IEnergyStorage store = Energy.getNullable(te, null);
        double fraction = store.getEnergyStored() / (double)store.getMaxEnergyStored();
        fraction = MathHelper.clamp(fraction, 0.0, 1.0);
        RenderHelper.bindTexture(energyBar);
        drawTexturedModalRect(stack, xPos, yPos + (int)((1.0 - fraction) * 58), 6, (int)((1.0 - fraction) * 58), 6,58 - ((int)((1.0 - fraction) * 58)));

    }


    public void drawForeground(MatrixStack stack, int mouseX, int mouseY) {
        List<StringTextComponent> hoveringText = new ArrayList<StringTextComponent>();
        IEnergyStorage store = Energy.getNullable(te, null);


        // If the mouse is over one of the burn time indicator add the burn time indicator hovering text
        if (isInRect(xPos , yPos, 6, 58, mouseX, mouseY)) {
            hoveringText.add(new StringTextComponent(store.getEnergyStored() + "/" + store.getMaxEnergyStored() + "FE"));
        }

        // If hoveringText is not empty draw the hovering text
        if (!hoveringText.isEmpty()){
            gc.renderFloatingTooltip(stack, hoveringText, mouseX, mouseY);
        }
    }


}
