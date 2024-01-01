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
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class S2CLoginCustomPayloadPacket implements IPacket {

    public int queryId;
    public String channel;
    public byte[] payload;

    public S2CLoginCustomPayloadPacket() {
    }

    public S2CLoginCustomPayloadPacket(final int queryId, final String channel, final byte[] payload) {
        this.queryId = queryId;
        this.channel = channel;
        this.payload = payload;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.queryId = PacketTypes.readVarInt(byteBuf);
        this.channel = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
        final int length = byteBuf.readableBytes();
        if (length < 0 || length > 1048576) {
            throw new IllegalStateException("Payload may not be larger than 1048576 bytes");
        }
        this.payload = new byte[length];
        byteBuf.readBytes(this.payload);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeVarInt(byteBuf, this.queryId);
        PacketTypes.writeString(byteBuf, this.channel);
        byteBuf.writeBytes(this.payload);
    }

}
