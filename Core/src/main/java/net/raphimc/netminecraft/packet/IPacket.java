package net.raphimc.netminecraft.packet;

public interface IPacket {

    void read(final PacketByteBuf buf);

    void write(final PacketByteBuf buf);

}
