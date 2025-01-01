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
package net.raphimc.netminecraft.packet.impl.handshaking;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.constants.IntendedState;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SHandshakingClientIntentionPacket implements Packet {

    public int protocolVersion;
    public String address;
    public int port;
    public IntendedState intendedState;

    public C2SHandshakingClientIntentionPacket() {
    }

    public C2SHandshakingClientIntentionPacket(final int protocolVersion, final String address, final int port, final IntendedState intendedState) {
        this.protocolVersion = protocolVersion;
        this.address = address;
        this.port = port;
        this.intendedState = intendedState;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        this.protocolVersion = PacketTypes.readVarInt(byteBuf);
        this.address = PacketTypes.readString(byteBuf, 255);
        this.port = byteBuf.readUnsignedShort();
        this.intendedState = IntendedState.byId(PacketTypes.readVarInt(byteBuf));
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        PacketTypes.writeVarInt(byteBuf, this.protocolVersion);
        PacketTypes.writeString(byteBuf, this.address);
        byteBuf.writeShort(this.port);
        PacketTypes.writeVarInt(byteBuf, this.intendedState.ordinal() + 1);
    }

}
