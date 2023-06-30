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
package net.raphimc.netminecraft.constants;

public enum ConnectionState {

    HANDSHAKING(-1),
    PLAY(0),
    STATUS(1),
    LOGIN(2);

    private final int id;

    ConnectionState(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }


    public static ConnectionState getById(final int id) {
        for (ConnectionState state : ConnectionState.values()) {
            if (state.getId() == id) return state;
        }
        return null;
    }

}
