package net.raphimc.netminecraft.packet.impl.status;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class S2CStatusResponsePacket implements IPacket {

    public String statusJson;

    public S2CStatusResponsePacket() {
    }

    public S2CStatusResponsePacket(final String statusJson) {
        this.statusJson = statusJson;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.statusJson = buf.readString(Short.MAX_VALUE);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.statusJson);
    }

}
