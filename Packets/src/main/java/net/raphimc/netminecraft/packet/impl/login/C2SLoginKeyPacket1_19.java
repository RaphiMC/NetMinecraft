package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginKeyPacket1_19 extends C2SLoginKeyPacket1_8 {

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
    public void read(ByteBuf byteBuf) {
        this.encryptedSecretKey = PacketTypes.readByteArray(byteBuf);
        if (!byteBuf.readBoolean()) {
            this.salt = byteBuf.readLong();
            this.signature = PacketTypes.readByteArray(byteBuf);
        } else {
            this.encryptedNonce = PacketTypes.readByteArray(byteBuf);
        }
    }

    @Override
    public void write(ByteBuf byteBuf) {
        final boolean isSigned = this.salt != null && this.signature != null;
        PacketTypes.writeByteArray(byteBuf, this.encryptedSecretKey);
        byteBuf.writeBoolean(!isSigned);
        if (isSigned) {
            byteBuf.writeLong(this.salt);
            PacketTypes.writeByteArray(byteBuf, this.signature);
        } else {
            PacketTypes.writeByteArray(byteBuf, this.encryptedNonce);
        }
    }

}
