package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.util.*;

public class S2CLoginSuccessPacket1_19 extends S2CLoginSuccessPacket1_16 {

    public List<String[]> properties;

    public S2CLoginSuccessPacket1_19() {
    }

    public S2CLoginSuccessPacket1_19(final UUID uuid, final String name, final List<String[]> properties) {
        super(uuid, name);
        this.properties = properties;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.uuid = this.uuidFromIntArray(new int[]{byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()});
        this.name = PacketTypes.readString(byteBuf, 16);
        final int count = PacketTypes.readVarInt(byteBuf);
        this.properties = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String name = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
            final String value = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
            String signature = null;
            if (byteBuf.readBoolean()) {
                signature = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
            }
            this.properties.add(new String[]{name, value, signature});
        }
    }

    @Override
    public void write(ByteBuf byteBuf) {
        for (int i : this.uuidToIntArray(this.uuid)) byteBuf.writeInt(i);
        PacketTypes.writeString(byteBuf, this.name);
        PacketTypes.writeVarInt(byteBuf, this.properties.size());
        for (String[] property : this.properties) {
            PacketTypes.writeString(byteBuf, property[0]);
            PacketTypes.writeString(byteBuf, property[1]);
            byteBuf.writeBoolean(property[2] != null);
            if (property[2] != null) {
                PacketTypes.writeString(byteBuf, property[2]);
            }
        }
    }
}
