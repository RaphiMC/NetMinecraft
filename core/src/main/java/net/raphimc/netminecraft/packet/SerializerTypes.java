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
package net.raphimc.netminecraft.packet;

import net.lenni0451.mcstructs.text.serializer.TextComponentSerializer;
import net.raphimc.netminecraft.constants.MCVersion;

public class SerializerTypes {

    public static TextComponentSerializer getTextComponentSerializer(final int protocolVersion) {
        if (protocolVersion >= MCVersion.v1_20_5) {
            return TextComponentSerializer.V1_20_5;
        } else if (protocolVersion >= MCVersion.v1_20_3) {
            return TextComponentSerializer.V1_20_3;
        } else if (protocolVersion >= MCVersion.v1_19_4) {
            return TextComponentSerializer.V1_19_4;
        } else if (protocolVersion >= MCVersion.v1_18) {
            return TextComponentSerializer.V1_18;
        } else if (protocolVersion >= MCVersion.v1_17) {
            return TextComponentSerializer.V1_17;
        } else if (protocolVersion >= MCVersion.v1_16) {
            return TextComponentSerializer.V1_16;
        } else if (protocolVersion >= MCVersion.v1_15) {
            return TextComponentSerializer.V1_15;
        } else if (protocolVersion >= MCVersion.v1_14) {
            return TextComponentSerializer.V1_14;
        } else if (protocolVersion >= MCVersion.v1_12) {
            return TextComponentSerializer.V1_12;
        } else if (protocolVersion >= MCVersion.v1_9) {
            return TextComponentSerializer.V1_9;
        } else if (protocolVersion >= MCVersion.v1_8) {
            return TextComponentSerializer.V1_8;
        } else {
            return TextComponentSerializer.V1_7;
        }
    }

}
