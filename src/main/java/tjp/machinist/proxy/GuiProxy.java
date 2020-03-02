package tjp.machinist.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import tjp.machinist.api.multiblock.IMultiblockPart;
import tjp.machinist.blocks.crusher.Crusher;
import tjp.machinist.blocks.smelter.Smelter;
import tjp.machinist.container.BlastFurnaceMultiContainer;
import tjp.machinist.container.CrusherContainer;
import tjp.machinist.container.SmelterContainer;
import tjp.machinist.gui.BlastFurnaceGui;
import tjp.machinist.gui.CrusherGui;
import tjp.machinist.gui.SmelterGui;
import tjp.machinist.tileentity.BlastFurnaceMultiControllerTileEntity;
import tjp.machinist.tileentity.CrusherTileEntity;
import tjp.machinist.tileentity.SmelterTileEntity;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		switch(ID) {
			case Smelter.GUI_ID:
				return new SmelterContainer(player.inventory, (SmelterTileEntity)te);
			case Crusher.GUI_ID:
				return new CrusherContainer(player.inventory, (CrusherTileEntity)te);
			case BlastFurnaceMultiControllerTileEntity.GUI_ID:
				return new BlastFurnaceMultiContainer(player.inventory, (BlastFurnaceMultiControllerTileEntity)((IMultiblockPart)te).getMultiblockController());
			default:
				return null;
		}
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		switch(ID) {
		case Smelter.GUI_ID:
			SmelterTileEntity smelterTileEntity = (SmelterTileEntity)te;
			return new SmelterGui(smelterTileEntity, new SmelterContainer(player.inventory, smelterTileEntity));
		case Crusher.GUI_ID:
			return new CrusherGui((CrusherTileEntity)te, new CrusherContainer(player.inventory, (CrusherTileEntity)te));
		case BlastFurnaceMultiControllerTileEntity.GUI_ID:
			BlastFurnaceMultiControllerTileEntity blastFurnaceMultiController = (BlastFurnaceMultiControllerTileEntity)((IMultiblockPart)te).getMultiblockController();
			return new BlastFurnaceGui(blastFurnaceMultiController, new BlastFurnaceMultiContainer(player.inventory, blastFurnaceMultiController));
		default:
			return null;
		}
	}
}
