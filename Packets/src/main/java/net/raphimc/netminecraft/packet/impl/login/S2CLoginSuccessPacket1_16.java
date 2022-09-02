package net.raphimc.netminecraft.packet.impl.login;

import net.raphimc.netminecraft.packet.PacketByteBuf;

import java.util.UUID;

public class S2CLoginSuccessPacket1_16 extends S2CLoginSuccessPacket1_7_6 {

    public S2CLoginSuccessPacket1_16() {
    }

    public S2CLoginSuccessPacket1_16(final UUID uuid, final String name) {
        super(uuid, name);
    }

    @Override
    public void read(PacketByteBuf buf) {
        this.uuid = this.uuidFromIntArray(new int[]{buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()});
        this.name = buf.readString(16);
    }

    @Override
    public void write(PacketByteBuf buf) {
        for (int i : this.uuidToIntArray(this.uuid)) buf.writeInt(i);
        buf.writeString(this.name);
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
