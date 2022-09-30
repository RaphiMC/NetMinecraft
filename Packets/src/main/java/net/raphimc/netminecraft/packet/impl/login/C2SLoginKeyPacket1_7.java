package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginKeyPacket1_7 implements IPacket {

    public byte[] encryptedSecretKey;
    public byte[] encryptedNonce;

    public C2SLoginKeyPacket1_7() {
    }

    public C2SLoginKeyPacket1_7(final byte[] encryptedSecretKey, final byte[] encryptedNonce) {
        this.encryptedSecretKey = encryptedSecretKey;
        this.encryptedNonce = encryptedNonce;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.encryptedSecretKey = PacketTypes.readShortByteArray(byteBuf);
        this.encryptedNonce = PacketTypes.readShortByteArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeShortByteArray(byteBuf, this.encryptedSecretKey);
        PacketTypes.writeShortByteArray(byteBuf, this.encryptedNonce);
    }

}
