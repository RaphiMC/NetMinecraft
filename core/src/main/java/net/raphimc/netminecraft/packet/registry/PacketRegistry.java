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
package net.raphimc.netminecraft.packet.registry;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.constants.ConnectionState;
import net.raphimc.netminecraft.packet.Packet;

public interface PacketRegistry {

    Packet createPacket(final int packetId, final ByteBuf byteBuf);

    int getPacketId(final Packet packet);

    int getProtocolVersion();

    ConnectionState getConnectionState();

    void setConnectionState(final ConnectionState connectionState);

}
