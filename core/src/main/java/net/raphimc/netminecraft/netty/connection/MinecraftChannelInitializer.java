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
package net.raphimc.netminecraft.netty.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import net.raphimc.netminecraft.constants.MCPipeline;

import java.util.function.Supplier;

public class MinecraftChannelInitializer extends ChannelInitializer<Channel> {

    protected final Supplier<ChannelHandler> handlerSupplier;

    public MinecraftChannelInitializer(final Supplier<ChannelHandler> handlerSupplier) {
        this.handlerSupplier = handlerSupplier;
    }

    @Override
    protected void initChannel(final Channel channel) {
        channel.attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).set(null);
        channel.attr(MCPipeline.COMPRESSION_THRESHOLD_ATTRIBUTE_KEY).set(-1);
        channel.attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).set(null);

        channel.pipeline().addLast(MCPipeline.ENCRYPTION_HANDLER_NAME, MCPipeline.ENCRYPTION_HANDLER.get());
        channel.pipeline().addLast(MCPipeline.SIZER_HANDLER_NAME, MCPipeline.SIZER_HANDLER.get());
        channel.pipeline().addLast(MCPipeline.FLOW_CONTROL_HANDLER_NAME, MCPipeline.FLOW_CONTROL_HANDLER.get());
        channel.pipeline().addLast(MCPipeline.COMPRESSION_HANDLER_NAME, MCPipeline.COMPRESSION_HANDLER.get());
        channel.pipeline().addLast(MCPipeline.PACKET_CODEC_HANDLER_NAME, MCPipeline.PACKET_CODEC_HANDLER.get());
        channel.pipeline().addLast(MCPipeline.HANDLER_HANDLER_NAME, this.handlerSupplier.get());
    }

}
