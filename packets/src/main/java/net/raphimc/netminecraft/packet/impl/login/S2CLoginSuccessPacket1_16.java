/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
