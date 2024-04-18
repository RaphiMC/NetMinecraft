/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2024 RK_01/RaphiMC and contributors
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
