package one.tlph.machinist.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.gui.elements.EnergyBar;
import one.tlph.machinist.blocks.smelter.SmelterTileEntity;
import one.tlph.machinist.util.RenderHelper;

import java.util.ArrayList;
import java.util.List;

public class SmelterGui extends ScreenBase<SmelterContainer> {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;
	
	final int COOK_XPOS = 79;
	final int COOK_YPOS = 35;
	final int COOK_U = 176;
	final int COOK_V = 14;
	final int COOK_WIDTH = 24;
	final int COOK_HEIGHT = 17;
	
	final int FLAME_XPOS = 56;
	final int FLAME_YPOS = 36;
	final int FLAME_U = 176;   // texture position of flame icon
	final int FLAME_V = 0;
	final int FLAME_WIDTH = 14;
	final int FLAME_HEIGHT = 14;
	
	final int ENERGY_XPOS = 6;
	final int ENERGY_YPOS = 14;
	final int ENERGY_U = 176;
	final int ENERGY_V = 31;
	final int ENERGY_WIDTH = 6;
	final int ENERGY_HEIGHT = 58;
	
	private SmelterTileEntity te;
	private EnergyBar bar;
	private static final ResourceLocation background = new ResourceLocation(Machinist.MODID, "textures/gui/smelter.png");
	
	public SmelterGui(final SmelterContainer container, final PlayerInventory inventory, final ITextComponent title) {
		super(container, inventory, new TranslationTextComponent("machinist.smelter.gui.title"));
		
		xSize = WIDTH;
		ySize = HEIGHT;
		this.te = container.te;
	}

	@Override
	protected void init() {
		super.init();
		this.bar = new EnergyBar(this, te, guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderHelper.bindTexture(background);
		blit(stack, guiLeft,guiTop, 0,0, xSize, ySize);
		
		double cookProgress = te.fractionOfCookTimeComplete();
		//Machinist.logger.info("cookProgress = {}", cookProgress);
		blit(stack,guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_U, COOK_V, (int)(cookProgress * COOK_WIDTH), COOK_HEIGHT);
		
		//double fuelRemaining = te.fractionOfFuelRemaining();
		//drawTexturedModalRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS + (int)((1.0 - fuelRemaining) * FLAME_HEIGHT), FLAME_U, (int)((1.0 - fuelRemaining) * FLAME_HEIGHT) + FLAME_V, FLAME_WIDTH, FLAME_HEIGHT - (int)((1.0 - fuelRemaining) * FLAME_HEIGHT));
		
		//double energyLeft = te.fractionOfEnergyRemaining();
		//Machinist.logger.info("energyLeft = {}", energyLeft);
		//drawTexturedModalRect(guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS + (int)((1.0 - energyLeft) * ENERGY_HEIGHT), ENERGY_U, (int)((1.0 - energyLeft) * ENERGY_HEIGHT) + ENERGY_V, ENERGY_WIDTH,ENERGY_HEIGHT - ((int)((1.0 - energyLeft) * ENERGY_HEIGHT)));
		bar.drawBackground(stack);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
		List<StringTextComponent> hoveringText = new ArrayList<StringTextComponent>();

		// If the mouse is over the progress bar add the progress bar hovering text
		if (isInRect(guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_WIDTH, COOK_HEIGHT, mouseX, mouseY)){
			hoveringText.add(new StringTextComponent("Progress:"));
			int cookPercentage =(int)(te.fractionOfCookTimeComplete() * 100);
			hoveringText.add(new StringTextComponent(cookPercentage + "%"));
		}

		bar.drawForeground(stack, mouseX, mouseY);
		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			this.renderFloatingTooltip(stack, hoveringText, mouseX, mouseY);
		}
	}
	
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
	
}
