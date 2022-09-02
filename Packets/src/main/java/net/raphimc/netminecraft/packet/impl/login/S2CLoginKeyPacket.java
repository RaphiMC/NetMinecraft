package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class S2CLoginKeyPacket implements IPacket {

    public String serverId;
    public byte[] publicKey;
    public byte[] nonce;

    public S2CLoginKeyPacket() {
    }

    public S2CLoginKeyPacket(final String serverId, final byte[] publicKey, final byte[] nonce) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.nonce = nonce;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.serverId = buf.readString(20);
        this.publicKey = buf.readByteArray();
        this.nonce = buf.readByteArray();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.serverId);
        buf.writeByteArray(this.publicKey);
        buf.writeByteArray(this.nonce);
    }

}
