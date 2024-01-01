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
package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.util.UUID;

public class S2CLoginSuccessPacket1_7_6 extends S2CLoginSuccessPacket1_7 {

    public S2CLoginSuccessPacket1_7_6() {
    }

    public S2CLoginSuccessPacket1_7_6(final UUID uuid, final String name) {
        super(uuid, name);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.uuid = UUID.fromString(PacketTypes.readString(byteBuf, 36));
        this.name = PacketTypes.readString(byteBuf, 16);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeString(byteBuf, this.uuid == null ? "" : this.uuid.toString());
        PacketTypes.writeString(byteBuf, this.name);
    }

}
