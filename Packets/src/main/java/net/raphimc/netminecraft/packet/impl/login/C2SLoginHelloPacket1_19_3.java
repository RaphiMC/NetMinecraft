package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.security.PublicKey;
import java.time.Instant;
import java.util.UUID;

public class C2SLoginHelloPacket1_19_3 extends C2SLoginHelloPacket1_19_1 {

    public C2SLoginHelloPacket1_19_3() {
    }

    public C2SLoginHelloPacket1_19_3(final String name) {
        super(name);
    }

    public C2SLoginHelloPacket1_19_3(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature) {
        super(name, expiresAt, key, keySignature);
    }

    public C2SLoginHelloPacket1_19_3(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature, final UUID uuid) {
        super(name, expiresAt, key, keySignature, uuid);
    }

    public C2SLoginHelloPacket1_19_3(final String name, final UUID uuid) {
        super(name, null, null, null, uuid);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.name = PacketTypes.readString(byteBuf, 16);
        if (byteBuf.readBoolean()) this.uuid = PacketTypes.readUuid(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.name);
        byteBuf.writeBoolean(this.uuid != null);
        if (this.uuid != null) {
            PacketTypes.writeUuid(byteBuf, this.uuid);
        }
    }

}
