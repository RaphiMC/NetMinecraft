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
import net.raphimc.netminecraft.packet.impl.login.*;
import net.raphimc.netminecraft.packet.registry.DefaultPacketRegistry;

public class LoginPacketRegistry1_7 extends DefaultPacketRegistry {

    public LoginPacketRegistry1_7(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_HELLO.getId(MCVersion.v1_7_2), C2SLoginHelloPacket1_7::new);
        this.registerC2SPacket(MCPackets.C2S_LOGIN_KEY.getId(MCVersion.v1_7_2), C2SLoginKeyPacket1_7::new);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_DISCONNECT.getId(MCVersion.v1_7_2), S2CLoginDisconnectPacket1_7::new);
        this.registerS2CPacket(MCPackets.S2C_LOGIN_HELLO.getId(MCVersion.v1_7_2), S2CLoginHelloPacket1_7::new);
        this.registerS2CPacket(MCPackets.S2C_LOGIN_GAME_PROFILE.getId(MCVersion.v1_7_2), S2CLoginGameProfilePacket1_7::new);
    }

}
