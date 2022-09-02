package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class C2SLoginCustomPayloadPacket implements IPacket {

    public int queryId;
    public PacketByteBuf response;

    public C2SLoginCustomPayloadPacket() {
    }

    public C2SLoginCustomPayloadPacket(final int queryId, final PacketByteBuf response) {
        this.queryId = queryId;
        this.response = response;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.queryId = buf.readVarInt();
        if (buf.readBoolean()) {
            final int length = buf.readableBytes();
            if (length < 0 || length > 1048576) {
                throw new IllegalStateException("Payload may not be larger than 1048576 bytes");
            }

            this.response = new PacketByteBuf(buf.readBytes(length));
        } else {
            this.response = null;
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.queryId);
        buf.writeBoolean(this.response != null);
        if (this.response != null) {
            buf.writeBytes(this.response.slice());
        }
    }

}
