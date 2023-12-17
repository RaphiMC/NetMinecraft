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
package net.raphimc.netminecraft.util;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;

import java.net.SocketAddress;

public enum ChannelType {

    NIO(NioSocketChannel.class, NioDatagramChannel.class, LazyLoadGroup.CLIENT_NIO_EVENTLOOP, NioServerSocketChannel.class, NioDatagramChannel.class, LazyLoadGroup.SERVER_NIO_CHILD_EVENTLOOP, LazyLoadGroup.SERVER_NIO_PARENT_EVENTLOOP),
    EPOLL(EpollSocketChannel.class, EpollDatagramChannel.class, LazyLoadGroup.CLIENT_EPOLL_EVENTLOOP, EpollServerSocketChannel.class, EpollDatagramChannel.class, LazyLoadGroup.SERVER_EPOLL_CHILD_EVENTLOOP, LazyLoadGroup.SERVER_EPOLL_PARENT_EVENTLOOP),
    KQUEUE(KQueueSocketChannel.class, KQueueDatagramChannel.class, LazyLoadGroup.CLIENT_KQUEUE_EVENTLOOP, KQueueServerSocketChannel.class, KQueueDatagramChannel.class, LazyLoadGroup.SERVER_KQUEUE_CHILD_EVENTLOOP, LazyLoadGroup.SERVER_KQUEUE_PARENT_EVENTLOOP),

    UNIX_EPOLL(EpollDomainSocketChannel.class, EpollDomainDatagramChannel.class, LazyLoadGroup.CLIENT_EPOLL_EVENTLOOP, EpollServerDomainSocketChannel.class, EpollDomainDatagramChannel.class, LazyLoadGroup.SERVER_EPOLL_CHILD_EVENTLOOP, LazyLoadGroup.SERVER_EPOLL_PARENT_EVENTLOOP),
    UNIX_KQUEUE(KQueueDomainSocketChannel.class, KQueueDomainDatagramChannel.class, LazyLoadGroup.CLIENT_KQUEUE_EVENTLOOP, KQueueServerDomainSocketChannel.class, KQueueDomainDatagramChannel.class, LazyLoadGroup.SERVER_KQUEUE_CHILD_EVENTLOOP, LazyLoadGroup.SERVER_KQUEUE_PARENT_EVENTLOOP),

    ;

    public static ChannelType get() {
        return get(false);
    }

    public static ChannelType get(final SocketAddress address) {
        return get(address instanceof DomainSocketAddress);
    }

    public static ChannelType get(final boolean unix) {
        if (Epoll.isAvailable()) {
            if (unix) {
                return UNIX_EPOLL;
            } else {
                return EPOLL;
            }
        } else if (KQueue.isAvailable()) {
            if (unix) {
                return UNIX_KQUEUE;
            } else {
                return KQUEUE;
            }
        } else {
            if (unix) {
                throw new UnsupportedOperationException("Unix sockets are not supported on this platform");
            }

            return NIO;
        }
    }

    private final Class<? extends Channel> tcpClientChannelClass;
    private final Class<? extends Channel> udpClientChannelClass;
    private final LazyLoadGroup<? extends EventLoopGroup> clientEventLoopGroup;

    private final Class<? extends ServerChannel> tcpServerChannelClass;
    private final Class<? extends Channel> udpServerChannelClass;
    private final LazyLoadGroup<? extends EventLoopGroup> serverParentEventLoopGroup;
    private final LazyLoadGroup<? extends EventLoopGroup> serverChildEventLoopGroup;

    ChannelType(final Class<? extends Channel> tcpClientChannelClass, final Class<? extends Channel> udpClientChannelClass, final LazyLoadGroup<? extends EventLoopGroup> clientEventLoopGroup, final Class<? extends ServerChannel> tcpServerChannelClass, final Class<? extends Channel> udpServerChannelClass, final LazyLoadGroup<? extends EventLoopGroup> serverChildEventLoopGroup, final LazyLoadGroup<? extends EventLoopGroup> serverParentEventLoopGroup) {
        this.tcpClientChannelClass = tcpClientChannelClass;
        this.udpClientChannelClass = udpClientChannelClass;
        this.clientEventLoopGroup = clientEventLoopGroup;
        this.tcpServerChannelClass = tcpServerChannelClass;
        this.udpServerChannelClass = udpServerChannelClass;
        this.serverParentEventLoopGroup = serverParentEventLoopGroup;
        this.serverChildEventLoopGroup = serverChildEventLoopGroup;
    }

    public Class<? extends Channel> tcpClientChannelClass() {
        return this.tcpClientChannelClass;
    }

    public Class<? extends Channel> udpClientChannelClass() {
        return this.udpClientChannelClass;
    }

    public LazyLoadGroup<? extends EventLoopGroup> clientEventLoopGroup() {
        return this.clientEventLoopGroup;
    }

    public Class<? extends ServerChannel> tcpServerChannelClass() {
        return this.tcpServerChannelClass;
    }

    public Class<? extends Channel> udpServerChannelClass() {
        return this.udpServerChannelClass;
    }

    public LazyLoadGroup<? extends EventLoopGroup> serverParentEventLoopGroup() {
        return this.serverParentEventLoopGroup;
    }

    public LazyLoadGroup<? extends EventLoopGroup> serverChildEventLoopGroup() {
        return this.serverChildEventLoopGroup;
    }

}
