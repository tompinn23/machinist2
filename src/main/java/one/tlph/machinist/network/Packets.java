package one.tlph.machinist.network;

import one.tlph.machinist.Machinist;
import one.tlph.machinist.network.packets.EnergyNextModePacket;

public class Packets {
    public static void register() {
        Machinist.NETWORK.register(new EnergyNextModePacket());

    }
}
