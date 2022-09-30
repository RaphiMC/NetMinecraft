package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class S2CLoginKeyPacket1_7 implements IPacket {

    public String serverId;
    public byte[] publicKey;
    public byte[] nonce;

    public S2CLoginKeyPacket1_7() {
    }

    public S2CLoginKeyPacket1_7(final String serverId, final byte[] publicKey, final byte[] nonce) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.nonce = nonce;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.serverId = PacketTypes.readString(byteBuf, 20);
        this.publicKey = PacketTypes.readShortByteArray(byteBuf);
        this.nonce = PacketTypes.readShortByteArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.serverId);
        PacketTypes.writeShortByteArray(byteBuf, this.publicKey);
        PacketTypes.writeShortByteArray(byteBuf, this.nonce);
    }

}
