package one.tlph.machinist.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import one.tlph.machinist.network.IPacket;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;

import java.util.function.Supplier;

public class EnergyNextModePacket implements IPacket<EnergyNextModePacket> {
    private int side;
    private BlockPos pos;

    public EnergyNextModePacket() {
        this(0, BlockPos.ZERO);
    }

    public EnergyNextModePacket(int side, BlockPos pos) {
        this.side = side;
        this.pos = pos;
    }

    @Override
    public void encode(EnergyNextModePacket msg, PacketBuffer buffer) {
        buffer.writeInt(msg.side);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public EnergyNextModePacket decode(PacketBuffer buffer) {
        return new EnergyNextModePacket(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void handle(EnergyNextModePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player != null) {
                if (player.world.isBlockPresent(msg.pos)) {
                    TileEntity entity = player.world.getTileEntity(msg.pos);
                    if(entity instanceof AbstractPoweredTileEntity) {
                        AbstractPoweredTileEntity te = (AbstractPoweredTileEntity)entity;
                        te.getSidedConfig().next(msg.side > 5 ? null : Direction.values()[msg.side]);
                        if(msg.side > 5) {
                            te.getSidedConfig().setSides(te.getSidedConfig().get(null));
                        }
                        te.sync();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
