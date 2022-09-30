package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.util.UUIDAdapter;

import java.util.UUID;

public class S2CLoginSuccessPacket1_7 implements IPacket {

    public UUID uuid;
    public String name;

    public S2CLoginSuccessPacket1_7() {
    }

    public S2CLoginSuccessPacket1_7(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.uuid = UUIDAdapter.fromString(PacketTypes.readString(byteBuf, 36));
        this.name = PacketTypes.readString(byteBuf, 16);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.uuid == null ? "" : UUIDAdapter.fromUUID(this.uuid));
        PacketTypes.writeString(byteBuf, this.name);
    }

}
