package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class S2CLoginDisconnectPacket implements IPacket {

    public String reason;

    public S2CLoginDisconnectPacket() {
    }

    public S2CLoginDisconnectPacket(final String reason) {
        this.reason = reason;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.reason = PacketTypes.readString(byteBuf, 262144);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.reason);
    }

}
