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
package net.raphimc.netminecraft.netty.connection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import net.raphimc.netminecraft.util.ChannelType;

import java.net.SocketAddress;

public class NetServer {

    protected final ChannelInitializer<Channel> channelInitializer;

    protected ChannelFuture channelFuture;

    public NetServer(final ChannelInitializer<Channel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public void initialize(final ChannelType channelType, final ServerBootstrap bootstrap) {
        bootstrap
                .group(channelType.serverParentEventLoopGroup().get(), channelType.serverChildEventLoopGroup().get())
                .channel(channelType.tcpServerChannelClass())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(this.channelInitializer);

        this.channelFuture = bootstrap.register().syncUninterruptibly();
    }

    public void bind(final SocketAddress address) {
        this.bind(address, true);
    }

    public void bind(final SocketAddress address, final boolean blocking) {
        if (this.channelFuture == null) {
            this.initialize(ChannelType.get(address), new ServerBootstrap());
        }
        this.getChannel().bind(address).syncUninterruptibly();

        if (blocking) {
            this.getChannel().closeFuture().syncUninterruptibly();
        }
    }

    public Channel getChannel() {
        if (this.channelFuture == null) {
            return null;
        }

        return this.channelFuture.channel();
    }

    public ChannelFuture getChannelFuture() {
        return this.channelFuture;
    }

}
