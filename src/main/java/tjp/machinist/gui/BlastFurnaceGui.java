package tjp.machinist.gui;

import tjp.machinist.blocks.blastFurnace.BlastFurnaceMultiContainer;
import tjp.machinist.blocks.blastFurnace.BlastFurnaceMultiController;

public class BlastFurnaceGui extends GuiContainerBase {

    protected BlastFurnaceMultiController te;

    public BlastFurnaceGui(BlastFurnaceMultiController te, BlastFurnaceMultiContainer container) {
        super(container);
        this.te = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }
}
