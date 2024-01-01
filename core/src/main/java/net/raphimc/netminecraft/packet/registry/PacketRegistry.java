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
package net.raphimc.netminecraft.packet.registry;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.UnknownPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PacketRegistry {

    private final boolean clientside;

    public PacketRegistry(final boolean clientside) {
        this.clientside = clientside;
    }

    private final Map<Integer, Supplier<IPacket>> C2S_ID_PACKET_REGISTRY = new HashMap<>();
    private final Map<Integer, Supplier<IPacket>> S2C_ID_PACKET_REGISTRY = new HashMap<>();

    private final Map<Class<? extends IPacket>, Integer> C2S_PACKET_ID_REGISTRY = new HashMap<>();
    private final Map<Class<? extends IPacket>, Integer> S2C_PACKET_ID_REGISTRY = new HashMap<>();

    protected final void registerC2SPacket(final int packetId, final Supplier<IPacket> packetSupplier) {
        this.unregisterC2SPacket(packetId);
        C2S_PACKET_ID_REGISTRY.put(packetSupplier.get().getClass(), packetId);
        C2S_ID_PACKET_REGISTRY.put(packetId, packetSupplier);
    }

    protected final void unregisterC2SPacket(final int packetId) {
        C2S_PACKET_ID_REGISTRY.entrySet().removeIf(e -> e.getValue() == packetId);
        C2S_ID_PACKET_REGISTRY.remove(packetId);
    }

    protected final void registerS2CPacket(final int packetId, final Supplier<IPacket> packetSupplier) {
        this.unregisterS2CPacket(packetId);
        S2C_PACKET_ID_REGISTRY.put(packetSupplier.get().getClass(), packetId);
        S2C_ID_PACKET_REGISTRY.put(packetId, packetSupplier);
    }

    protected final void unregisterS2CPacket(final int packetId) {
        S2C_PACKET_ID_REGISTRY.entrySet().removeIf(e -> e.getValue() == packetId);
        S2C_ID_PACKET_REGISTRY.remove(packetId);
    }


    public final IPacket getPacketById(final int packetId) {
        if (this.clientside) return S2C_ID_PACKET_REGISTRY.getOrDefault(packetId, UnknownPacket::new).get();
        else return C2S_ID_PACKET_REGISTRY.getOrDefault(packetId, UnknownPacket::new).get();
    }

    @SuppressWarnings("unchecked")
    public final Class<? extends IPacket> getTargetClassByPacket(final Class<? extends IPacket> packetClass) {
        Class<?> clazz = packetClass;
        do {
            if (C2S_PACKET_ID_REGISTRY.containsKey(clazz) || S2C_PACKET_ID_REGISTRY.containsKey(clazz)) return (Class<? extends IPacket>) clazz;
            clazz = clazz.getSuperclass();
        } while (!IPacket.class.equals(clazz) && clazz != null);
        return packetClass;
    }

    public final int getIdByPacket(final Class<? extends IPacket> packetClass) {
        if (this.clientside) return C2S_PACKET_ID_REGISTRY.getOrDefault(packetClass, -1);
        else return S2C_PACKET_ID_REGISTRY.getOrDefault(packetClass, -1);
    }

}
