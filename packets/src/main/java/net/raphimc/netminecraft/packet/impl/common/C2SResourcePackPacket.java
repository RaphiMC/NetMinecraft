/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2025 RK_01/RaphiMC and contributors
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
package net.raphimc.netminecraft.packet.impl.common;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.util.UUID;

public abstract class C2SResourcePackPacket implements Packet {

    public int status;

    public UUID packId;

    public String hash;

    public C2SResourcePackPacket() {
    }

    public C2SResourcePackPacket(final int status) {
        this.status = status;
    }

    public C2SResourcePackPacket(final int status, final UUID packId) {
        this.status = status;
        this.packId = packId;
    }

    public C2SResourcePackPacket(final int status, final UUID packId, final String hash) {
        this.status = status;
        this.packId = packId;
        this.hash = hash;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_9_3) {
            this.hash = PacketTypes.readString(byteBuf, 40); // hash
        }
        if (protocolVersion >= MCVersion.v1_20_3) {
            this.packId = PacketTypes.readUuid(byteBuf); // pack id
        }
        this.status = PacketTypes.readVarInt(byteBuf); // status
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_9_3) {
            PacketTypes.writeString(byteBuf, this.hash); // hash
        }
        if (protocolVersion >= MCVersion.v1_20_3) {
            PacketTypes.writeUuid(byteBuf, this.packId); // pack id
        }
        PacketTypes.writeVarInt(byteBuf, this.status); // status
    }

}
