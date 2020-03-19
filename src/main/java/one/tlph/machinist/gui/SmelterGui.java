package one.tlph.machinist.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.gui.elements.EnergyBar;
import one.tlph.machinist.tileentity.SmelterTileEntity;
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
		this.bar = new EnergyBar(this, te.getCapability(CapabilityEnergy.ENERGY, null).orElse(null), ENERGY_XPOS, ENERGY_YPOS);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderHelper.bindTexture(background);
		blit(guiLeft,guiTop, 0,0, xSize, ySize);
		
		double cookProgress = te.fractionOfCookTimeComplete();
		//Machinist.logger.info("cookProgress = {}", cookProgress);
		blit(guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_U, COOK_V, (int)(cookProgress * COOK_WIDTH), COOK_HEIGHT);
		
		//double fuelRemaining = te.fractionOfFuelRemaining();
		//drawTexturedModalRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS + (int)((1.0 - fuelRemaining) * FLAME_HEIGHT), FLAME_U, (int)((1.0 - fuelRemaining) * FLAME_HEIGHT) + FLAME_V, FLAME_WIDTH, FLAME_HEIGHT - (int)((1.0 - fuelRemaining) * FLAME_HEIGHT));
		
		//double energyLeft = te.fractionOfEnergyRemaining();
		//Machinist.logger.info("energyLeft = {}", energyLeft);
		//drawTexturedModalRect(guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS + (int)((1.0 - energyLeft) * ENERGY_HEIGHT), ENERGY_U, (int)((1.0 - energyLeft) * ENERGY_HEIGHT) + ENERGY_V, ENERGY_WIDTH,ENERGY_HEIGHT - ((int)((1.0 - energyLeft) * ENERGY_HEIGHT)));
		bar.drawBackground();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		List<String> hoveringText = new ArrayList<String>();

		// If the mouse is over the progress bar add the progress bar hovering text
		if (isInRect(guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_WIDTH, COOK_HEIGHT, mouseX, mouseY)){
			hoveringText.add("Progress:");
			int cookPercentage =(int)(te.fractionOfCookTimeComplete() * 100);
			hoveringText.add(cookPercentage + "%");
		}

		bar.drawForeground(mouseX, mouseY);
		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			this.renderTooltip(hoveringText, mouseX - guiLeft, mouseY - guiTop);
		}
	}
	
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
	
}
