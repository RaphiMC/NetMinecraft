/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2025 RK_01/RaphiMC and contributors
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
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.packet.model.KnownPack;

import java.util.ArrayList;
import java.util.List;

public class C2SConfigSelectKnownPacksPacket implements Packet {

    public List<KnownPack> knownPacks;

    public C2SConfigSelectKnownPacksPacket() {
    }

    public C2SConfigSelectKnownPacksPacket(final List<KnownPack> knownPacks) {
        this.knownPacks = knownPacks;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        final int length = PacketTypes.readVarInt(byteBuf);
        if (length < 0 || length > 64) {
            throw new IllegalStateException("Length may not be larger than 64 entries");
        }
        this.knownPacks = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            final String namespace = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
            final String id = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
            final String version = PacketTypes.readString(byteBuf, Short.MAX_VALUE);
            this.knownPacks.add(new KnownPack(namespace, id, version));
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        PacketTypes.writeVarInt(byteBuf, this.knownPacks.size());
        for (KnownPack knownPack : this.knownPacks) {
            PacketTypes.writeString(byteBuf, knownPack.namespace);
            PacketTypes.writeString(byteBuf, knownPack.id);
            PacketTypes.writeString(byteBuf, knownPack.version);
        }
    }

}
