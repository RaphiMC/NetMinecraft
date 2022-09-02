package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.PacketByteBuf;

public class C2SLoginKeyPacket1_19 extends C2SLoginKeyPacket1_7 {

    public Long salt;
    public byte[] signature;

    public C2SLoginKeyPacket1_19() {
    }

    public C2SLoginKeyPacket1_19(final byte[] encryptedSecretKey, final byte[] encryptedNonce) {
        super(encryptedSecretKey, encryptedNonce);
    }

    public C2SLoginKeyPacket1_19(final byte[] encryptedSecretKey, final long salt, final byte[] signature) {
        super(encryptedSecretKey, null);
        this.salt = salt;
        this.signature = signature;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.encryptedSecretKey = buf.readByteArray();
        if (!buf.readBoolean()) {
            this.salt = buf.readLong();
            this.signature = buf.readByteArray();
        } else {
            this.encryptedNonce = buf.readByteArray();
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        final boolean isSigned = this.salt != null && this.signature != null;
        buf.writeByteArray(this.encryptedSecretKey);
        buf.writeBoolean(!isSigned);
        if (isSigned) {
            buf.writeLong(this.salt);
            buf.writeByteArray(this.signature);
        } else {
            buf.writeByteArray(this.encryptedNonce);
        }
    }

}
