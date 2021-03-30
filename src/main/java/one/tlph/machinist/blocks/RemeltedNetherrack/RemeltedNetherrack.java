package one.tlph.machinist.blocks.RemeltedNetherrack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.blocks.AbstractBlock;
import one.tlph.machinist.blocks.PortalHeater.PortalHeaterTileEntity;
import one.tlph.machinist.init.registries.ModBlocks;

import javax.annotation.Nullable;

public class RemeltedNetherrack  extends AbstractBlock {

    public static final BooleanProperty PORTAL = BooleanProperty.create("portal");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public RemeltedNetherrack() {
        super(AbstractBlock.Properties.create(Material.ROCK));
        this.setDefaultState(this.getDefaultState().with(PORTAL, false).with(TOP, false).with(BOTTOM, false).with(ACTIVE, false));
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PORTAL, TOP, BOTTOM, ACTIVE);
        super.fillStateContainer(builder);
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(PORTAL);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(PORTAL) && state.get(BOTTOM))
            return new PortalRemeltedTileEntity();
        else
            return null;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return createPortalState((World) worldIn, currentPos, false);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        worldIn.setBlockState(pos, createPortalState(worldIn, pos, false));
    }


    @Override
    public boolean isVariableOpacity() {
        return true;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        if(state.get(ACTIVE)) {
            return (side == Direction.DOWN && state.get(TOP)) || (side == Direction.UP && state.get(BOTTOM));
        }
        return super.isSideInvisible(state, adjacentBlockState, side);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

        if(newState.getBlock() != state.getBlock()) {
            if(state.get(BOTTOM)) {
                if(world.getBlockState(pos.up(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get())
                    world.setBlockState(pos.up(2), world.getBlockState(pos.up(2)).with(TOP, false).with(PORTAL, false), 2);
            }
            if(state.get(TOP)) {
                if(world.getBlockState(pos.down(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get())
                    world.setBlockState(pos.down(2), world.getBlockState(pos.down(2)).with(BOTTOM, false).with(PORTAL, false), 2);
            }
            world.removeTileEntity(pos);
        }
        if(newState.getBlock() == ModBlocks.REMELTED_NETHERRACK.get() && !newState.get(PORTAL)) {
            world.removeTileEntity(pos);
        }
    }

    public static final  VoxelShape CollisionDOWN = makeCuboidShape(0, 0, 0, 16, 5, 16);
    public static final  VoxelShape CollisionUP = makeCuboidShape(0, 11, 0, 16, 16, 16);


    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if(state.get(ACTIVE)) {
            return state.get(BOTTOM) ?  CollisionDOWN : CollisionUP;
        }
        return super.getCollisionShape(state, worldIn, pos, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createPortalState(context.getWorld(), context.getPos(), true);
    }

    private BlockState createPortalState(World world, BlockPos currentPos, boolean notifyOther) {
        if((world.getBlockState(currentPos.up()).getBlock() == Blocks.GOLD_BLOCK)
                && world.getBlockState(currentPos.up(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get()) {
            if(notifyOther)
                world.setBlockState(currentPos.up(2), world.getBlockState(currentPos.up(2)).with(TOP, true).with(PORTAL, true), 2);
            return this.getDefaultState().with(PORTAL, true).with(BOTTOM, true);
        }
        else if((world.getBlockState(currentPos.up()).getBlock() == ModBlocks.PORTAL_BLOCK.get())
                && world.getBlockState(currentPos.up(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get()) {
            if(notifyOther)
                world.setBlockState(currentPos.up(2), world.getBlockState(currentPos.up(2)).with(TOP, true).with(PORTAL, true).with(ACTIVE, true));
            return this.getDefaultState().with(PORTAL, true).with(BOTTOM, true).with(ACTIVE, true);
        }
        if((world.getBlockState(currentPos.down()).getBlock() == Blocks.GOLD_BLOCK)
                && world.getBlockState(currentPos.down(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get()) {
            if(notifyOther)
                world.setBlockState(currentPos.down(2), world.getBlockState(currentPos.down(2)).with(BOTTOM, true).with(PORTAL, true));
            return this.getDefaultState().with(PORTAL, true).with(TOP, true);
        }
        else if((world.getBlockState(currentPos.down()).getBlock() == ModBlocks.PORTAL_BLOCK.get())
                && world.getBlockState(currentPos.down(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get()) {
            if(notifyOther)
                world.setBlockState(currentPos.down(2), world.getBlockState(currentPos.down(2)).with(BOTTOM, true).with(PORTAL, true).with(ACTIVE, true));
            return this.getDefaultState().with(PORTAL, true).with(TOP, true).with(ACTIVE,true);
        }
        return this.getDefaultState();
    }
}
