package net.raphimc.netminecraft.netty.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.raphimc.netminecraft.util.LazyLoadBase;
import net.raphimc.netminecraft.util.ServerAddress;

import java.util.function.Function;
import java.util.function.Supplier;

public class NetClient {

    protected final Supplier<ChannelHandler> handlerSupplier;
    protected final Function<Supplier<ChannelHandler>, ChannelInitializer<Channel>> channelInitializerSupplier;

    protected ChannelFuture channelFuture;

    public NetClient(final Supplier<ChannelHandler> handlerSupplier) {
        this(handlerSupplier, MinecraftChannelInitializer::new);
    }

    public NetClient(final Supplier<ChannelHandler> handlerSupplier, final Function<Supplier<ChannelHandler>, ChannelInitializer<Channel>> channelInitializerSupplier) {
        this.handlerSupplier = handlerSupplier;
        this.channelInitializerSupplier = channelInitializerSupplier;
    }

    public void initialize(final Bootstrap bootstrap) {
        if (Epoll.isAvailable()) {
            bootstrap
                    .group(LazyLoadBase.CLIENT_EPOLL_EVENTLOOP.getValue())
                    .channel(EpollSocketChannel.class);
        } else {
            bootstrap
                    .group(LazyLoadBase.CLIENT_NIO_EVENTLOOP.getValue())
                    .channel(NioSocketChannel.class);
        }

        bootstrap
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .handler(this.channelInitializerSupplier.apply(this.handlerSupplier));

        this.channelFuture = bootstrap.register().syncUninterruptibly();
    }

    public void connect(final ServerAddress serverAddress) {
        if (this.channelFuture == null) this.initialize(new Bootstrap());
        this.getChannel().connect(serverAddress.toSocketAddress()).syncUninterruptibly();
    }

    public Channel getChannel() {
        if (this.channelFuture == null) return null;
        return this.channelFuture.channel();
    }

    public ChannelFuture getChannelFuture() {
        return this.channelFuture;
    }

}
