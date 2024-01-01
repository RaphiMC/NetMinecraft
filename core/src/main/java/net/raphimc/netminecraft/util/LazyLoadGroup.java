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
package net.raphimc.netminecraft.util;

import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public abstract class LazyLoadGroup<T> {

    public static final LazyLoadGroup<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadGroup<NioEventLoopGroup>() {
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty NIO Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadGroup<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new LazyLoadGroup<EpollEventLoopGroup>() {
        protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadGroup<KQueueEventLoopGroup> CLIENT_KQUEUE_EVENTLOOP = new LazyLoadGroup<KQueueEventLoopGroup>() {
        protected KQueueEventLoopGroup load() {
            return new KQueueEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty KQueue Client IO #%d").setDaemon(true).build());
        }
    };

    public static final LazyLoadGroup<NioEventLoopGroup> SERVER_NIO_PARENT_EVENTLOOP = new LazyLoadGroup<NioEventLoopGroup>() {
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadGroup<EpollEventLoopGroup> SERVER_EPOLL_PARENT_EVENTLOOP = new LazyLoadGroup<EpollEventLoopGroup>() {
        protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadGroup<KQueueEventLoopGroup> SERVER_KQUEUE_PARENT_EVENTLOOP = new LazyLoadGroup<KQueueEventLoopGroup>() {
        protected KQueueEventLoopGroup load() {
            return new KQueueEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty KQueue Server IO #%d").setDaemon(true).build());
        }
    };

    public static final LazyLoadGroup<NioEventLoopGroup> SERVER_NIO_CHILD_EVENTLOOP = new LazyLoadGroup<NioEventLoopGroup>() {
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty NIO Server Child IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadGroup<EpollEventLoopGroup> SERVER_EPOLL_CHILD_EVENTLOOP = new LazyLoadGroup<EpollEventLoopGroup>() {
        protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server Child IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadGroup<KQueueEventLoopGroup> SERVER_KQUEUE_CHILD_EVENTLOOP = new LazyLoadGroup<KQueueEventLoopGroup>() {
        protected KQueueEventLoopGroup load() {
            return new KQueueEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty KQueue Server Child IO #%d").setDaemon(true).build());
        }
    };


    private final Object loadLock = new Object();

    private T value;
    private boolean isLoaded = false;

    public T get() {
        synchronized (this.loadLock) {
            if (!this.isLoaded) {
                this.isLoaded = true;
                this.value = this.load();
            }
        }

        return this.value;
    }

    protected abstract T load();

}
