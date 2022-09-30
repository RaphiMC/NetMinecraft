package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.util.UUID;

public class S2CLoginSuccessPacket1_7_6 extends S2CLoginSuccessPacket1_7 {

    public S2CLoginSuccessPacket1_7_6() {
    }

    public S2CLoginSuccessPacket1_7_6(final UUID uuid, final String name) {
        super(uuid, name);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.uuid = UUID.fromString(PacketTypes.readString(byteBuf, 36));
        this.name = PacketTypes.readString(byteBuf, 16);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.uuid == null ? "" : this.uuid.toString());
        PacketTypes.writeString(byteBuf, this.name);
    }

}
