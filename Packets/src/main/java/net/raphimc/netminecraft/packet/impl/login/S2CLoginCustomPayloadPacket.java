package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class S2CLoginCustomPayloadPacket implements IPacket {

    public int queryId;
    public String channel;
    public PacketByteBuf payload;

    public S2CLoginCustomPayloadPacket() {
    }

    public S2CLoginCustomPayloadPacket(final int queryId, final String channel, final PacketByteBuf payload) {
        this.queryId = queryId;
        this.channel = channel;
        this.payload = payload;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.queryId = buf.readVarInt();
        this.channel = buf.readString(Short.MAX_VALUE);
        final int length = buf.readableBytes();
        if (length < 0 || length > 1048576) {
            throw new IllegalStateException("Payload may not be larger than 1048576 bytes");
        }

        this.payload = new PacketByteBuf(buf.readBytes(length));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.queryId);
        buf.writeString(this.channel);
        buf.writeBytes(this.payload.slice());
    }

}
