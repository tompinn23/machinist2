package one.tlph.machinist.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.energy.TransferType;
import one.tlph.machinist.gui.ScreenBase;
import one.tlph.machinist.network.packets.EnergyNextModePacket;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;
import one.tlph.machinist.util.RenderHelper;

import java.util.ArrayList;
import java.util.List;

public class EnergyConfig extends IGuiElement {

    private AbstractPoweredTileEntity tileEntity;

    private Button[] energyButtons;
    private int[] buttonSides = new int[] { Direction.UP.getIndex(), 6, Direction.EAST.getIndex(), Direction.NORTH.getIndex(), Direction.WEST.getIndex(), Direction.SOUTH.getIndex(), Direction.DOWN.getIndex()};


    private static ResourceLocation ENERGY_CONFIG = new ResourceLocation(Machinist.MODID, "textures/gui/power_config.png");

    private static int IN_OFF_X = 18;
    private static int IN_OFF_Y = 13;
    private static int INOUT_OFF_X = 10;
    private static int INOUT_OFF_Y = 13;
    private static int OUT_OFF_X = 2;
    private static int OUT_OFF_Y = 13;
    private static int NON_OFF_X = 10;
    private static int NON_OFF_Y = 5;


    public EnergyConfig(ScreenBase gc, int posX, int posY, AbstractPoweredTileEntity tileEntity) {
        super(gc, posX, posY);
        this.tileEntity = tileEntity;
        this.texW = 32;
        this.texH = 32;
        energyButtons = new Button[7];
        for(int i = 0; i < 2; i++) {
            int butSide = buttonSides[i];
            switch(tileEntity.getSidedConfig().get(butSide == 6 ? null : Direction.values()[butSide])) {
                case IN:
                    energyButtons[i] = new Button(gc, posX + 10 + (i * 8), posY + 5, ENERGY_CONFIG,
                            IN_OFF_X, IN_OFF_Y,
                            6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
                case INOUT:
                    energyButtons[i] = new Button(gc, posX + 10 + (i * 8), posY + 5, ENERGY_CONFIG,
                            INOUT_OFF_X, INOUT_OFF_Y,
                            6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
                case OUT:
                    energyButtons[i] = new Button(gc, posX + 10 + (i * 8), posY + 5, ENERGY_CONFIG,
                        OUT_OFF_X, OUT_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
                case NONE:
                    energyButtons[i] = new Button(gc, posX + 10 + (i * 8), posY + 5, ENERGY_CONFIG,
                            NON_OFF_X, NON_OFF_Y,
                            6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
            }
        }
        for(int i = 0; i < 3; i++) {
            int butSide = buttonSides[i + 2];
            switch(tileEntity.getSidedConfig().get(butSide == 6 ? null : Direction.values()[butSide])) {
                case IN:
                    energyButtons[i + 2] = new Button(gc, posX + 2 + (i * 8), posY + 13, ENERGY_CONFIG,
                            IN_OFF_X, IN_OFF_Y,
                            6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
                case INOUT:
                    energyButtons[i+ 2] = new Button(gc, posX + 2 + (i * 8), posY + 13, ENERGY_CONFIG,
                            INOUT_OFF_X, INOUT_OFF_Y,
                            6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
                case OUT:
                    energyButtons[i + 2] = new Button(gc, posX + 2 + (i * 8), posY + 13, ENERGY_CONFIG,
                            OUT_OFF_X, OUT_OFF_Y,
                            6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
                case NONE:
                    energyButtons[i + 2] = new Button(gc, posX + 2 + (i * 8), posY + 13, ENERGY_CONFIG,
                            NON_OFF_X, NON_OFF_Y,
                            6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                    break;
            }

        }
        int butSide = buttonSides[5];
        switch(tileEntity.getSidedConfig().get(Direction.values()[butSide])) {
            case IN:
                energyButtons[5] = new Button(gc, posX + 2, posY + 21, ENERGY_CONFIG,
                        IN_OFF_X, IN_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
            case INOUT:
                energyButtons[5] = new Button(gc, posX + 2, posY + 21, ENERGY_CONFIG,
                        INOUT_OFF_X, INOUT_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
            case OUT:
                energyButtons[5] = new Button(gc, posX + 2, posY + 21, ENERGY_CONFIG,
                        OUT_OFF_X, OUT_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
            case NONE:
                energyButtons[5] = new Button(gc, posX + 2, posY + 21, ENERGY_CONFIG,
                        NON_OFF_X, NON_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
        }
        butSide = buttonSides[6];
        switch(tileEntity.getSidedConfig().get(Direction.values()[butSide])) {
            case IN:
                energyButtons[6] = new Button(gc, posX + 10, posY + 21, ENERGY_CONFIG,
                        IN_OFF_X, IN_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
            case INOUT:
                energyButtons[6] = new Button(gc, posX + 10, posY + 21, ENERGY_CONFIG,
                        INOUT_OFF_X, INOUT_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
            case OUT:
                energyButtons[6] = new Button(gc, posX + 10, posY + 21, ENERGY_CONFIG,
                        OUT_OFF_X, OUT_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
            case NONE:
                energyButtons[6] = new Button(gc, posX + 10, posY + 21, ENERGY_CONFIG,
                        NON_OFF_X, NON_OFF_Y,
                        6, 6, this.texW, this.texH, butSide, this::energyButtonPress);
                break;
        }
    }



    private void updateButton(Button button) {
        Direction side = button.state == 6 ? null : Direction.values()[button.state];
        TransferType type = this.tileEntity.getSidedConfig().get(side);
        switch(type) {
            case IN:
                button.xOff = IN_OFF_X;
                button.yOff = IN_OFF_Y;
                break;
            case INOUT:
                button.xOff = INOUT_OFF_X;
                button.yOff = INOUT_OFF_Y;
                break;
            case OUT:
                button.xOff = OUT_OFF_X;
                button.yOff = OUT_OFF_Y;
                break;
            case NONE:
                button.xOff = NON_OFF_X;
                button.yOff = NON_OFF_Y;
                break;
        }
    }



    public void energyButtonPress(Button button) {
        Direction side = null;
        if(button.state > 5) {
            this.tileEntity.getSidedConfig().next(null);
            TransferType newType = this.tileEntity.getSidedConfig().get(null);
            this.tileEntity.getSidedConfig().setSides(newType);
            for(int i = 0; i < energyButtons.length; i++) {
                updateButton(energyButtons[i]);
            }
        } else {
            side = Direction.values()[button.state];
            this.tileEntity.getSidedConfig().next(side);
            updateButton(button);
        }
        Machinist.NETWORK.toServer(new EnergyNextModePacket(button.state, tileEntity.getPos()));
    }

    public void mouseClicked(double buttonX, double buttonY, int button) {
        for(int i = 0; i < 7; i++) {
            if(energyButtons[i].mouseClick(buttonX, buttonY, button))
                break;
        }
    }

    @Override
    public void drawBackground(MatrixStack stack) {
        RenderHelper.bindTexture(ENERGY_CONFIG);
        drawTexturedModalRect(stack,xPos, yPos, 0, 0, 29 , 32);
        for(int i = 0; i < 7; i++) {
            energyButtons[i].drawBackgroundNoStateChange(stack);
        }
    }


    @Override
    public void drawForeground(MatrixStack stack, int mouseX, int mouseY) {
        List<IFormattableTextComponent> hoveringText = new ArrayList<IFormattableTextComponent>();
        for(int i = 0; i < 7; i++) {
            if(energyButtons[i].isMouseIn(mouseX, mouseY)) {
                hoveringText.add(new StringTextComponent("Facing: ").appendSibling(new StringTextComponent(buttonSides[i] > 5 ? "ALL" : Direction.values()[buttonSides[i]].toString()).mergeStyle(TextFormatting.GOLD)));
                hoveringText.add(new StringTextComponent("Mode: ").appendSibling(new StringTextComponent(tileEntity.getSidedConfig().get(buttonSides[i] > 5 ? null : Direction.values()[buttonSides[i]]).toString()).mergeStyle(TextFormatting.GOLD)));
                break;
            }
        }
        if (!hoveringText.isEmpty()){
            gc.renderFloatingTooltip(stack, hoveringText, mouseX, mouseY);
        }
    }
}
