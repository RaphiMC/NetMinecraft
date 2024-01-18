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

public class S2CLoginKeyPacket1_20_5 extends S2CLoginKeyPacket1_8 {

    public boolean authenticate;

    public S2CLoginKeyPacket1_20_5() {
    }

    public S2CLoginKeyPacket1_20_5(final String serverId, final byte[] publicKey, final byte[] nonce, final boolean authenticate) {
        super(serverId, publicKey, nonce);
        this.authenticate = authenticate;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        super.read(byteBuf);
        this.authenticate = byteBuf.readBoolean();
    }

    @Override
    public void write(ByteBuf byteBuf) {
        super.write(byteBuf);
        byteBuf.writeBoolean(this.authenticate);
    }

}
