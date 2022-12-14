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
