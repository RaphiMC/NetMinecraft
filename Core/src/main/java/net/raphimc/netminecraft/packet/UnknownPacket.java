package net.raphimc.netminecraft.packet;

import io.netty.buffer.ByteBuf;

public class UnknownPacket implements IPacket {

    public int packetId;
    public byte[] data;

    public UnknownPacket() {
    }

    public UnknownPacket(final int packetId, final byte[] data) {
        this.packetId = packetId;
        this.data = data;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.data = PacketTypes.readReadableBytes(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.data);
    }

}
