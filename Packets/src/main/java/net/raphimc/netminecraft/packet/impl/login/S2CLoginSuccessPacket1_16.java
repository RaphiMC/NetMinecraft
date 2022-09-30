package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.util.UUID;

public class S2CLoginSuccessPacket1_16 extends S2CLoginSuccessPacket1_7_6 {

    public S2CLoginSuccessPacket1_16() {
    }

    public S2CLoginSuccessPacket1_16(final UUID uuid, final String name) {
        super(uuid, name);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.uuid = this.uuidFromIntArray(new int[]{byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()});
        this.name = PacketTypes.readString(byteBuf, 16);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        for (int i : this.uuidToIntArray(this.uuid)) byteBuf.writeInt(i);
        PacketTypes.writeString(byteBuf, this.name);
    }

    protected UUID uuidFromIntArray(final int[] ints) {
        return new UUID((long) ints[0] << 32 | ((long) ints[1] & 0xFFFFFFFFL), (long) ints[2] << 32 | ((long) ints[3] & 0xFFFFFFFFL));
    }

    protected int[] uuidToIntArray(final UUID uuid) {
        return bitsToIntArray(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    protected int[] bitsToIntArray(final long long1, final long long2) {
        return new int[]{(int) (long1 >> 32), (int) long1, (int) (long2 >> 32), (int) long2};
    }

}
