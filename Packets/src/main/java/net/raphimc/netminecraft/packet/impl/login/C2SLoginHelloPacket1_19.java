package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.netty.crypto.CryptUtil;
import net.raphimc.netminecraft.packet.PacketTypes;

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
    public void read(ByteBuf byteBuf) {
        this.name = PacketTypes.readString(byteBuf, 16);
        if (byteBuf.readBoolean()) {
            this.expiresAt = Instant.ofEpochMilli(byteBuf.readLong());
            this.key = CryptUtil.decodeRsaPublicKey(PacketTypes.readByteArray(byteBuf, 512));
            this.keySignature = PacketTypes.readByteArray(byteBuf, 4096);
        }
    }

    @Override
    public void write(ByteBuf byteBuf) {
        final boolean hasKeyData = this.expiresAt != null && this.key != null && this.keySignature != null;
        PacketTypes.writeString(byteBuf, this.name);
        byteBuf.writeBoolean(hasKeyData);
        if (hasKeyData) {
            byteBuf.writeLong(this.expiresAt.toEpochMilli());
            PacketTypes.writeByteArray(byteBuf, this.key.getEncoded());
            PacketTypes.writeByteArray(byteBuf, this.keySignature);
        }
    }

}
