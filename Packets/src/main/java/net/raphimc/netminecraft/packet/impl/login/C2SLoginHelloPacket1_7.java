package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginHelloPacket1_7 implements IPacket {

    public String name;

    public C2SLoginHelloPacket1_7() {
    }

    public C2SLoginHelloPacket1_7(final String name) {
        this.name = name;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.name = PacketTypes.readString(byteBuf, 16);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.name);
    }

}
