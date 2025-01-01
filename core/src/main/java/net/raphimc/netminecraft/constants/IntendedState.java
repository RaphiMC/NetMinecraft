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
package net.raphimc.netminecraft.constants;

public enum IntendedState {

    STATUS(ConnectionState.STATUS),
    LOGIN(ConnectionState.LOGIN),
    TRANSFER(ConnectionState.LOGIN),
    ;

    private final ConnectionState connectionState;

    IntendedState(final ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public static IntendedState byId(final int id) {
        if (id < 1 || id - 1 >= values().length) {
            return null;
        }

        return values()[id - 1];
    }

    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

}
