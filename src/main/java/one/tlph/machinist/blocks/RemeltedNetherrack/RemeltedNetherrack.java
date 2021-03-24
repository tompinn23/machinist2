package one.tlph.machinist.blocks.RemeltedNetherrack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import one.tlph.machinist.blocks.AbstractBlock;
import one.tlph.machinist.blocks.PortalHeater.PortalHeaterTileEntity;
import one.tlph.machinist.init.registries.ModBlocks;

import javax.annotation.Nullable;

public class RemeltedNetherrack  extends AbstractBlock {

    public static final BooleanProperty PORTAL = BooleanProperty.create("portal");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public RemeltedNetherrack() {
        super(AbstractBlock.Properties.create(Material.ROCK));
        this.setDefaultState(this.getDefaultState().with(PORTAL, false).with(TOP, false).with(BOTTOM, false));
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PORTAL, TOP, BOTTOM);
        super.fillStateContainer(builder);
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(PORTAL);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(PORTAL))
            return new PortalRemeltedTileEntity();
        else
            return null;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return createPortalState(worldIn, currentPos, true);
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
//        if(!newState.get(PORTAL)) {
//            world.removeTileEntity(pos);
//        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createPortalState(context.getWorld(), context.getPos(), true);
    }

    private BlockState createPortalState(IWorld world, BlockPos currentPos, boolean notifyOther) {
        if(world.getBlockState(currentPos.up()).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(currentPos.up(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get()) {
            if(notifyOther)
                world.setBlockState(currentPos.up(2), world.getBlockState(currentPos.up(2)).with(TOP, true).with(PORTAL, true), 2);
            return this.getDefaultState().with(PORTAL, true).with(BOTTOM, true);
        }
        if(world.getBlockState(currentPos.down()).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(currentPos.down(2)).getBlock() == ModBlocks.REMELTED_NETHERRACK.get()) {
            if(notifyOther)
                world.setBlockState(currentPos.down(2), world.getBlockState(currentPos.down(2)).with(BOTTOM, true).with(PORTAL, true), 2);
            return this.getDefaultState().with(PORTAL, true).with(TOP, true);
        }
        return this.getDefaultState();
    }
}
