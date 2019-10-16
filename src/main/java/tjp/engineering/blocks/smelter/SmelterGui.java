package tjp.engineering.blocks.smelter;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import tjp.engineering.Engineering;

public class SmelterGui extends GuiContainer {
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
	private static final ResourceLocation background = new ResourceLocation(Engineering.MODID, "textures/gui/smelter.png");
	
	public SmelterGui(SmelterTileEntity tileEntity, SmelterContainer container) {
		super(container);
		
		xSize = WIDTH;
		ySize = HEIGHT;
		this.te = tileEntity;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft,guiTop, 0,0, xSize, ySize);
		
		double cookProgress = te.fractionOfCookTimeComplete();
		Engineering.logger.info("cookProgress = {}", cookProgress);
		drawTexturedModalRect(guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_U, COOK_V, (int)(cookProgress * COOK_WIDTH), COOK_HEIGHT);
		
		double fuelRemaining = te.fractionOfFuelRemaining();
		drawTexturedModalRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS + (int)((1.0 - fuelRemaining) * FLAME_HEIGHT), FLAME_U, (int)((1.0 - fuelRemaining) * FLAME_HEIGHT) + FLAME_V, FLAME_WIDTH, FLAME_HEIGHT - (int)((1.0 - fuelRemaining) * FLAME_HEIGHT));
		
		double energyLeft = te.fractionOfEnergyRemaining();
		Engineering.logger.info("energyLeft = {}", energyLeft);
		drawTexturedModalRect(guiLeft + ENERGY_XPOS, guiTop + ENERGY_YPOS + (int)((1.0 - energyLeft) * ENERGY_HEIGHT), ENERGY_U, (int)((1.0 - energyLeft) * ENERGY_HEIGHT) + ENERGY_V, ENERGY_WIDTH,ENERGY_HEIGHT - ((int)((1.0 - energyLeft) * ENERGY_HEIGHT)));
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

		// If the mouse is over one of the burn time indicator add the burn time indicator hovering text
		if (isInRect(guiLeft + ENERGY_XPOS , guiTop + ENERGY_YPOS, ENERGY_WIDTH, ENERGY_HEIGHT, mouseX, mouseY)) {
				hoveringText.add("Energy:");
				hoveringText.add(te.getEnergyStored() + "/" + te.getMaxEnergyStored() + "RF");
			}
		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}
	}
	
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
	
}
