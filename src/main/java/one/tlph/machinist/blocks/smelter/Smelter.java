package one.tlph.machinist.blocks.smelter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import one.tlph.machinist.blocks.AbstractHorizontalBlock;
import one.tlph.machinist.blocks.crusher.CrusherTileEntity;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.tileentity.AbstractTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Smelter extends AbstractHorizontalBlock {
	
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public Smelter() {
    	super(Block.Properties.create(Material.ROCK));
    }

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

    @Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    	return new SmelterTileEntity();
	}

	@Nullable
	@Override
	public Container getContainer(int id, PlayerInventory playerInventory, AbstractTileEntity te, BlockRayTraceResult result) {
		if (te instanceof SmelterTileEntity) {
			return new SmelterContainer(id, playerInventory, (SmelterTileEntity) te);
		}
		return null;
	}




    
}

