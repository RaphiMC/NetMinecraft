package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

public class S2CLoginKeyPacket1_8 extends S2CLoginKeyPacket1_7 {

    public S2CLoginKeyPacket1_8() {
    }

    public S2CLoginKeyPacket1_8(final String serverId, final byte[] publicKey, final byte[] nonce) {
        super(serverId, publicKey, nonce);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.serverId = PacketTypes.readString(byteBuf, 20);
        this.publicKey = PacketTypes.readByteArray(byteBuf);
        this.nonce = PacketTypes.readByteArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.serverId);
        PacketTypes.writeByteArray(byteBuf, this.publicKey);
        PacketTypes.writeByteArray(byteBuf, this.nonce);
    }

}
