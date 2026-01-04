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
import net.raphimc.netminecraft.packet.impl.configuration.*;
import net.raphimc.netminecraft.packet.impl.handshaking.C2SHandshakingClientIntentionPacket;
import net.raphimc.netminecraft.packet.impl.login.*;
import net.raphimc.netminecraft.packet.impl.play.*;
import net.raphimc.netminecraft.packet.impl.status.C2SStatusPingRequestPacket;
import net.raphimc.netminecraft.packet.impl.status.C2SStatusRequestPacket;
import net.raphimc.netminecraft.packet.impl.status.S2CStatusPongResponsePacket;
import net.raphimc.netminecraft.packet.impl.status.S2CStatusResponsePacket;

public class DefaultPacketRegistry extends PacketRegistryImpl {

    public DefaultPacketRegistry(final boolean isClientside, final int protocolVersion) {
        super(isClientside, protocolVersion);

        this.registerPacket(MCPackets.C2S_HANDSHAKING_CLIENT_INTENTION, C2SHandshakingClientIntentionPacket::new);

        this.registerPacket(MCPackets.C2S_STATUS_REQUEST, C2SStatusRequestPacket::new);
        this.registerPacket(MCPackets.C2S_STATUS_PING_REQUEST, C2SStatusPingRequestPacket::new);
        this.registerPacket(MCPackets.S2C_STATUS_RESPONSE, S2CStatusResponsePacket::new);
        this.registerPacket(MCPackets.S2C_STATUS_PONG_RESPONSE, S2CStatusPongResponsePacket::new);

        this.registerPacket(MCPackets.C2S_LOGIN_HELLO, C2SLoginHelloPacket::new);
        this.registerPacket(MCPackets.C2S_LOGIN_KEY, C2SLoginKeyPacket::new);
        this.registerPacket(MCPackets.C2S_LOGIN_CUSTOM_QUERY_ANSWER, C2SLoginCustomQueryAnswerPacket::new);
        this.registerPacket(MCPackets.C2S_LOGIN_ACKNOWLEDGED, C2SLoginAcknowledgedPacket::new);
        this.registerPacket(MCPackets.C2S_LOGIN_COOKIE_RESPONSE, C2SLoginCookieResponsePacket::new);
        this.registerPacket(MCPackets.S2C_LOGIN_DISCONNECT, S2CLoginDisconnectPacket::new);
        this.registerPacket(MCPackets.S2C_LOGIN_HELLO, S2CLoginHelloPacket::new);
        this.registerPacket(MCPackets.S2C_LOGIN_GAME_PROFILE, S2CLoginGameProfilePacket::new);
        this.registerPacket(MCPackets.S2C_LOGIN_COMPRESSION, S2CLoginCompressionPacket::new);
        this.registerPacket(MCPackets.S2C_LOGIN_CUSTOM_QUERY, S2CLoginCustomQueryPacket::new);
        this.registerPacket(MCPackets.S2C_LOGIN_COOKIE_REQUEST, S2CLoginCookieRequestPacket::new);

        this.registerPacket(MCPackets.C2S_CONFIG_CUSTOM_PAYLOAD, C2SConfigCustomPayloadPacket::new);
        this.registerPacket(MCPackets.C2S_CONFIG_FINISH_CONFIGURATION, C2SConfigFinishConfigurationPacket::new);
        this.registerPacket(MCPackets.C2S_CONFIG_KEEP_ALIVE, C2SConfigKeepAlivePacket::new);
        this.registerPacket(MCPackets.C2S_CONFIG_RESOURCE_PACK, C2SConfigResourcePackPacket::new);
        this.registerPacket(MCPackets.C2S_CONFIG_COOKIE_RESPONSE, C2SConfigCookieResponsePacket::new);
        this.registerPacket(MCPackets.C2S_CONFIG_SELECT_KNOWN_PACKS, C2SConfigSelectKnownPacksPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_CUSTOM_PAYLOAD, S2CConfigCustomPayloadPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_DISCONNECT, S2CConfigDisconnectPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_FINISH_CONFIGURATION, S2CConfigFinishConfigurationPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_KEEP_ALIVE, S2CConfigKeepAlivePacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_RESOURCE_PACK, S2CConfigResourcePackPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_RESOURCE_PACK_POP, S2CConfigResourcePackPopPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_RESOURCE_PACK_PUSH, S2CConfigResourcePackPushPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_COOKIE_REQUEST, S2CConfigCookieRequestPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_STORE_COOKIE, S2CConfigStoreCookiePacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_TRANSFER, S2CConfigTransferPacket::new);
        this.registerPacket(MCPackets.S2C_CONFIG_SELECT_KNOWN_PACKS, S2CConfigSelectKnownPacksPacket::new);

        this.registerPacket(MCPackets.C2S_CONFIGURATION_ACKNOWLEDGED, C2SPlayConfigurationAcknowledgedPacket::new);
        this.registerPacket(MCPackets.C2S_RESOURCE_PACK, C2SPlayResourcePackPacket::new);
        this.registerPacket(MCPackets.C2S_CUSTOM_PAYLOAD, C2SPlayCustomPayloadPacket::new);
        this.registerPacket(MCPackets.C2S_KEEP_ALIVE, C2SPlayKeepAlivePacket::new);
        this.registerPacket(MCPackets.S2C_KEEP_ALIVE, S2CPlayKeepAlivePacket::new);
        this.registerPacket(MCPackets.S2C_START_CONFIGURATION, S2CPlayStartConfigurationPacket::new);
        this.registerPacket(MCPackets.S2C_DISCONNECT, S2CPlayDisconnectPacket::new);
        this.registerPacket(MCPackets.S2C_CUSTOM_PAYLOAD, S2CPlayCustomPayloadPacket::new);
        this.registerPacket(MCPackets.S2C_SET_COMPRESSION, S2CPlaySetCompressionPacket::new);
        this.registerPacket(MCPackets.S2C_RESOURCE_PACK, S2CPlayResourcePackPacket::new);
        this.registerPacket(MCPackets.S2C_RESOURCE_PACK_POP, S2CPlayResourcePackPopPacket::new);
        this.registerPacket(MCPackets.S2C_RESOURCE_PACK_PUSH, S2CPlayResourcePackPushPacket::new);
        this.registerPacket(MCPackets.S2C_TRANSFER, S2CPlayTransferPacket::new);
    }

}
