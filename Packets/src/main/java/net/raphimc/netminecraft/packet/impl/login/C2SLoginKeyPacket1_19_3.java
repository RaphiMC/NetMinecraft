package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginKeyPacket1_19_3 extends C2SLoginKeyPacket1_19 {

    public C2SLoginKeyPacket1_19_3() {
    }

    public C2SLoginKeyPacket1_19_3(final byte[] encryptedSecretKey, final byte[] encryptedNonce) {
        super(encryptedSecretKey, encryptedNonce);
    }

    public C2SLoginKeyPacket1_19_3(final byte[] encryptedSecretKey, final long salt, final byte[] signature) {
        super(encryptedSecretKey, salt, signature);
    }

    public C2SLoginKeyPacket1_19_3(final byte[] encryptedSecretKey, final byte[] encryptedNonce, final long salt, final byte[] signature) {
        super(encryptedSecretKey, salt, signature);
        this.encryptedNonce = encryptedNonce;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.encryptedSecretKey = PacketTypes.readByteArray(byteBuf);
        this.encryptedNonce = PacketTypes.readByteArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeByteArray(byteBuf, this.encryptedSecretKey);
        PacketTypes.writeByteArray(byteBuf, this.encryptedNonce);
    }

}
