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
package net.raphimc.netminecraft.packet.impl.configuration;

import net.lenni0451.mcstructs.text.TextComponent;
import net.raphimc.netminecraft.packet.impl.common.S2CResourcePackPacket;

public class S2CConfigResourcePackPacket extends S2CResourcePackPacket {

    public S2CConfigResourcePackPacket() {
    }

    public S2CConfigResourcePackPacket(final String url, final String hash, final boolean required, final TextComponent message) {
        super(url, hash, required, message);
    }

}
