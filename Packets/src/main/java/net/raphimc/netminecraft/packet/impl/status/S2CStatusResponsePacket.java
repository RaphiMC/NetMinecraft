package net.raphimc.netminecraft.packet.impl.status;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class S2CStatusResponsePacket implements IPacket {

    public String statusJson;

    public S2CStatusResponsePacket() {
    }

    public S2CStatusResponsePacket(final String statusJson) {
        this.statusJson = statusJson;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.statusJson = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.statusJson);
    }

}
