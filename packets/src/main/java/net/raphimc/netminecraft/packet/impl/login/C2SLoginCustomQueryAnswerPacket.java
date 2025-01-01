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
package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginCustomQueryAnswerPacket implements Packet {

    public int queryId;
    public byte[] response;

    public C2SLoginCustomQueryAnswerPacket() {
    }

    public C2SLoginCustomQueryAnswerPacket(final int queryId, final byte[] response) {
        this.queryId = queryId;
        this.response = response;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        this.queryId = PacketTypes.readVarInt(byteBuf);
        if (byteBuf.readBoolean()) {
            final int length = byteBuf.readableBytes();
            if (length < 0 || length > 1048576) {
                throw new IllegalStateException("Payload may not be larger than 1048576 bytes");
            }

            this.response = new byte[length];
            byteBuf.readBytes(this.response);
        } else {
            this.response = null;
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        PacketTypes.writeVarInt(byteBuf, this.queryId);
        byteBuf.writeBoolean(this.response != null);
        if (this.response != null) {
            byteBuf.writeBytes(this.response);
        }
    }

}
