package tjp.engineering.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import tjp.engineering.blocks.smelter.Smelter;
import tjp.engineering.blocks.smelter.SmelterContainer;
import tjp.engineering.blocks.smelter.SmelterGui;
import tjp.engineering.blocks.smelter.SmelterTileEntity;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		switch(ID) {
			case Smelter.GUI_ID:
				return new SmelterContainer(player.inventory, (SmelterTileEntity)te);	
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
		default:
			return null;
		}
	}
}
