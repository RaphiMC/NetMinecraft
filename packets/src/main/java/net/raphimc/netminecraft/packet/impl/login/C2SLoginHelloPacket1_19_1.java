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
package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.security.PublicKey;
import java.time.Instant;
import java.util.UUID;

public class C2SLoginHelloPacket1_19_1 extends C2SLoginHelloPacket1_19 {

    public UUID uuid;

    public C2SLoginHelloPacket1_19_1() {
    }

    public C2SLoginHelloPacket1_19_1(final String name) {
        super(name);
    }

    public C2SLoginHelloPacket1_19_1(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature) {
        super(name, expiresAt, key, keySignature);
    }

    public C2SLoginHelloPacket1_19_1(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature, final UUID uuid) {
        super(name, expiresAt, key, keySignature);
        this.uuid = uuid;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        super.read(byteBuf);
        if (byteBuf.readBoolean()) this.uuid = PacketTypes.readUuid(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        super.write(byteBuf);
        byteBuf.writeBoolean(this.uuid != null);
        if (this.uuid != null) {
            PacketTypes.writeUuid(byteBuf, this.uuid);
        }
    }

}
