package one.tlph.machinist.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import one.tlph.machinist.init.registries.Dimensions;
import one.tlph.machinist.world.MachinistTeleporter;

public class PortalBlock extends AbstractBlock {
    public PortalBlock() {
        super(Properties.create(Material.ROCK).notSolid().doesNotBlockMovement());
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(!entity.isPassenger() && !entity.isBeingRidden() && entity.canChangeDimension()) {
            if(entity.hasPortalCooldown()) {
                entity.setPortalCooldown();
            }
            else {
                World entityWorld = entity.world;
                if(entityWorld != null) {
                    MinecraftServer minecraftserver = entityWorld.getServer();
                    RegistryKey<World> destination = entity.world.getDimensionKey() == Dimensions.UNDER_NETHER_WORLD ? World.OVERWORLD : Dimensions.UNDER_NETHER_WORLD;
                    if(minecraftserver != null) {
                        ServerWorld destinationWorld = minecraftserver.getWorld(destination);
                        if(destinationWorld != null && minecraftserver.getAllowNether() && !entity.isPassenger()) {
                            entity.world.getProfiler().startSection("under_nether_portal");
                            entity.setPortalCooldown();
                            entity.changeDimension(destinationWorld, new MachinistTeleporter(pos));
                            entity.world.getProfiler().endSection();
                        }
                    }
                }
            }
        }
    }
}