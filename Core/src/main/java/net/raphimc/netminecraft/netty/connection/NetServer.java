package net.raphimc.netminecraft.netty.connection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.raphimc.netminecraft.util.LazyLoadBase;

import java.net.InetSocketAddress;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetServer {

    protected final Supplier<ChannelHandler> handlerSupplier;
    protected final Function<Supplier<ChannelHandler>, ChannelInitializer<Channel>> channelInitializerSupplier;

    protected ChannelFuture channelFuture;

    public NetServer(final Supplier<ChannelHandler> handlerSupplier) {
        this(handlerSupplier, MinecraftChannelInitializer::new);
    }

    public NetServer(final Supplier<ChannelHandler> handlerSupplier, final Function<Supplier<ChannelHandler>, ChannelInitializer<Channel>> channelInitializerSupplier) {
        this.handlerSupplier = handlerSupplier;
        this.channelInitializerSupplier = channelInitializerSupplier;
    }

    public void initialize(final ServerBootstrap bootstrap) {
        if (Epoll.isAvailable()) {
            bootstrap
                    .group(LazyLoadBase.SERVER_EPOLL_PARENT_EVENTLOOP.getValue(), LazyLoadBase.SERVER_EPOLL_CHILD_EVENTLOOP.getValue())
                    .channel(EpollServerSocketChannel.class);
        } else {
            bootstrap
                    .group(LazyLoadBase.SERVER_NIO_PARENT_EVENTLOOP.getValue(), LazyLoadBase.SERVER_NIO_CHILD_EVENTLOOP.getValue())
                    .channel(NioServerSocketChannel.class);
        }

        bootstrap
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.IP_TOS, 0x18)
                .childHandler(this.channelInitializerSupplier.apply(this.handlerSupplier));

        this.channelFuture = bootstrap.register().syncUninterruptibly();
    }

    public void bind(final String host, final int port) {
        this.bind(host, port, true);
    }

    public void bind(final String host, final int port, final boolean blocking) {
        if (this.channelFuture == null) this.initialize(new ServerBootstrap());
        this.getChannel().bind(new InetSocketAddress(host, port)).syncUninterruptibly();

        if (blocking) this.getChannel().closeFuture().syncUninterruptibly();
    }

    public Channel getChannel() {
        if (this.channelFuture == null) return null;
        return this.channelFuture.channel();
    }

    public ChannelFuture getChannelFuture() {
        return this.channelFuture;
    }

}
