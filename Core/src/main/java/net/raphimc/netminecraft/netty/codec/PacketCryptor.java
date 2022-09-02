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
