package one.tlph.machinist.gui.elements;

import one.tlph.machinist.gui.ScreenBase;

public abstract class IGuiElement {

    protected ScreenBase gc;
    protected int xPos;
    protected int yPos;

    protected int texW;
    protected int texH;

    protected int sizeX;
    protected int sizeY;

    public IGuiElement(ScreenBase gc, int posX, int posY) {
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

        ScreenBase.blit(x, y, u, v, width, height, texW, texH);
    }

}
