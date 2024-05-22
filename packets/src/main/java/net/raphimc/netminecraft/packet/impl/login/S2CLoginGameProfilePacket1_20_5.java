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

import java.util.List;
import java.util.UUID;

public class S2CLoginGameProfilePacket1_20_5 extends S2CLoginGameProfilePacket1_19 {

    public boolean strictErrorHandling;

    public S2CLoginGameProfilePacket1_20_5() {
    }

    public S2CLoginGameProfilePacket1_20_5(final UUID uuid, final String name, final List<String[]> properties, final boolean strictErrorHandling) {
        super(uuid, name, properties);
        this.strictErrorHandling = strictErrorHandling;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        super.read(byteBuf);
        this.strictErrorHandling = byteBuf.readBoolean();
    }

    @Override
    public void write(ByteBuf byteBuf) {
        super.write(byteBuf);
        byteBuf.writeBoolean(this.strictErrorHandling);
    }

}
