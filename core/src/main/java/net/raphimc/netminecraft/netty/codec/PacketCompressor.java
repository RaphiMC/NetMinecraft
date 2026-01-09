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
import io.netty.handler.codec.DecoderException;
import io.netty.util.Attribute;
import net.raphimc.netminecraft.constants.MCPipeline;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class PacketCompressor extends ByteToMessageCodec<ByteBuf> {

    private static final int MAX_UNCOMPRESSED_SIZE_1_7_2 = 1024 * 1024 * 2;
    private static final int MAX_UNCOMPRESSED_SIZE_1_17_1 = 1024 * 1024 * 8;

    private final byte[] deflateBuffer = new byte[8192];
    private Attribute<Integer> thresholdAttribute;
    private Attribute<PacketRegistry> packetRegistryAttribute;

    // only allocated if needed
    private Deflater deflater;
    private Inflater inflater;

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        this.thresholdAttribute = ctx.channel().attr(MCPipeline.COMPRESSION_THRESHOLD_ATTRIBUTE_KEY);
        this.packetRegistryAttribute = ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY);
    }

    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (this.inflater != null) {
            this.inflater.end();
        }
        if (this.deflater != null) {
            this.deflater.end();
        }
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final int threshold = this.thresholdAttribute.get();
        if (threshold < 0) {
            out.add(in.readBytes(in.readableBytes()));
            return;
        }

        if (in.readableBytes() != 0) {
            final int uncompressedLength = PacketTypes.readVarInt(in);
            if (uncompressedLength == 0) {
                out.add(in.readBytes(in.readableBytes()));
            } else {
                if (uncompressedLength < threshold) {
                    throw new DecoderException("Badly compressed packet - size of " + uncompressedLength + " is below server threshold of " + threshold);
                }
                final int maxUncompressedSize = this.getMaxUncompressedSize();
                if (uncompressedLength > maxUncompressedSize) {
                    throw new DecoderException("Badly compressed packet - size of " + uncompressedLength + " is larger than protocol maximum of " + maxUncompressedSize);
                }
                if (this.inflater == null) {
                    this.inflater = new Inflater();
                }

                final byte[] compressedData = new byte[in.readableBytes()];
                in.readBytes(compressedData);
                this.inflater.setInput(compressedData);
                final byte[] uncompressedData = new byte[uncompressedLength];
                this.inflater.inflate(uncompressedData);
                out.add(in.alloc().buffer().writeBytes(uncompressedData));
                this.inflater.reset();
            }
        }
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) {
        final int threshold = this.thresholdAttribute.get();
        if (threshold < 0) {
            out.writeBytes(in);
            return;
        }

        final int packetSize = in.readableBytes();
        if (packetSize < threshold) {
            PacketTypes.writeVarInt(out, 0);
            out.writeBytes(in);
        } else {
            final int maxUncompressedSize = this.getMaxUncompressedSize();
            if (packetSize > maxUncompressedSize) {
                throw new IllegalArgumentException("Packet too big (is " + packetSize + ", should be less than " + maxUncompressedSize + ")");
            }
            if (this.deflater == null) {
                this.deflater = new Deflater();
            }

            final byte[] uncompressedData = new byte[packetSize];
            in.readBytes(uncompressedData);
            PacketTypes.writeVarInt(out, uncompressedData.length);
            this.deflater.setInput(uncompressedData, 0, packetSize);
            this.deflater.finish();

            while (!this.deflater.finished()) {
                out.writeBytes(this.deflateBuffer, 0, this.deflater.deflate(this.deflateBuffer));
            }
            this.deflater.reset();
        }
    }

    private int getMaxUncompressedSize() {
        int maxUncompressedSize = MAX_UNCOMPRESSED_SIZE_1_17_1;
        final PacketRegistry packetRegistry = this.packetRegistryAttribute.get();
        if (packetRegistry != null) {
            if (packetRegistry.getProtocolVersion() < MCVersion.v1_17_1) {
                maxUncompressedSize = MAX_UNCOMPRESSED_SIZE_1_7_2;
            }
        }
        return maxUncompressedSize;
    }

}
