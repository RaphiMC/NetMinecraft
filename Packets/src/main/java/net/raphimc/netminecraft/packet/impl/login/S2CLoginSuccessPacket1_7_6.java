package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.PacketByteBuf;

import java.util.UUID;

public class S2CLoginSuccessPacket1_7_6 extends S2CLoginSuccessPacket1_7 {

    public S2CLoginSuccessPacket1_7_6() {
    }

    public S2CLoginSuccessPacket1_7_6(final UUID uuid, final String name) {
        super(uuid, name);
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.uuid = UUID.fromString(buf.readString(36));
        this.name = buf.readString(16);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.uuid == null ? "" : this.uuid.toString());
        buf.writeString(this.name);
    }

}
