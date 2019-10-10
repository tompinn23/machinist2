package tjp.engineering.blocks.smelter;

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
		drawTexturedModalRect(guiLeft + COOK_XPOS, guiTop + COOK_YPOS, COOK_U, COOK_V, (int)(cookProgress * COOK_WIDTH), COOK_HEIGHT);
		
		double fuelRemaining = te.fractionOfFuelRemaining();
		drawTexturedModalRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS, FLAME_U, FLAME_V, FLAME_WIDTH, (int)(fuelRemaining * FLAME_HEIGHT));
	}
	
}
