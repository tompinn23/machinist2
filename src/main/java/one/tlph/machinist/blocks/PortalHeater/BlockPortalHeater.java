package one.tlph.machinist.blocks.PortalHeater;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import one.tlph.machinist.blocks.AbstractHorizontalBlock;
import one.tlph.machinist.blocks.EnergyNet.EnergyConduitTileEntity;
import one.tlph.machinist.blocks.RemeltedNetherrack.PortalRemeltedTileEntity;
import one.tlph.machinist.blocks.RemeltedNetherrack.RemeltedNetherrack;
import one.tlph.machinist.init.registries.ModBlocks;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public class BlockPortalHeater extends AbstractHorizontalBlock {


    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final VoxelShape CABLE = makeCuboidShape(1, 0, 1, 15, 14, 15);

    public BlockPortalHeater() {
        super(AbstractBlock.Properties.create(Material.ROCK));
        this.setDefaultState(this.getDefaultState().with(ACTIVE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
        super.fillStateContainer(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createHeaterState(context.getWorld(), context.getPos(), context.getPlacementHorizontalFacing().getOpposite());
    }

    private BlockState createHeaterState(World world, BlockPos pos, Direction direction) {
        BlockPos bottomPortal = pos.offset(direction, 2).down();
        if(world.getBlockState(bottomPortal).getBlock() == ModBlocks.REMELTED_NETHERRACK.get())
            return this.getDefaultState().with(FACING, direction).with(ACTIVE, world.getBlockState(bottomPortal).get(RemeltedNetherrack.PORTAL));
        else {
            return this.getDefaultState().with(FACING, direction);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return CABLE;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return CABLE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PortalHeaterTileEntity();
    }
}
