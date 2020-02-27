package tjp.machinist.gui.elements;

import tjp.machinist.gui.GuiContainerBase;

public abstract class IGuiElement {

    protected GuiContainerBase gc;
    protected int xPos;
    protected int yPos;

    protected int texW;
    protected int texH;

    protected int sizeX;
    protected int sizeY;

    public IGuiElement(GuiContainerBase gc, int posX, int posY) {
        this.gc = gc;
        this.xPos = posX;
        this.yPos = posY;
    }

    public abstract void drawBackground();
    public abstract void drawForeground(int mouseX, int mouseY);

    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
        return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {

        gc.drawSizedTexturedModalRect(x, y, u, v, width, height, texW, texH);
    }

}
