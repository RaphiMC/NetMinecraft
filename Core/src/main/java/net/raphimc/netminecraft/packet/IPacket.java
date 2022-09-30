package net.raphimc.netminecraft.packet;

import io.netty.buffer.ByteBuf;

public interface IPacket {

    void read(final ByteBuf byteBuf);

    void write(final ByteBuf byteBuf);

}
