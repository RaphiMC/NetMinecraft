/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2026 RK_01/RaphiMC and contributors
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

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.constants.ConnectionState;
import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.PacketDirection;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.UnknownPacket;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PacketRegistryImpl implements PacketRegistry {

    private final boolean isClientside;
    private final int protocolVersion;
    private final Map<MCPackets, Supplier<Packet>> registry = new EnumMap<>(MCPackets.class);
    private final Map<Class<?>, MCPackets> reverseRegistry = new HashMap<>();
    private ConnectionState connectionState = ConnectionState.HANDSHAKING;

    public PacketRegistryImpl(final boolean isClientside, final int protocolVersion) {
        this.isClientside = isClientside;
        this.protocolVersion = protocolVersion;
    }

    @Override
    public final Packet createPacket(final int packetId, final ByteBuf byteBuf) {
        final MCPackets packetType = MCPackets.getPacket(this.connectionState, this.isClientside ? PacketDirection.CLIENTBOUND : PacketDirection.SERVERBOUND, this.protocolVersion, packetId);
        final Packet packet = this.registry.getOrDefault(packetType, () -> new UnknownPacket(packetId)).get();
        packet.read(byteBuf, this.protocolVersion);
        return packet;
    }

    @Override
    public int getPacketId(final Packet packet) {
        if (packet instanceof UnknownPacket) {
            return ((UnknownPacket) packet).packetId;
        }

        final Class<?> packetClass = packet.getClass();
        if (!this.reverseRegistry.containsKey(packetClass)) {
            throw new IllegalArgumentException("Packet " + packetClass.getSimpleName() + " is not registered in the packet registry");
        }
        final int packetId = this.reverseRegistry.get(packetClass).getId(this.protocolVersion);
        if (packetId == -1) {
            throw new IllegalArgumentException("Packet " + packetClass.getSimpleName() + " is not available for protocol version " + this.protocolVersion);
        }
        return packetId;
    }

    @Override
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public final ConnectionState getConnectionState() {
        return this.connectionState;
    }

    @Override
    public final void setConnectionState(final ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    protected final void registerPacket(final MCPackets packetType, final Supplier<Packet> packetCreator) {
        this.unregisterPacket(packetType);
        final Class<?> packetClass = packetCreator.get().getClass();
        this.registry.put(packetType, packetCreator);
        if (this.reverseRegistry.put(packetClass, packetType) != null) {
            throw new IllegalArgumentException("Packet " + packetClass.getSimpleName() + " is already registered in the packet registry");
        }
    }

    protected final void unregisterPacket(final MCPackets packetType) {
        this.registry.remove(packetType);
        this.reverseRegistry.values().removeIf(packetType::equals);
    }

}
