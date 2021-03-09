package one.tlph.machinist.blocks.EnergyNet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
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
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import one.tlph.machinist.blocks.AbstractBlock;
import one.tlph.machinist.energy.Energy;
import one.tlph.machinist.energy.net.EnergyNetBase;
import one.tlph.machinist.energy.net.IEnergyNetPart;
import one.tlph.machinist.tileentity.AbstractTileEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class EnergyConduit extends AbstractBlock {

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

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            Optional<Direction> side = getHitSide(hit.getHitVec(), pos);
            if(!side.isPresent())
                return ActionResultType.PASS;
            if(te instanceof EnergyConduitTileEntity) {
                INamedContainerProvider provider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new ItemStack(EnergyConduit.this).getDisplayName();
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int i, PlayerInventory inventory, PlayerEntity player) {
                        return getContainer(i, inventory, (EnergyConduitTileEntity) te, hit);
                    }
                };
                Container container = provider.createMenu(0, player.inventory, player);
                if(container != null) {
                    if(player instanceof ServerPlayerEntity) {
                        NetworkHooks.openGui((ServerPlayerEntity)player, provider, packetBuffer -> {
                            packetBuffer.writeBlockPos(pos);
                            additionalGuiData(packetBuffer, state,worldIn,pos,player, handIn, hit);
                        });
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void additionalGuiData(PacketBuffer buffer, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        getHitSide(result.getHitVec(), pos).ifPresent(side -> buffer.writeInt(side.getIndex()));
        super.additionalGuiData(buffer, state, world, pos, player, hand, result);
    }

    public static Optional<Direction> getHitSide(Vector3d hit, BlockPos pos) {
        double x = hit.x - pos.getX();
        double y = hit.y - pos.getY();
        double z = hit.z - pos.getZ();
        if (x > 0.0D && x < 0.4D) return Optional.of(Direction.WEST);
        else if (x > 0.6D && x < 1.0D) return Optional.of(Direction.EAST);
        else if (z > 0.0D && z < 0.4D) return Optional.of(Direction.NORTH);
        else if (z > 0.6D && z < 1.0D) return Optional.of(Direction.SOUTH);
        else if (y > 0.6D && y < 1.0D) return Optional.of(Direction.UP);
        else if (y > 0.0D && y < 0.4D) return Optional.of(Direction.DOWN);
        return Optional.empty();
    }


    @Nullable
    public Container getContainer(int id, PlayerInventory playerInventory, EnergyConduitTileEntity tileEntity, BlockRayTraceResult result) {
        if(tileEntity instanceof EnergyConduitTileEntity) {
            //return new EnergyConduitContainer();
        }
        return null;
    }
}
