package one.tlph.machinist.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.MultiblockTileEntityBase;
import one.tlph.machinist.init.ModTileEntityTypes;
import one.tlph.machinist.tileentity.CableTileEntity;
import one.tlph.machinist.tileentity.EnergyNetTileEntity;

public class Cable extends Block {


    public Cable(Properties properties) {
        super(properties);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof MultiblockTileEntityBase) {
            MultiblockControllerBase controller = ((MultiblockTileEntityBase) te).getMultiblockController();
            if(controller instanceof EnergyNetTileEntity) {
                Direction side = Direction.getFacingFromVector(neighbor.getX() - pos.getX(), neighbor.getY() - pos.getY(), neighbor.getZ() - pos.getZ());
                ((EnergyNetTileEntity) controller).onNeighbourTileChanged(pos, side);
            }
        }
    }

    @Override
    @Deprecated
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block neighbourBlock, BlockPos neighbourPos, boolean isMoving) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof MultiblockTileEntityBase) {
            MultiblockControllerBase controller = ((MultiblockTileEntityBase) te).getMultiblockController();
            if(controller instanceof EnergyNetTileEntity) {
                Direction side = Direction.getFacingFromVector(neighbourPos.getX() - pos.getX(), neighbourPos.getY() - pos.getY(), neighbourPos.getZ() - pos.getZ());
                ((EnergyNetTileEntity) controller).onNeighbourBlockChanged(pos, side);
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CableTileEntity();
    }
}
