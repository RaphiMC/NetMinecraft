package net.raphimc.netminecraft.netty.connection;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.raphimc.netminecraft.constants.MCPipeline;

import java.util.function.Supplier;

public class MinecraftChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Supplier<ChannelHandler> handlerSupplier;

    public MinecraftChannelInitializer(final Supplier<ChannelHandler> handlerSupplier) {
        this.handlerSupplier = handlerSupplier;
    }

    @Override
    protected void initChannel(final SocketChannel socketChannel) {
        socketChannel.attr(MCPipeline.ENCRYPTION_ATTRIBUTE_KEY).set(null);
        socketChannel.attr(MCPipeline.COMPRESSION_THRESHOLD_ATTRIBUTE_KEY).set(-1);
        socketChannel.attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).set(null);

        socketChannel.pipeline().addLast(MCPipeline.ENCRYPTION_HANDLER_NAME, MCPipeline.ENCRYPTION_HANDLER.get());
        socketChannel.pipeline().addLast(MCPipeline.SIZER_HANDLER_NAME, MCPipeline.SIZER_HANDLER.get());
        socketChannel.pipeline().addLast(MCPipeline.COMPRESSION_HANDLER_NAME, MCPipeline.COMPRESSION_HANDLER.get());
        socketChannel.pipeline().addLast(MCPipeline.PACKET_CODEC_HANDLER_NAME, MCPipeline.PACKET_CODEC_HANDLER.get());
        socketChannel.pipeline().addLast(MCPipeline.HANDLER_HANDLER_NAME, this.handlerSupplier.get());
    }

}
