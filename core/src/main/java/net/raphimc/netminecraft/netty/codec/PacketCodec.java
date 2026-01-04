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
package net.raphimc.netminecraft.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import net.raphimc.netminecraft.constants.MCPipeline;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

import java.util.List;

public class PacketCodec extends ByteToMessageCodec<Packet> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        final PacketRegistry packetRegistry = ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get();
        if (packetRegistry == null) {
            out.add(in.readBytes(in.readableBytes()));
            return;
        }

        if (in.readableBytes() != 0) {
            final int packetId = PacketTypes.readVarInt(in);
            final Packet packet = packetRegistry.createPacket(packetId, in);
            out.add(packet);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet in, ByteBuf out) {
        final PacketRegistry packetRegistry = ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get();
        if (packetRegistry == null) {
            throw new IllegalStateException("Can't write Packet without a packet registry");
        }

        PacketTypes.writeVarInt(out, packetRegistry.getPacketId(in));
        in.write(out, packetRegistry.getProtocolVersion());
    }

}
