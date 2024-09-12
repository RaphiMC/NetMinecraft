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

public class S2CLoginHelloPacket implements Packet {

    public String serverId;
    public byte[] publicKey;
    public byte[] nonce;

    public boolean authenticate;

    public S2CLoginHelloPacket() {
    }

    public S2CLoginHelloPacket(final String serverId, final byte[] publicKey, final byte[] nonce) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.nonce = nonce;
    }

    public S2CLoginHelloPacket(final String serverId, final byte[] publicKey, final byte[] nonce, final boolean authenticate) {
        this(serverId, publicKey, nonce);
        this.authenticate = authenticate;
    }

    @Override
    public void read(final ByteBuf byteBuf, final int protocolVersion) {
        this.serverId = PacketTypes.readString(byteBuf, 20);
        if (protocolVersion <= MCVersion.v1_7_6) {
            this.publicKey = PacketTypes.readShortByteArray(byteBuf);
            this.nonce = PacketTypes.readShortByteArray(byteBuf);
        } else {
            this.publicKey = PacketTypes.readByteArray(byteBuf);
            this.nonce = PacketTypes.readByteArray(byteBuf);
        }
        if (protocolVersion >= MCVersion.v1_20_5) {
            this.authenticate = byteBuf.readBoolean();
        }
    }

    @Override
    public void write(final ByteBuf byteBuf, final int protocolVersion) {
        PacketTypes.writeString(byteBuf, this.serverId);
        if (protocolVersion <= MCVersion.v1_7_6) {
            PacketTypes.writeShortByteArray(byteBuf, this.publicKey);
            PacketTypes.writeShortByteArray(byteBuf, this.nonce);
        } else {
            PacketTypes.writeByteArray(byteBuf, this.publicKey);
            PacketTypes.writeByteArray(byteBuf, this.nonce);
        }
        if (protocolVersion >= MCVersion.v1_20_5) {
            byteBuf.writeBoolean(this.authenticate);
        }
    }

}
