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
package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginKeyPacket1_8;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginCompressionPacket;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginDisconnectPacket1_8;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginKeyPacket1_8;

public class LoginPacketRegistry1_8 extends LoginPacketRegistry1_7_6 {

    public LoginPacketRegistry1_8(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_ENCRYPTION_RESPONSE.getId(MCVersion.v1_8), C2SLoginKeyPacket1_8::new);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_DISCONNECT.getId(MCVersion.v1_8), S2CLoginDisconnectPacket1_8::new);
        this.registerS2CPacket(MCPackets.S2C_LOGIN_ENCRYPTION_REQUEST.getId(MCVersion.v1_8), S2CLoginKeyPacket1_8::new);
        this.registerS2CPacket(MCPackets.S2C_LOGIN_COMPRESSION.getId(MCVersion.v1_8), S2CLoginCompressionPacket::new);
    }

}
