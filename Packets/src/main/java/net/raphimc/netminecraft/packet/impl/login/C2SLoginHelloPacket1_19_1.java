package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.PacketByteBuf;

import java.security.PublicKey;
import java.time.Instant;
import java.util.UUID;

public class C2SLoginHelloPacket1_19_1 extends C2SLoginHelloPacket1_19 {

    public UUID uuid;

    public C2SLoginHelloPacket1_19_1() {
    }

    public C2SLoginHelloPacket1_19_1(final String name) {
        super(name);
    }

    public C2SLoginHelloPacket1_19_1(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature) {
        super(name, expiresAt, key, keySignature);
    }

    public C2SLoginHelloPacket1_19_1(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature, final UUID uuid) {
        super(name, expiresAt, key, keySignature);
        this.uuid = uuid;
    }

    @Override
    public void read(PacketByteBuf buf) {
        super.read(buf);
        if (buf.readBoolean()) this.uuid = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) {
        super.write(buf);
        buf.writeBoolean(this.uuid != null);
        if (this.uuid != null) {
            buf.writeUuid(this.uuid);
        }
    }

}
