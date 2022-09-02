package net.raphimc.netminecraft.packet.impl.handshake;

import net.raphimc.netminecraft.constants.ConnectionState;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

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
    public void read(PacketByteBuf buf) {
        this.protocolVersion = buf.readVarInt();
        this.address = buf.readString(255);
        this.port = buf.readUnsignedShort();
        this.intendedState = ConnectionState.getById(buf.readVarInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.protocolVersion);
        buf.writeString(this.address);
        buf.writeShort(this.port);
        buf.writeVarInt(this.intendedState.getId());
    }

}
