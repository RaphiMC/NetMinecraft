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

import java.util.List;

public class PacketCryptor extends ByteToMessageCodec<ByteBuf> {

    private byte[] decryptBuffer = new byte[0];
    private byte[] encryptBuffer = new byte[0];

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (ctx.channel().attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).get() == null) {
            out.add(in.readBytes(in.readableBytes()));
            return;
        }

        final int length = in.readableBytes();
        final byte[] bytes = this.getBytes(in);
        final ByteBuf result = ctx.alloc().heapBuffer(ctx.channel().attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).get().getDecryptOutputSize(length));
        result.writerIndex(ctx.channel().attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).get().decrypt(bytes, 0, length, result.array(), result.arrayOffset()));
        out.add(result);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        if (ctx.channel().attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).get() == null) {
            out.writeBytes(in);
            return;
        }

        final int length = in.readableBytes();
        final byte[] bytes = this.getBytes(in);
        final int outLength = ctx.channel().attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).get().getEncryptOutputSize(length);
        if (this.encryptBuffer.length < outLength) this.encryptBuffer = new byte[outLength];
        out.writeBytes(this.encryptBuffer, 0, ctx.channel().attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).get().encrypt(bytes, 0, length, this.encryptBuffer, 0));
    }

    private byte[] getBytes(final ByteBuf buf) {
        final int length = buf.readableBytes();
        if (this.decryptBuffer.length < length) this.decryptBuffer = new byte[length];
        buf.readBytes(this.decryptBuffer, 0, length);
        return this.decryptBuffer;
    }

}
