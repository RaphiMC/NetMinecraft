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
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginKeyPacket implements Packet {

    public byte[] encryptedSecretKey;
    public byte[] encryptedNonce;

    public Long salt;
    public byte[] signature;

    public C2SLoginKeyPacket() {
    }

    public C2SLoginKeyPacket(final byte[] encryptedSecretKey, final byte[] encryptedNonce) {
        this.encryptedSecretKey = encryptedSecretKey;
        this.encryptedNonce = encryptedNonce;
    }

    public C2SLoginKeyPacket(final byte[] encryptedSecretKey, final long salt, final byte[] signature) {
        this(encryptedSecretKey, null);
        this.salt = salt;
        this.signature = signature;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_7_6) {
            this.encryptedSecretKey = PacketTypes.readShortByteArray(byteBuf);
            this.encryptedNonce = PacketTypes.readShortByteArray(byteBuf);
        } else {
            this.encryptedSecretKey = PacketTypes.readByteArray(byteBuf);
            if (protocolVersion >= MCVersion.v1_19 && protocolVersion <= MCVersion.v1_19_1) {
                if (!byteBuf.readBoolean()) {
                    this.salt = byteBuf.readLong();
                    this.signature = PacketTypes.readByteArray(byteBuf);
                } else {
                    this.encryptedNonce = PacketTypes.readByteArray(byteBuf);
                }
            } else {
                this.encryptedNonce = PacketTypes.readByteArray(byteBuf);
            }
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        if (protocolVersion <= MCVersion.v1_7_6) {
            PacketTypes.writeShortByteArray(byteBuf, this.encryptedSecretKey);
            PacketTypes.writeShortByteArray(byteBuf, this.encryptedNonce);
        } else {
            PacketTypes.writeByteArray(byteBuf, this.encryptedSecretKey);
            if (protocolVersion >= MCVersion.v1_19 && protocolVersion <= MCVersion.v1_19_1) {
                final boolean isSigned = this.salt != null && this.signature != null;
                byteBuf.writeBoolean(!isSigned);
                if (isSigned) {
                    byteBuf.writeLong(this.salt);
                    PacketTypes.writeByteArray(byteBuf, this.signature);
                } else {
                    PacketTypes.writeByteArray(byteBuf, this.encryptedNonce);
                }
            } else {
                PacketTypes.writeByteArray(byteBuf, this.encryptedNonce);
            }
        }
    }

}
