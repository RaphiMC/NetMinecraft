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

import io.netty.channel.Channel;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalIoHandler;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.uring.*;

import java.net.SocketAddress;
import java.util.function.Supplier;

public enum TransportType {

    NIO(NioSocketChannel.class, NioDatagramChannel.class, NioServerSocketChannel.class, NioDatagramChannel.class, NioIoHandler::newFactory, true),
    EPOLL(EpollSocketChannel.class, EpollDatagramChannel.class, EpollServerSocketChannel.class, EpollDatagramChannel.class, EpollIoHandler::newFactory, Epoll.isAvailable()),
    KQUEUE(KQueueSocketChannel.class, KQueueDatagramChannel.class, KQueueServerSocketChannel.class, KQueueDatagramChannel.class, KQueueIoHandler::newFactory, KQueue.isAvailable()),
    IO_URING(IoUringSocketChannel.class, IoUringDatagramChannel.class, IoUringServerSocketChannel.class, IoUringDatagramChannel.class, IoUringIoHandler::newFactory, IoUring.isAvailable()),
    LOCAL(LocalChannel.class, LocalChannel.class, LocalServerChannel.class, LocalServerChannel.class, LocalIoHandler::newFactory, true),

    UNIX_EPOLL(EpollDomainSocketChannel.class, EpollDomainDatagramChannel.class, EpollServerDomainSocketChannel.class, EpollDomainDatagramChannel.class, EpollIoHandler::newFactory, Epoll.isAvailable()),
    UNIX_KQUEUE(KQueueDomainSocketChannel.class, KQueueDomainDatagramChannel.class, KQueueServerDomainSocketChannel.class, KQueueDomainDatagramChannel.class, KQueueIoHandler::newFactory, KQueue.isAvailable()),
    UNIX_IO_URING(IoUringDomainSocketChannel.class, null, IoUringServerDomainSocketChannel.class, null, IoUringIoHandler::newFactory, IoUring.isAvailable()),
    ;

    private static final TransportType[] BEST = {EPOLL, KQUEUE, IO_URING, NIO};
    private static final TransportType[] BEST_UNIX = {UNIX_EPOLL, UNIX_KQUEUE, UNIX_IO_URING};

    public static TransportType getBest() {
        return getBest(false);
    }

    public static TransportType getBest(final SocketAddress address) {
        return getBest(address instanceof DomainSocketAddress);
    }

    public static TransportType getBest(final boolean unixAddress) {
        return getBest(unixAddress ? BEST_UNIX : BEST);
    }

    public static TransportType getBest(final TransportType... transportTypes) {
        for (TransportType transportType : transportTypes) {
            if (transportType.isAvailable()) {
                return transportType;
            }
        }
        throw new UnsupportedOperationException("No available transport types found");
    }

    private final Class<? extends Channel> tcpClientChannelClass;
    private final Class<? extends Channel> udpClientChannelClass;
    private final Class<? extends ServerChannel> tcpServerChannelClass;
    private final Class<? extends Channel> udpServerChannelClass;
    private final Supplier<? extends IoHandlerFactory> ioHandlerFactorySupplier;
    private final boolean available;

    TransportType(final Class<? extends Channel> tcpClientChannelClass, final Class<? extends Channel> udpClientChannelClass, final Class<? extends ServerChannel> tcpServerChannelClass, final Class<? extends Channel> udpServerChannelClass, final Supplier<? extends IoHandlerFactory> ioHandlerFactorySupplier, final boolean available) {
        this.tcpClientChannelClass = tcpClientChannelClass;
        this.udpClientChannelClass = udpClientChannelClass;
        this.tcpServerChannelClass = tcpServerChannelClass;
        this.udpServerChannelClass = udpServerChannelClass;
        this.ioHandlerFactorySupplier = ioHandlerFactorySupplier;
        this.available = available;
    }

    public Class<? extends Channel> tcpClientChannelClass() {
        return this.tcpClientChannelClass;
    }

    public Class<? extends Channel> udpClientChannelClass() {
        return this.udpClientChannelClass;
    }

    public Class<? extends ServerChannel> tcpServerChannelClass() {
        return this.tcpServerChannelClass;
    }

    public Class<? extends Channel> udpServerChannelClass() {
        return this.udpServerChannelClass;
    }

    public Supplier<? extends IoHandlerFactory> ioHandlerFactorySupplier() {
        return this.ioHandlerFactorySupplier;
    }

    public boolean isAvailable() {
        return this.available;
    }

}
