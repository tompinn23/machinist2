package one.tlph.machinist.blocks.EnergyNet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import one.tlph.machinist.energy.Energy;
import one.tlph.machinist.energy.net.EnergyNetBase;
import one.tlph.machinist.energy.net.IEnergyNetPart;

import javax.annotation.Nullable;

public class EnergyConduit extends Block {

    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    private static final VoxelShape CABLE = makeCuboidShape(6.25, 6.25, 6.25, 9.75, 9.75, 9.75);
    private static final VoxelShape[] MULTIPART = new VoxelShape[]{makeCuboidShape(6.5, 6.5, 0, 9.5, 9.5, 7), makeCuboidShape(9.5, 6.5, 6.5, 16, 9.5, 9.5), makeCuboidShape(6.5, 6.5, 9.5, 9.5, 9.5, 16), makeCuboidShape(0, 6.5, 6.5, 6.5, 9.5, 9.5), makeCuboidShape(6.5, 9.5, 6.5, 9.5, 16, 9.5), makeCuboidShape(6.5, 0, 6.5, 9.5, 7, 9.5)};


    public EnergyConduit(Block.Properties properties) {
        super(properties);
        setDefaultState(getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createCableState(context.getWorld(), context.getPos());
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return createCableState(worldIn, currentPos);
    }

    private BlockState createCableState(IWorld world, BlockPos pos) {
        final BlockState state = getDefaultState();
        boolean[] north = canAttach(state, world, pos, Direction.NORTH);
        boolean[] south = canAttach(state, world, pos, Direction.SOUTH);
        boolean[] west = canAttach(state, world, pos, Direction.WEST);
        boolean[] east = canAttach(state, world, pos, Direction.EAST);
        boolean[] up = canAttach(state, world, pos, Direction.UP);
        boolean[] down = canAttach(state, world, pos, Direction.DOWN);
        return state.with(NORTH, north[0] && !north[1]).with(SOUTH, south[0] && !south[1]).with(WEST, west[0] && !west[1]).with(EAST, east[0] && !east[1]).with(UP, up[0] && !up[1]).with(DOWN, down[0] && !down[1]);
    }

    private boolean[] canAttach(BlockState state, IWorld world, BlockPos pos, Direction direction) {
        return new boolean[]{world.getBlockState(pos.offset(direction)).getBlock() == this || canConnectEnergy(world, pos, direction), canConnectEnergy(world, pos, direction)};
    }


    public boolean canConnectEnergy(IBlockReader world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos.offset(direction));
        return !(tile instanceof EnergyConduitTileEntity) && Energy.isPresent(tile, direction);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape voxelShape = CABLE;
        if (state.get(NORTH) || canConnectEnergy(world, pos, Direction.NORTH))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[0]);
        if (state.get(EAST) || canConnectEnergy(world, pos, Direction.EAST))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[1]);
        if (state.get(SOUTH) || canConnectEnergy(world, pos, Direction.SOUTH))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[2]);
        if (state.get(WEST) || canConnectEnergy(world, pos, Direction.WEST))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[3]);
        if (state.get(UP) || canConnectEnergy(world, pos, Direction.UP))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[4]);
        if (state.get(DOWN) || canConnectEnergy(world, pos, Direction.DOWN))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[5]);
        return voxelShape;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
        super.fillStateContainer(builder);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyConduitTileEntity();
    }


    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof IEnergyNetPart) {
            Direction dir = Direction.getFacingFromVector(neighbor.getX() - pos.getX(), neighbor.getY() - pos.getY(), neighbor.getZ() - pos.getZ());
            EnergyNetBase controller = ((IEnergyNetPart) te).getGridController();
            if(controller != null) {
                controller.onNeighbourBlockChanged(pos, dir);
            }
        }

    }


}
