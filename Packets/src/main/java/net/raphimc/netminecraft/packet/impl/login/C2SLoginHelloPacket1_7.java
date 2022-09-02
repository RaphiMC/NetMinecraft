package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class C2SLoginHelloPacket1_7 implements IPacket {

    public String name;

    public C2SLoginHelloPacket1_7() {
    }

    public C2SLoginHelloPacket1_7(final String name) {
        this.name = name;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.name = buf.readString(16);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.name);
    }

}
