package one.tlph.machinist.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import one.tlph.machinist.container.ConduitContainer;

public class ConduitGui extends ScreenBase<ConduitContainer> {

    public ConduitGui(ConduitContainer container, PlayerInventory inventorySlotsIn, ITextComponent title) {
        super(container, inventorySlotsIn, title);
    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {

    }
}
