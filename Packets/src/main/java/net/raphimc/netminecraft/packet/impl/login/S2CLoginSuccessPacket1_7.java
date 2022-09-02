package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketByteBuf;
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
    public void read(PacketByteBuf buf) {
        this.uuid = UUIDAdapter.fromString(buf.readString(36));
        this.name = buf.readString(16);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.uuid == null ? "" : UUIDAdapter.fromUUID(this.uuid));
        buf.writeString(this.name);
    }

}
