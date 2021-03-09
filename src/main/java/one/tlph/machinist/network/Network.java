package one.tlph.machinist.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network {
    private final ResourceLocation location;
    private final SimpleChannel channel;
    private int id;
    public Network(String id) {
        this.location = new ResourceLocation(id, "main");
        this.channel = NetworkRegistry.ChannelBuilder.named(this.location)
                .clientAcceptedVersions("1"::equals)
                .serverAcceptedVersions("1"::equals)
                .networkProtocolVersion(() -> "1")
                .simpleChannel();
    }

    @SuppressWarnings("unchecked")
    public <T> void register(IPacket<T> packet) {
        this.channel.registerMessage(this.id++, (Class<T>)packet.getClass(), packet::encode, packet::decode, packet::handle);
    }

    @OnlyIn(Dist.CLIENT)
    public <T> void toServer(T msg) {
        this.channel.sendToServer(msg);
    }

    public <T> void toAll(T msg) {
        this.channel.send(PacketDistributor.ALL.noArg(), msg);
    }

    public <T> void toClient(T msg, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            this.channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
        }
    }
}
