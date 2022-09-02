package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class S2CLoginCompressionPacket implements IPacket {

    public int compressionThreshold;

    public S2CLoginCompressionPacket() {
    }

    public S2CLoginCompressionPacket(final int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.compressionThreshold = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.compressionThreshold);
    }

}
