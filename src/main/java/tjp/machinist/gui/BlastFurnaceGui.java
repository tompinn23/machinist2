package tjp.machinist.gui;

import net.minecraft.util.ResourceLocation;
import tjp.machinist.Machinist;
import tjp.machinist.container.BlastFurnaceMultiContainer;
import tjp.machinist.tileentity.BlastFurnaceMultiBlockTileEntity;

public class BlastFurnaceGui extends GuiContainerBase {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;

    protected BlastFurnaceMultiBlockTileEntity te;
    private static final ResourceLocation guiTexture = new ResourceLocation(Machinist.MODID, "textures/gui/blastfurnace.png");

    public BlastFurnaceGui(BlastFurnaceMultiBlockTileEntity te, BlastFurnaceMultiContainer container) {
        super(container);
        xSize = WIDTH;
        ySize = HEIGHT;
        this.te = te;
    }


    @Override
    public void initGui() {
        super.initGui();

    }



    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0 ,0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }
}
