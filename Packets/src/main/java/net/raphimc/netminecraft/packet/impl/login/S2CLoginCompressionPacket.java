package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class S2CLoginCompressionPacket implements IPacket {

    public int compressionThreshold;

    public S2CLoginCompressionPacket() {
    }

    public S2CLoginCompressionPacket(final int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.compressionThreshold = PacketTypes.readVarInt(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeVarInt(byteBuf, this.compressionThreshold);
    }

}
