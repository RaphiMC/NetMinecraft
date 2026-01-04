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
package net.raphimc.netminecraft.packet.impl.common;

import io.netty.buffer.ByteBuf;
import net.lenni0451.mcstructs.text.TextComponent;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.packet.SerializerTypes;

import java.util.UUID;

public abstract class S2CResourcePackPushPacket implements Packet {

    public UUID packId;
    public String url;
    public String hash;
    public boolean required;
    public TextComponent message;

    public S2CResourcePackPushPacket() {
    }

    public S2CResourcePackPushPacket(final UUID packId, final String url, final String hash, final boolean required, final TextComponent message) {
        this.packId = packId;
        this.url = url;
        this.hash = hash;
        this.required = required;
        this.message = message;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        this.packId = PacketTypes.readUuid(byteBuf);
        this.url = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
        this.hash = PacketTypes.readString(byteBuf, 40);
        this.required = byteBuf.readBoolean();
        if (byteBuf.readBoolean()) {
            this.message = SerializerTypes.getTextComponentSerializer(protocolVersion).getParentCodec().deserialize(PacketTypes.readUnnamedTag(byteBuf));
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        PacketTypes.writeUuid(byteBuf, this.packId);
        PacketTypes.writeString(byteBuf, this.url);
        PacketTypes.writeString(byteBuf, this.hash);
        byteBuf.writeBoolean(this.required);
        byteBuf.writeBoolean(this.message != null);
        if (this.message != null) {
            PacketTypes.writeUnnamedTag(byteBuf, SerializerTypes.getTextComponentSerializer(protocolVersion).getParentCodec().serializeNbtTree(this.message));
        }
    }

}
