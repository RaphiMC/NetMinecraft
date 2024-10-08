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
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.util.UUIDAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class S2CLoginGameProfilePacket implements Packet {

    public UUID uuid;
    public String name;

    public List<String[]> properties;

    public boolean strictErrorHandling;

    public S2CLoginGameProfilePacket() {
    }

    public S2CLoginGameProfilePacket(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public S2CLoginGameProfilePacket(final UUID uuid, final String name, final List<String[]> properties) {
        this(uuid, name);
        this.properties = properties;
    }

    public S2CLoginGameProfilePacket(final UUID uuid, final String name, final List<String[]> properties, final boolean strictErrorHandling) {
        this(uuid, name, properties);
        this.strictErrorHandling = strictErrorHandling;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_7_2) {
            this.uuid = UUIDAdapter.fromString(PacketTypes.readString(byteBuf, 36));
        } else if (protocolVersion <= MCVersion.v1_15_2) {
            this.uuid = UUID.fromString(PacketTypes.readString(byteBuf, 36));
        } else {
            this.uuid = this.uuidFromIntArray(new int[]{byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()});
        }
        this.name = PacketTypes.readString(byteBuf, 16);
        if (protocolVersion >= MCVersion.v1_19) {
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
        if (protocolVersion >= MCVersion.v1_20_5 && protocolVersion <= MCVersion.v1_21) {
            this.strictErrorHandling = byteBuf.readBoolean();
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_7_2) {
            PacketTypes.writeString(byteBuf, this.uuid == null ? "" : UUIDAdapter.fromUUID(this.uuid));
        } else if (protocolVersion <= MCVersion.v1_15_2) {
            PacketTypes.writeString(byteBuf, this.uuid == null ? "" : this.uuid.toString());
        } else {
            for (int i : this.uuidToIntArray(this.uuid)) byteBuf.writeInt(i);
        }
        PacketTypes.writeString(byteBuf, this.name);
        if (protocolVersion >= MCVersion.v1_19) {
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
        if (protocolVersion >= MCVersion.v1_20_5 && protocolVersion <= MCVersion.v1_21) {
            byteBuf.writeBoolean(this.strictErrorHandling);
        }
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
