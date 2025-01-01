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
package net.raphimc.netminecraft.packet;

import io.netty.buffer.ByteBuf;

public class UnknownPacket implements Packet {

    public int packetId;
    public byte[] data;

    public UnknownPacket(final int packetId) {
        this.packetId = packetId;
    }

    public UnknownPacket(final int packetId, final byte[] data) {
        this.packetId = packetId;
        this.data = data;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        this.data = PacketTypes.readReadableBytes(byteBuf);
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        byteBuf.writeBytes(this.data);
    }

}
