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
package net.raphimc.netminecraft.packet.impl.configuration;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class S2CConfigTransfer1_20_5 implements IPacket {

    public String host;
    public int port;

    public S2CConfigTransfer1_20_5() {
    }

    public S2CConfigTransfer1_20_5(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.host = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
        this.port = PacketTypes.readVarInt(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.host);
        PacketTypes.writeVarInt(byteBuf, this.port);
    }

}
