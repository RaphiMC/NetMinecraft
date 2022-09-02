package net.raphimc.netminecraft.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import net.raphimc.netminecraft.constants.MCPipeline;
import net.raphimc.netminecraft.packet.*;

import java.lang.reflect.Field;
import java.util.List;

public class PacketCodec extends ByteToMessageCodec<IPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get() == null) {
            out.add(new PacketByteBuf(in.readBytes(in.readableBytes())));
            return;
        }

        if (in.readableBytes() != 0) {
            final PacketByteBuf packetByteBuf = new PacketByteBuf(in);
            final int packetId = packetByteBuf.readVarInt();
            final IPacket packet = ctx.channel().attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).get().getPacketById(packetId);
            if (packet instanceof UnknownPacket) ((UnknownPacket) packet).packetId = packetId;
            packet.read(packetByteBuf);
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

        final PacketByteBuf packetByteBuf = new PacketByteBuf();
        packetByteBuf.writeVarInt(packetId);
        in.write(packetByteBuf);
        out.writeBytes(packetByteBuf);
    }

}
