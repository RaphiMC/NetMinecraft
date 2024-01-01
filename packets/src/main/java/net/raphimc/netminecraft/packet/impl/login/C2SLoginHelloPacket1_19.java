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
import net.raphimc.netminecraft.netty.crypto.CryptUtil;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.security.PublicKey;
import java.time.Instant;

public class C2SLoginHelloPacket1_19 extends C2SLoginHelloPacket1_7 {

    public Instant expiresAt;
    public PublicKey key;
    public byte[] keySignature;

    public C2SLoginHelloPacket1_19() {
    }

    public C2SLoginHelloPacket1_19(final String name) {
        super(name);
    }

    public C2SLoginHelloPacket1_19(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature) {
        super(name);
        this.expiresAt = expiresAt;
        this.key = key;
        this.keySignature = keySignature;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.name = PacketTypes.readString(byteBuf, 16);
        if (byteBuf.readBoolean()) {
            this.expiresAt = Instant.ofEpochMilli(byteBuf.readLong());
            this.key = CryptUtil.decodeRsaPublicKey(PacketTypes.readByteArray(byteBuf, 512));
            this.keySignature = PacketTypes.readByteArray(byteBuf, 4096);
        }
    }

    @Override
    public void write(ByteBuf byteBuf) {
        final boolean hasKeyData = this.expiresAt != null && this.key != null && this.keySignature != null;
        PacketTypes.writeString(byteBuf, this.name);
        byteBuf.writeBoolean(hasKeyData);
        if (hasKeyData) {
            byteBuf.writeLong(this.expiresAt.toEpochMilli());
            PacketTypes.writeByteArray(byteBuf, this.key.getEncoded());
            PacketTypes.writeByteArray(byteBuf, this.keySignature);
        }
    }

}
