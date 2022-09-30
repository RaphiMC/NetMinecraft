package net.raphimc.netminecraft.packet.impl.status;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;

public class C2SPingRequestPacket implements IPacket {

    public long startTime;

    public C2SPingRequestPacket() {
    }

    public C2SPingRequestPacket(final long startTime) {
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
