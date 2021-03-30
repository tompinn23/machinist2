package one.tlph.machinist.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.Mod;
import one.tlph.machinist.blocks.RemeltedNetherrack.PortalRemeltedTileEntity;
import one.tlph.machinist.init.registries.Dimensions;
import one.tlph.machinist.init.registries.ModBlocks;

import java.util.function.Function;

import static one.tlph.machinist.blocks.RemeltedNetherrack.RemeltedNetherrack.*;

public class MachinistTeleporter implements ITeleporter {

    protected final BlockPos pos;

    public MachinistTeleporter(BlockPos pos) {
        this.pos = pos;
    }


    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
       Entity e = repositionEntity.apply(false);
        if (!(e instanceof ServerPlayerEntity)) {
            return e;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) e;
        Chunk chunk = (Chunk) destWorld.getChunk(pos);
        BlockPos teleporterPos = findPortalInChunk(chunk);

        if (teleporterPos == null) {
            if (destWorld.getDimensionKey() ==Dimensions.UNDER_NETHER_WORLD) {
                teleporterPos = placeTeleporterUnder(destWorld, chunk);
            } else {
                teleporterPos = placeTeleporterOverworld(destWorld, chunk);
            }
        }
        if (teleporterPos == null) {
            return e;
        }

        //player.giveExperienceLevels(0);
        player.teleport(destWorld, teleporterPos.getX() + 0.5D, teleporterPos.getY() + 1D, teleporterPos.getZ() + 0.5D, yaw, 0);
        return e;
    }

    private BlockPos placeTeleporterUnder(ServerWorld destWorld, Chunk chunk) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int y = 110; y > 0; y--) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    pos.setPos(x, y, z);
                    if (chunk.getBlockState(pos).getBlock() == ModBlocks.UNDER_NETHERRACK.get() && chunk.getBlockState(pos.up(1)).isAir() && chunk.getBlockState(pos.up(2)).isAir() && chunk.getBlockState(pos.up(3)).isAir()) {
                        BlockPos absolutePos = chunk.getPos().asBlockPos().add(pos.getX(), pos.getY(), pos.getZ()).up();
                        destWorld.setBlockState(absolutePos, ModBlocks.REMELTED_NETHERRACK.get().getDefaultState().with(BOTTOM, true).with(PORTAL, true).with(ACTIVE, true));
                        destWorld.setBlockState(absolutePos.up(), ModBlocks.PORTAL_BLOCK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.up(2), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState().with(TOP, true).with(PORTAL, true).with(ACTIVE, true));
                        destWorld.setBlockState(absolutePos.down(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().north(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().east(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().south(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().west(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().north().east(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().east().east(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().south().east(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                        destWorld.setBlockState(absolutePos.down().west().east(), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());


                        //destWorld.setBlockState(pos.up(3), ModBlocks.COPPER_ORE.get().getDefaultState());
                        return absolutePos;
                    }
                }
            }
        }

        for (int y = 60; y > 0; y--) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    pos.setPos(x, y, z);
                    if (isAirOrStone(chunk, pos) && isAirOrStone(chunk, pos.up(1)) && isAirOrStone(chunk, pos.up(2))) {
                        BlockPos absolutePos = chunk.getPos().asBlockPos().add(pos.getX(), pos.getY(), pos.getZ());
                        boolean pred = true;
                        for(Direction direction : Direction.values()) {
                            if(direction == Direction.DOWN) {
                                continue;
                            }
                            pred = isReplaceable(destWorld, absolutePos.offset(direction, 1)) && isReplaceable(destWorld, absolutePos.offset(direction, 2));
                            if(pred == false)
                                break;
                        }
                        if (pred) {

                            destWorld.setBlockState(absolutePos, ModBlocks.REMELTED_NETHERRACK.get().getDefaultState().with(BOTTOM, true).with(PORTAL, true).with(ACTIVE, true));
                            destWorld.setBlockState(absolutePos.up(), ModBlocks.PORTAL_BLOCK.get().getDefaultState());
                            destWorld.setBlockState(absolutePos.up(2), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState().with(TOP, true).with(PORTAL, true).with(ACTIVE, true));
                            for(Direction dir : Direction.values()) {
                                if(dir == Direction.UP) {
                                    continue;
                                }
                                destWorld.setBlockState(absolutePos.offset(dir), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState());
                            }
                            return absolutePos;
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean isAirOrStone(Chunk chunk, BlockPos pos) {
        BlockState state = chunk.getBlockState(pos);
        return state.getBlock().equals(ModBlocks.UNDER_NETHERRACK) || state.isAir();
    }

    private boolean isReplaceable(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock().equals(Blocks.STONE) ||
                state.getBlock().equals(Blocks.GRANITE) ||
                state.getBlock().equals(Blocks.ANDESITE) ||
                state.getBlock().equals(Blocks.DIORITE) ||
                state.getBlock().equals(Blocks.DIRT) ||
                state.getBlock().equals(Blocks.GRAVEL) ||
                state.getBlock().equals(Blocks.LAVA) ||
                state.isAir();
    }

    private BlockPos placeTeleporterOverworld(ServerWorld destWorld, Chunk chunk) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int y = 0; y < 255; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    pos.setPos(x, y, z);
                    if (chunk.getBlockState(pos).isAir() && chunk.getBlockState(pos.up(1)).isAir() && chunk.getBlockState(pos.up(2)).isAir()) {
                        BlockPos absolutePos = chunk.getPos().asBlockPos().add(pos.getX(), pos.getY(), pos.getZ());
                        destWorld.setBlockState(pos, ModBlocks.REMELTED_NETHERRACK.get().getDefaultState().with(BOTTOM, true).with(PORTAL, true).with(ACTIVE, true));
                        destWorld.setBlockState(pos.up(), ModBlocks.PORTAL_BLOCK.get().getDefaultState());
                        destWorld.setBlockState(pos.up(2), ModBlocks.REMELTED_NETHERRACK.get().getDefaultState().with(TOP, true).with(PORTAL, true).with(ACTIVE, true));
                        return absolutePos;
                    }
                }
            }
        }
        return null;
    }

    private BlockPos findPortalInChunk(Chunk chunk) {
        for (TileEntity tile : chunk.getTileEntityMap().values()) {
            if (tile instanceof PortalRemeltedTileEntity) {
                BlockPos pos = tile.getPos();
                return pos;
            }
        }
        return null;
    }
}
