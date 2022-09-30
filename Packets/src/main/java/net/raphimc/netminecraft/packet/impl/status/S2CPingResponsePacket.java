package net.raphimc.netminecraft.packet.impl.status;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;

public class S2CPingResponsePacket implements IPacket {

    public long startTime;

    public S2CPingResponsePacket() {
    }

    public S2CPingResponsePacket(final long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.startTime = byteBuf.readLong();
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeLong(this.startTime);
    }

}
