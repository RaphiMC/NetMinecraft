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
        channel.pipeline().addLast(MCPipeline.COMPRESSION_HANDLER_NAME, MCPipeline.COMPRESSION_HANDLER.get());
        channel.pipeline().addLast(MCPipeline.PACKET_CODEC_HANDLER_NAME, MCPipeline.PACKET_CODEC_HANDLER.get());
        channel.pipeline().addLast(MCPipeline.HANDLER_HANDLER_NAME, this.handlerSupplier.get());
    }

}
