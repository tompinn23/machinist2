package tjp.engineering.blocks.smelter;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import tjp.engineering.Engineering;

public class SmelterGui extends GuiContainer {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;
	
	private static final ResourceLocation background = new ResourceLocation(Engineering.MODID, "textures/gui/smelter.png");
	
	public SmelterGui(SmelterTileEntity tileEntity, SmelterContainer container) {
		super(container);
		
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft,guiTop, 0,0, xSize, ySize);
	}
	
}
