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
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.netty.crypto.CryptUtil;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.security.PublicKey;
import java.time.Instant;
import java.util.UUID;

public class C2SLoginHelloPacket implements Packet {

    public String name;

    public Instant expiresAt;
    public PublicKey key;
    public byte[] keySignature;

    public UUID uuid;

    public C2SLoginHelloPacket() {
    }

    public C2SLoginHelloPacket(final String name) {
        this.name = name;
    }

    public C2SLoginHelloPacket(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature) {
        this(name);
        this.expiresAt = expiresAt;
        this.key = key;
        this.keySignature = keySignature;
    }

    public C2SLoginHelloPacket(final String name, final Instant expiresAt, final PublicKey key, final byte[] keySignature, final UUID uuid) {
        this(name, expiresAt, key, keySignature);
        this.uuid = uuid;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        this.name = PacketTypes.readString(byteBuf, 16);
        if (protocolVersion >= MCVersion.v1_19 && protocolVersion <= MCVersion.v1_19_1) {
            if (byteBuf.readBoolean()) {
                this.expiresAt = Instant.ofEpochMilli(byteBuf.readLong());
                this.key = CryptUtil.decodeRsaPublicKey(PacketTypes.readByteArray(byteBuf, 512));
                this.keySignature = PacketTypes.readByteArray(byteBuf, 4096);
            }
        }
        if (protocolVersion >= MCVersion.v1_19_1 && protocolVersion <= MCVersion.v1_20) {
            if (byteBuf.readBoolean()) {
                this.uuid = PacketTypes.readUuid(byteBuf);
            }
        } else if (protocolVersion >= MCVersion.v1_20_2) {
            this.uuid = PacketTypes.readUuid(byteBuf);
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        PacketTypes.writeString(byteBuf, this.name);
        if (protocolVersion >= MCVersion.v1_19 && protocolVersion <= MCVersion.v1_19_1) {
            final boolean hasKeyData = this.expiresAt != null && this.key != null && this.keySignature != null;
            byteBuf.writeBoolean(hasKeyData);
            if (hasKeyData) {
                byteBuf.writeLong(this.expiresAt.toEpochMilli());
                PacketTypes.writeByteArray(byteBuf, this.key.getEncoded());
                PacketTypes.writeByteArray(byteBuf, this.keySignature);
            }
        }
        if (protocolVersion >= MCVersion.v1_19_1 && protocolVersion <= MCVersion.v1_20) {
            byteBuf.writeBoolean(this.uuid != null);
            if (this.uuid != null) {
                PacketTypes.writeUuid(byteBuf, this.uuid);
            }
        } else if (protocolVersion >= MCVersion.v1_20_2) {
            PacketTypes.writeUuid(byteBuf, this.uuid);
        }
    }

}
