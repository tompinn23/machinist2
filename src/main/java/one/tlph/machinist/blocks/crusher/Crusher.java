package one.tlph.machinist.blocks.crusher;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import one.tlph.machinist.tileentity.CrusherTileEntity;

public class Crusher extends HorizontalBlock {
	
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final int GUI_ID = 2;


    public Crusher() {
    	super(Block.Properties.create(Material.ROCK));
    }

    
    
    @Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
    	builder.add(HORIZONTAL_FACING, ACTIVE);
	}



	@Override
    public boolean hasTileEntity() {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrusherTileEntity();
    }

    @Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
            if(!worldIn.isRemote) {
                TileEntity te = worldIn.getTileEntity(pos);
                if(te instanceof INamedContainerProvider) {
                    NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)te, pos);
                }
                else {
                    throw new IllegalStateException("Our named container provider is missing!");
                }
                return true;
            }
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof CrusherTileEntity) {
                IItemHandler inventory = ((CrusherTileEntity) te).inventory;
                for (int i = 0; i < inventory.getSlots(); i++) {
                        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
