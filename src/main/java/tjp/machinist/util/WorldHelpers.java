package tjp.machinist.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class WorldHelpers {

    public static BlockPos getMinCoord(BlockPos from, BlockPos to) {
        return new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(),to.getZ()));
    }

    public static BlockPos getMaxCoord(BlockPos from, BlockPos to) {
        return new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(),to.getZ()));
    }

    public static long getChunkXZHashFromBlock(BlockPos pos) {
        return new ChunkPos(pos).hashCode();
    }

    public static void notifyBlockUpdate(World world, BlockPos position, IBlockState oldState, IBlockState newState) {

        if (null == oldState)
            oldState = world.getBlockState(position);

        if (null == newState)
            newState = oldState;

        world.notifyBlockUpdate(position, oldState, newState, 3);
    }

}
