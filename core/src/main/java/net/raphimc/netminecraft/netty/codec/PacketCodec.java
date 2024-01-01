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
package net.raphimc.netminecraft.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import net.raphimc.netminecraft.constants.MCPipeline;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.packet.UnknownPacket;

import java.lang.reflect.Field;
import java.util.List;

public class PacketCodec extends ByteToMessageCodec<IPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get() == null) {
            out.add(in.readBytes(in.readableBytes()));
            return;
        }

        if (in.readableBytes() != 0) {
            final int packetId = PacketTypes.readVarInt(in);
            final IPacket packet = ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get().getPacketById(packetId);
            if (packet instanceof UnknownPacket) ((UnknownPacket) packet).packetId = packetId;
            packet.read(in);
            out.add(packet);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IPacket in, ByteBuf out) {
        if (ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get() == null) {
            throw new IllegalStateException("Can't write IPacket without a packet registry");
        }

        final Class<? extends IPacket> targetClass = ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get().getTargetClassByPacket(in.getClass());
        if (!in.getClass().equals(targetClass)) {
            try {
                final IPacket newPacket = targetClass.newInstance();
                for (Field field : targetClass.getFields()) field.set(newPacket, field.get(in));
                in = newPacket;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        int packetId = ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get().getIdByPacket(targetClass);
        if (in instanceof UnknownPacket) packetId = ((UnknownPacket) in).packetId;

        PacketTypes.writeVarInt(out, packetId);
        in.write(out);
    }

}
