package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginCustomPayloadPacket implements IPacket {

    public int queryId;
    public byte[] response;

    public C2SLoginCustomPayloadPacket() {
    }

    public C2SLoginCustomPayloadPacket(final int queryId, final byte[] response) {
        this.queryId = queryId;
        this.response = response;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.queryId = PacketTypes.readVarInt(byteBuf);
        if (byteBuf.readBoolean()) {
            final int length = byteBuf.readableBytes();
            if (length < 0 || length > 1048576) {
                throw new IllegalStateException("Payload may not be larger than 1048576 bytes");
            }

            this.response = new byte[length];
            byteBuf.readBytes(this.response);
        } else {
            this.response = null;
        }
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeVarInt(byteBuf, this.queryId);
        byteBuf.writeBoolean(this.response != null);
        if (this.response != null) {
            byteBuf.writeBytes(this.response);
        }
    }

}
