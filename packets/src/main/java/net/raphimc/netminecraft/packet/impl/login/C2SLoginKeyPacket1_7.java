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
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginKeyPacket1_7 implements IPacket {

    public byte[] encryptedSecretKey;
    public byte[] encryptedNonce;

    public C2SLoginKeyPacket1_7() {
    }

    public C2SLoginKeyPacket1_7(final byte[] encryptedSecretKey, final byte[] encryptedNonce) {
        this.encryptedSecretKey = encryptedSecretKey;
        this.encryptedNonce = encryptedNonce;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.encryptedSecretKey = PacketTypes.readShortByteArray(byteBuf);
        this.encryptedNonce = PacketTypes.readShortByteArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeShortByteArray(byteBuf, this.encryptedSecretKey);
        PacketTypes.writeShortByteArray(byteBuf, this.encryptedNonce);
    }

}
