package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.PacketByteBuf;

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
    public void read(PacketByteBuf buf) {
        this.uuid = this.uuidFromIntArray(new int[]{buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()});
        this.name = buf.readString(16);
        final int count = buf.readVarInt();
        this.properties = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String name = buf.readString(Short.MAX_VALUE);
            final String value = buf.readString(Short.MAX_VALUE);
            String signature = null;
            if (buf.readBoolean()) {
                signature = buf.readString(Short.MAX_VALUE);
            }
            this.properties.add(new String[]{name, value, signature});
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        for (int i : this.uuidToIntArray(this.uuid)) buf.writeInt(i);
        buf.writeString(this.name);
        buf.writeVarInt(this.properties.size());
        for (String[] property : this.properties) {
            buf.writeString(property[0]);
            buf.writeString(property[1]);
            buf.writeBoolean(property[2] != null);
            if (property[2] != null) {
                buf.writeString(property[2]);
            }
        }
    }
}
