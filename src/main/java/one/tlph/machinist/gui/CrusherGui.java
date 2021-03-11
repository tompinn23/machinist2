package one.tlph.machinist.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.gui.elements.EnergyBar;
import one.tlph.machinist.blocks.crusher.CrusherTileEntity;
import one.tlph.machinist.gui.elements.EnergyConfig;
import one.tlph.machinist.util.RenderHelper;

public class CrusherGui extends ScreenBase<CrusherContainer> {
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

    final int CONFIG_XPOS = 176;
    final int CONFIG_YPOS = 14;


    private CrusherTileEntity te;
    private static final ResourceLocation background = new ResourceLocation(Machinist.MODID, "textures/gui/crusher.png");

    private EnergyBar energyBar;
    private EnergyConfig energyConfig;

    public CrusherGui(CrusherContainer container, PlayerInventory inventory, final ITextComponent title) {
        super(container,inventory, new TranslationTextComponent("machinist.crusher.gui.title"));
        this.te = container.te;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        energyConfig.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
	protected void init() {
		super.init();
        this.energyBar = new EnergyBar(this, this.te, guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS);
        //this.energyConfig = new EnergyConfig(this, guiLeft + CONFIG_XPOS, guiTop + CONFIG_YPOS, this.te);
	}


    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderHelper.bindTexture(background);
        blit(stack, guiLeft,guiTop, 0,0, xSize, ySize);

        double cookProgress = te.getCookProgress();
        blit(stack, guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_U, COOK_V, (int)(cookProgress * COOK_WIDTH), COOK_HEIGHT);


        //double energyLeft = te.fractionOfEnergyRemaining();
        energyBar.drawBackground(stack);
        //energyConfig.drawBackground(stack);
        //drawTexturedModalRect(guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS + (int)((1.0 - energyLeft) * ENERGY_HEIGHT), ENERGY_U, (int)((1.0 - energyLeft) * ENERGY_HEIGHT) + ENERGY_V, ENERGY_WIDTH,ENERGY_HEIGHT - ((int)((1.0 - energyLeft) * ENERGY_HEIGHT)));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        energyBar.drawForeground(stack, mouseX, mouseY);
        //energyConfig.drawForeground(stack, mouseX, mouseY);
    }
}
