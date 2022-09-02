package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.netty.crypto.CryptUtil;
import net.raphimc.netminecraft.packet.PacketByteBuf;

import java.security.PublicKey;
import java.time.Instant;

public class C2SLoginHelloPacket1_19 extends C2SLoginHelloPacket1_7 {

    public Instant expiresAt;
    public PublicKey key;
    public byte[] keySignature;

    public C2SLoginHelloPacket1_19() {
    }

    public C2SLoginHelloPacket1_19(final String name) {
        super(name);
    }

    public C2SLoginHelloPacket1_19(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature) {
        super(name);
        this.expiresAt = expiresAt;
        this.key = key;
        this.keySignature = keySignature;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.name = buf.readString(16);
        if (buf.readBoolean()) {
            this.expiresAt = Instant.ofEpochMilli(buf.readLong());
            this.key = CryptUtil.decodeRsaPublicKey(buf.readByteArray(512));
            this.keySignature = buf.readByteArray(4096);
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        final boolean hasKeyData = this.expiresAt != null && this.key != null && this.keySignature != null;
        buf.writeString(this.name);
        buf.writeBoolean(hasKeyData);
        if (hasKeyData) {
            buf.writeLong(this.expiresAt.toEpochMilli());
            buf.writeByteArray(this.key.getEncoded());
            buf.writeByteArray(this.keySignature);
        }
    }

}
