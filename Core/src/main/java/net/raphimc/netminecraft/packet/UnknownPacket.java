package net.raphimc.netminecraft.packet;

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
    public void read(PacketByteBuf buf) {
        this.data = buf.readReadableBytes();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBytes(this.data);
    }

}
