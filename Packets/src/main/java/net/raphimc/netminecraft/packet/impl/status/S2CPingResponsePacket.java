package net.raphimc.netminecraft.packet.impl.status;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class S2CPingResponsePacket implements IPacket {

    public long startTime;

    public S2CPingResponsePacket() {
    }

    public S2CPingResponsePacket(final long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.startTime = buf.readLong();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.startTime);
    }

}
