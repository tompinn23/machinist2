package one.tlph.machinist.network;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket<T> {
    void encode(T msg, PacketBuffer buffer);

    T decode(PacketBuffer buffer);

    void handle(T msg, Supplier<NetworkEvent.Context> ctx);
}
