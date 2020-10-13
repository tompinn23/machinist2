package one.tlph.machinist.blocks.smelter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
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
import net.minecraftforge.items.IItemHandler;
import one.tlph.machinist.tileentity.SmelterTileEntity;

import javax.annotation.Nonnull;

public class Smelter extends HorizontalBlock {
	
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    
	public static final int GUI_ID = 1;

    public Smelter() {
    	super(Block.Properties.create(Material.ROCK));
    	this.setDefaultState(getDefaultState().with(HORIZONTAL_FACING,  Direction.NORTH));
    }


	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}


	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
	}

	@Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(HORIZONTAL_FACING, ACTIVE);
	}


    
    @Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    	return new SmelterTileEntity();
	}



	@Nonnull
	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof SmelterTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (SmelterTileEntity)te, pos);
			}
		}
		return ActionResultType.SUCCESS;
	}



    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof SmelterTileEntity) {
                IItemHandler inventory = ((SmelterTileEntity) te).inventory;
                for (int i = 0; i < inventory.getSlots(); i++) {
                        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    
}

