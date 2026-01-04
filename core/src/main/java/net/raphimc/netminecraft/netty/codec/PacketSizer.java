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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;
import net.raphimc.netminecraft.packet.PacketTypes;

import java.util.List;

public class PacketSizer extends ByteToMessageCodec<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        final byte[] bytes = new byte[3];

        for (int i = 0; i < bytes.length; ++i) {
            if (!in.isReadable()) {
                in.resetReaderIndex();
                return;
            }

            bytes[i] = in.readByte();
            if (bytes[i] >= 0) {
                final ByteBuf buf = Unpooled.wrappedBuffer(bytes);
                try {
                    final int packetLength = PacketTypes.readVarInt(buf);
                    if (in.readableBytes() < packetLength) {
                        in.resetReaderIndex();
                        return;
                    }
                    out.add(in.readBytes(packetLength));
                } finally {
                    buf.release();
                }
                return;
            }
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        final int packetLength = in.readableBytes();
        final int varIntLength = PacketTypes.getVarIntLength(packetLength);
        if (varIntLength > 3) {
            throw new IllegalArgumentException("Unable to fit " + packetLength + " into " + 3);
        } else {
            out.ensureWritable(varIntLength + packetLength);
            PacketTypes.writeVarInt(out, packetLength);
            out.writeBytes(in, in.readerIndex(), packetLength);
        }
    }

}
