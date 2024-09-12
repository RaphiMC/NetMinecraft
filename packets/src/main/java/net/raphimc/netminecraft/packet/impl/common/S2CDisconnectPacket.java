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
package net.raphimc.netminecraft.packet.impl.common;

import io.netty.buffer.ByteBuf;
import net.lenni0451.mcstructs.text.ATextComponent;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.packet.SerializerTypes;

public abstract class S2CDisconnectPacket implements Packet {

    public ATextComponent reason;

    public S2CDisconnectPacket() {
    }

    public S2CDisconnectPacket(final ATextComponent reason) {
        this.reason = reason;
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_20_2) {
            PacketTypes.writeString(byteBuf, SerializerTypes.getTextComponentSerializer(protocolVersion).serialize(this.reason));
        } else {
            PacketTypes.writeUnnamedTag(byteBuf, SerializerTypes.getTextComponentSerializer(protocolVersion).getParentCodec().serializeNbt(this.reason));
        }
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_20_2) {
            this.reason = SerializerTypes.getTextComponentSerializer(protocolVersion).deserialize(PacketTypes.readString(byteBuf, 262144));
        } else {
            this.reason = SerializerTypes.getTextComponentSerializer(protocolVersion).getParentCodec().deserialize(PacketTypes.readUnnamedTag(byteBuf));
        }
    }

}
