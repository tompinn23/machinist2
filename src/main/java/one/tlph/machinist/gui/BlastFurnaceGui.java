package one.tlph.machinist.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.container.BlastFurnaceMultiContainer;
import one.tlph.machinist.init.registries.ModBlocks;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceMultiBlockTileEntity;
import one.tlph.machinist.util.RenderHelper;

public class BlastFurnaceGui extends ScreenBase<BlastFurnaceMultiContainer> {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;

    public static final int COOK_U = 176;
    public static final int COOK_V = 14;
    public static final int COOK_XPOS = 79;
    public static final int COOK_YPOS = 35;
    public static final int COOK_WIDTH = 24;
    public static final int COOK_HEIGHT = 16;
    private static final int FLAME_XPOS = 44;
    private static final int FLAME_YPOS = 38;
    private static final int FLAME_HEIGHT = 14;
    private static final int FLAME_U = 176;
    private static final int FLAME_V = 0;
    private static final int FLAME_WIDTH = 14;

    protected BlastFurnaceMultiBlockTileEntity te;
    private static final ResourceLocation guiTexture = new ResourceLocation(Machinist.MODID, "textures/gui/blastfurnace.png");

    public BlastFurnaceGui(final BlastFurnaceMultiContainer container, final PlayerInventory inventory, final ITextComponent title) {
        super(container, inventory, ModBlocks.BLAST_FURNACE_CONTROLLER.get().getTranslatedName());
        xSize = WIDTH;
        ySize = HEIGHT;
    }





    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderHelper.bindTexture(guiTexture);
        blit(stack, guiLeft, guiTop, 0 ,0, xSize, ySize);
        double progress = container.tileEntity.getCookProgress();
        blit(stack,guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_U, COOK_V, (int)(progress * COOK_WIDTH), COOK_HEIGHT);
        double fuelLeft = container.tileEntity.getFuelLeft();
        blit(stack,guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS + (int)((1.0 - fuelLeft) * FLAME_HEIGHT), FLAME_U, (int)((1.0 - fuelLeft) * FLAME_HEIGHT) + FLAME_V, FLAME_WIDTH, FLAME_HEIGHT - (int)((1.0 - fuelLeft) * FLAME_HEIGHT));

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {

    }
}
