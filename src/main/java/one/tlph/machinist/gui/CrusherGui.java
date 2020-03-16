package one.tlph.machinist.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.gui.elements.EnergyBar;
import one.tlph.machinist.tileentity.CrusherTileEntity;

public class CrusherGui extends GuiContainerBase {
    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;


    final int ENERGY_XPOS = 6;
    final int ENERGY_YPOS = 14;
    final int ENERGY_U = 176;
    final int ENERGY_V = 31;
    final int ENERGY_WIDTH = 6;
    final int ENERGY_HEIGHT = 58;

    final int COOK_XPOS = 60;
    final int COOK_YPOS = 33;
    final int COOK_U = 176;
    final int COOK_V = 12;
    final int COOK_WIDTH = 28;
    final int COOK_HEIGHT = 19;



    private CrusherTileEntity te;
    private static final ResourceLocation background = new ResourceLocation(Machinist.MODID, "textures/gui/crusher.png");

    private EnergyBar energyBar;

    public CrusherGui(CrusherTileEntity tileEntity, CrusherContainer container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
        this.te = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.energyBar = new EnergyBar(this, te.getCapability(CapabilityEnergy.ENERGY, null), guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture(background);
        drawTexturedModalRect(guiLeft,guiTop, 0,0, xSize, ySize);

        double cookProgress = te.getCookProgress();
        drawTexturedModalRect(guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_U, COOK_V, (int)(cookProgress * COOK_WIDTH), COOK_HEIGHT);


        double energyLeft = te.fractionOfEnergyRemaining();
        energyBar.drawBackground();
        //drawTexturedModalRect(guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS + (int)((1.0 - energyLeft) * ENERGY_HEIGHT), ENERGY_U, (int)((1.0 - energyLeft) * ENERGY_HEIGHT) + ENERGY_V, ENERGY_WIDTH,ENERGY_HEIGHT - ((int)((1.0 - energyLeft) * ENERGY_HEIGHT)));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        energyBar.drawForeground(mouseX, mouseY);
    }
}
