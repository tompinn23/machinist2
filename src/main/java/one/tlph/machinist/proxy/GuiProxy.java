package one.tlph.machinist.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;
import one.tlph.machinist.container.BlastFurnaceMultiContainer;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.gui.BlastFurnaceGui;
import one.tlph.machinist.gui.CrusherGui;
import one.tlph.machinist.gui.SmelterGui;
import one.tlph.machinist.tileentity.BlastFurnaceMultiBlockTileEntity;
import one.tlph.machinist.tileentity.CrusherTileEntity;
import one.tlph.machinist.tileentity.SmelterTileEntity;

import javax.annotation.Nullable;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		switch(ID) {
			case Smelter.GUI_ID:
				return new SmelterContainer(player.inventory, (SmelterTileEntity)te);
			case Crusher.GUI_ID:
				return new CrusherContainer(player.inventory, (CrusherTileEntity)te);
			case BlastFurnaceMultiBlockTileEntity.GUI_ID:
				return new BlastFurnaceMultiContainer(player.inventory, (BlastFurnaceMultiBlockTileEntity)((IMultiblockPart)te).getMultiblockController());
			default:
				return null;
		}
	}
	
	@Override
	public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		switch(ID) {
		case Smelter.GUI_ID:
			SmelterTileEntity smelterTileEntity = (SmelterTileEntity)te;
			return new SmelterGui(smelterTileEntity, new SmelterContainer(player.inventory, smelterTileEntity));
		case Crusher.GUI_ID:
			return new CrusherGui((CrusherTileEntity)te, new CrusherContainer(player.inventory, (CrusherTileEntity)te));
		case BlastFurnaceMultiBlockTileEntity.GUI_ID:
			BlastFurnaceMultiBlockTileEntity blastFurnaceMultiController = (BlastFurnaceMultiBlockTileEntity)((IMultiblockPart)te).getMultiblockController();
			return new BlastFurnaceGui(blastFurnaceMultiController, new BlastFurnaceMultiContainer(player.inventory, blastFurnaceMultiController));
		default:
			return null;
		}
	}
}
