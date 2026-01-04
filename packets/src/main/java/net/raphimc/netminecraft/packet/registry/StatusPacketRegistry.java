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
package net.raphimc.netminecraft.packet.registry;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.packet.impl.handshaking.C2SHandshakingClientIntentionPacket;
import net.raphimc.netminecraft.packet.impl.status.C2SStatusPingRequestPacket;
import net.raphimc.netminecraft.packet.impl.status.C2SStatusRequestPacket;
import net.raphimc.netminecraft.packet.impl.status.S2CStatusPongResponsePacket;
import net.raphimc.netminecraft.packet.impl.status.S2CStatusResponsePacket;

public class StatusPacketRegistry extends PacketRegistryImpl {

    public StatusPacketRegistry(final boolean isClientside, final int protocolVersion) {
        super(isClientside, protocolVersion);

        this.registerPacket(MCPackets.C2S_HANDSHAKING_CLIENT_INTENTION, C2SHandshakingClientIntentionPacket::new);

        this.registerPacket(MCPackets.C2S_STATUS_REQUEST, C2SStatusRequestPacket::new);
        this.registerPacket(MCPackets.C2S_STATUS_PING_REQUEST, C2SStatusPingRequestPacket::new);
        this.registerPacket(MCPackets.S2C_STATUS_RESPONSE, S2CStatusResponsePacket::new);
        this.registerPacket(MCPackets.S2C_STATUS_PONG_RESPONSE, S2CStatusPongResponsePacket::new);
    }

}
