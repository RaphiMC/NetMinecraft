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
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.util.UUID;

public abstract class S2CResourcePackPopPacket implements Packet {

    public UUID packId;

    public S2CResourcePackPopPacket() {
    }

    public S2CResourcePackPopPacket(final UUID packId) {
        this.packId = packId;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        if (byteBuf.readBoolean()) {
            this.packId = PacketTypes.readUuid(byteBuf);
        } else {
            this.packId = null;
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        byteBuf.writeBoolean(this.packId != null);
        if (this.packId != null) {
            PacketTypes.writeUuid(byteBuf, this.packId);
        }
    }

}
