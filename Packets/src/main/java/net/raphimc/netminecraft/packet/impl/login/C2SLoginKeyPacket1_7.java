package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

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
    public void read(PacketByteBuf buf) {
        this.encryptedSecretKey = buf.readByteArray();
        this.encryptedNonce = buf.readByteArray();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByteArray(this.encryptedSecretKey);
        buf.writeByteArray(this.encryptedNonce);
    }

}
