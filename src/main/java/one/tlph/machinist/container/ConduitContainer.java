package one.tlph.machinist.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import one.tlph.machinist.blocks.EnergyNet.EnergyConduitTileEntity;

import javax.annotation.Nullable;

public class ConduitContainer extends ContainerBase {

    public EnergyConduitTileEntity te;
    private Direction hitSide;

    public ConduitContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(type, id, inventory, getTile(inventory.player, buffer.readBlockPos()), Direction.values()[buffer.readInt()]);
    }

    private static EnergyConduitTileEntity getTile(PlayerEntity player, BlockPos readBlockPos) {
        return (EnergyConduitTileEntity) player.world.getTileEntity(readBlockPos);
    }

    protected ConduitContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, EnergyConduitTileEntity tileEntity, Direction hitSide) {
        super(type, id, inventory);
        this.te = tileEntity;
        this.hitSide = hitSide;
    }




}
