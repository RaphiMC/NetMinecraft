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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import net.raphimc.netminecraft.util.ChannelType;

import java.net.SocketAddress;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetClient {

    protected final Supplier<ChannelHandler> handlerSupplier;
    protected final Function<Supplier<ChannelHandler>, ChannelInitializer<Channel>> channelInitializerSupplier;

    protected ChannelFuture channelFuture;

    public NetClient(final Supplier<ChannelHandler> handlerSupplier) {
        this(handlerSupplier, MinecraftChannelInitializer::new);
    }

    public NetClient(final Supplier<ChannelHandler> handlerSupplier, final Function<Supplier<ChannelHandler>, ChannelInitializer<Channel>> channelInitializerSupplier) {
        this.handlerSupplier = handlerSupplier;
        this.channelInitializerSupplier = channelInitializerSupplier;
    }

    public void initialize(final ChannelType channelType, final Bootstrap bootstrap) {
        bootstrap
                .group(channelType.clientEventLoopGroup().get())
                .channel(channelType.tcpClientChannelClass())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .handler(this.channelInitializerSupplier.apply(this.handlerSupplier));

        this.channelFuture = bootstrap.register().syncUninterruptibly();
    }

    public ChannelFuture connect(final SocketAddress address) {
        if (this.channelFuture == null) {
            this.initialize(ChannelType.get(address), new Bootstrap());
        }

        return this.getChannel().connect(address);
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
