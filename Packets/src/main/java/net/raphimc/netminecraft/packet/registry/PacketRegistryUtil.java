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
package net.raphimc.netminecraft.packet.registry;

import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.registry.impl.*;

public class PacketRegistryUtil {

    public static PacketRegistry getHandshakeRegistry(final boolean clientside) {
        return new HandshakePacketRegistryBase(clientside);
    }

    public static PacketRegistry getStatusRegistry(final boolean clientside) {
        return new StatusPacketRegistryBase(clientside);
    }

    public static PacketRegistry getLoginRegistry(final boolean clientside, final int protocolVersion) {
        if (protocolVersion >= MCVersion.v1_19_3) {
            return new LoginPacketRegistryBase1_19_3(clientside);
        } else if (protocolVersion >= MCVersion.v1_19_1) {
            return new LoginPacketRegistryBase1_19_1(clientside);
        } else if (protocolVersion >= MCVersion.v1_19) {
            return new LoginPacketRegistryBase1_19(clientside);
        } else if (protocolVersion >= MCVersion.v1_16) {
            return new LoginPacketRegistryBase1_16(clientside);
        } else if (protocolVersion >= MCVersion.v1_13) {
            return new LoginPacketRegistryBase1_13(clientside);
        } else if (protocolVersion >= MCVersion.v1_8) {
            return new LoginPacketRegistryBase1_8(clientside);
        } else if (protocolVersion >= MCVersion.v1_7_6) {
            return new LoginPacketRegistryBase1_7_6(clientside);
        }
        return new LoginPacketRegistryBase1_7(clientside);
    }

    public static PacketRegistry getPlayRegistry(final boolean clientside, final int protocolVersion) {
        return new PlayPacketRegistryBase(clientside);
    }

}
