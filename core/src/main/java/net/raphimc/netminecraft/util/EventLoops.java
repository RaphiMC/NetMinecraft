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
package net.raphimc.netminecraft.util;

import io.netty.channel.MultiThreadIoEventLoopGroup;

import java.util.EnumMap;

public class EventLoops {

    private static final EnumMap<TransportType, MultiThreadIoEventLoopGroup> CLIENT_EVENT_LOOPS = new EnumMap<>(TransportType.class);
    private static final EnumMap<TransportType, MultiThreadIoEventLoopGroup> SERVER_PARENT_EVENT_LOOPS = new EnumMap<>(TransportType.class);
    private static final EnumMap<TransportType, MultiThreadIoEventLoopGroup> SERVER_CHILD_EVENT_LOOPS = new EnumMap<>(TransportType.class);

    public static synchronized MultiThreadIoEventLoopGroup getClientEventLoop(final TransportType transportType) {
        return CLIENT_EVENT_LOOPS.computeIfAbsent(transportType, transport -> {
            final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder()
                    .setNameFormat("Netty " + transport.name() + " Client IO #%d")
                    .setDaemon(true);
            return new MultiThreadIoEventLoopGroup(0, threadFactoryBuilder.build(), transport.ioHandlerFactorySupplier().get());
        });
    }

    public static synchronized MultiThreadIoEventLoopGroup getServerParentEventLoop(final TransportType transportType) {
        return SERVER_PARENT_EVENT_LOOPS.computeIfAbsent(transportType, transport -> {
            final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder()
                    .setNameFormat("Netty " + transport.name() + " Server Parent IO #%d")
                    .setDaemon(true);
            return new MultiThreadIoEventLoopGroup(0, threadFactoryBuilder.build(), transport.ioHandlerFactorySupplier().get());
        });
    }

    public static synchronized MultiThreadIoEventLoopGroup getServerChildEventLoop(final TransportType transportType) {
        return SERVER_CHILD_EVENT_LOOPS.computeIfAbsent(transportType, transport -> {
            final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder()
                    .setNameFormat("Netty " + transport.name() + " Server Child IO #%d")
                    .setDaemon(true);
            return new MultiThreadIoEventLoopGroup(0, threadFactoryBuilder.build(), transport.ioHandlerFactorySupplier().get());
        });
    }

}
