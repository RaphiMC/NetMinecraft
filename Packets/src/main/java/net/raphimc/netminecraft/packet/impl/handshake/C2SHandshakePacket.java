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
package net.raphimc.netminecraft.packet.impl.handshake;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.constants.ConnectionState;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SHandshakePacket implements IPacket {

    public int protocolVersion;
    public String address;
    public int port;
    public ConnectionState intendedState;

    public C2SHandshakePacket() {
    }

    public C2SHandshakePacket(final int protocolVersion, final String address, final int port, final ConnectionState intendedState) {
        this.protocolVersion = protocolVersion;
        this.address = address;
        this.port = port;
        this.intendedState = intendedState;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.protocolVersion = PacketTypes.readVarInt(byteBuf);
        this.address = PacketTypes.readString(byteBuf, 255);
        this.port = byteBuf.readUnsignedShort();
        this.intendedState = ConnectionState.getById(PacketTypes.readVarInt(byteBuf));
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeVarInt(byteBuf, this.protocolVersion);
        PacketTypes.writeString(byteBuf, this.address);
        byteBuf.writeShort(this.port);
        PacketTypes.writeVarInt(byteBuf, this.intendedState.getId());
    }

}
