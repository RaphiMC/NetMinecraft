package net.raphimc.netminecraft.packet.impl.handshake;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.constants.ConnectionState;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SHandshakePacket implements IPacket {

    public int protocolVersion;
    public String address;
    public int port;
    public ConnectionState intendedState;

    public C2SHandshakePacket() {
    }

    public C2SHandshakePacket(final int protocolVersion, final String address, final int port, final ConnectionState intendedState) {
        this.protocolVersion = protocolVersion;
        this.address = address;
        this.port = port;
        this.intendedState = intendedState;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.protocolVersion = PacketTypes.readVarInt(byteBuf);
        this.address = PacketTypes.readString(byteBuf, 255);
        this.port = byteBuf.readUnsignedShort();
        this.intendedState = ConnectionState.getById(PacketTypes.readVarInt(byteBuf));
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeVarInt(byteBuf, this.protocolVersion);
        PacketTypes.writeString(byteBuf, this.address);
        byteBuf.writeShort(this.port);
        PacketTypes.writeVarInt(byteBuf, this.intendedState.getId());
    }

}
