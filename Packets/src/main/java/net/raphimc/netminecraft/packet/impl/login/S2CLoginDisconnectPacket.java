package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;

public class S2CLoginDisconnectPacket implements IPacket {

    public String reason;

    public S2CLoginDisconnectPacket() {
    }

    public S2CLoginDisconnectPacket(final String reason) {
        this.reason = reason;
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.reason = buf.readString(262144);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.reason);
    }

}
