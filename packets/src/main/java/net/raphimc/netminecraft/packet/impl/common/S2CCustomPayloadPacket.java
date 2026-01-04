/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2026 RK_01/RaphiMC and contributors
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

public abstract class S2CCustomPayloadPacket implements Packet {

    public String channel;
    public byte[] data;

    public S2CCustomPayloadPacket() {
    }

    public S2CCustomPayloadPacket(final String channel, final byte[] data) {
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        this.channel = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
        final int length;
        if (protocolVersion <= MCVersion.v1_7_6) {
            length = byteBuf.readShort();
        } else {
            length = byteBuf.readableBytes();
        }
        this.data = new byte[length];
        byteBuf.readBytes(this.data);
        if (protocolVersion <= MCVersion.v1_7_6) {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        PacketTypes.writeString(byteBuf, this.channel);
        if (protocolVersion <= MCVersion.v1_7_6) {
            byteBuf.writeShort(this.data.length);
        }
        byteBuf.writeBytes(this.data);
    }

}
