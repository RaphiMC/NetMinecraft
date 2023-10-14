# NetMinecraft
Easy to use low-level networking library for Minecraft.  

## Releases
### Gradle/Maven
To use NetMinecraft with Gradle/Maven you can get it from [Maven central](https://mvnrepository.com/artifact/net.raphimc.netminecraft), [Lenni0451's maven](https://maven.lenni0451.net/#/releases/net/raphimc/netminecraft) or [Jitpack](https://jitpack.io/#RaphiMC/NetMinecraft).
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from this [Jenkins](https://build.lenni0451.net/job/NetMinecraft/).

## Usage
### Client
To create a client you can create a new ``NetClient`` instance. The first parameter is a supplier which returns a ``ChannelHandler`` which will be used to handle the packets.  

To connect to a server with the client you can use the ``connect`` method. (The minecraft srv record will be resolved automatically)

### Server
To create a server you can create a new ``NetServer`` instance. The first parameter is a supplier which returns a ``ChannelHandler`` which will be used to handle the incoming connections.  

To bind the server to a port you can use the ``bind`` method.

### Handling packets
NetMinecraft allows you to either handle packet reading fully yourself or use the built-in packet reading system.  
To read packets yourself you don't have to do anything special. NetMinecraft will decompress and decrypt the packets for you and you can read them from the ``ByteBuf`` in the channel handlers.   
#### Built-in packet reading
The built-in packet reading system requires the ``packets`` submodule to be included into your project.  
It will allow you to read packets from all version of Minecraft, but only from the Handshake, Status and Login state.

To use the built-in packet reading system you have to overwrite the ``ChannelInitializer`` of the ``NetClient`` or ``NetServer`` instance.  

Here is an example for a client:
```java
public class ClientChannelInitializer extends MinecraftChannelInitializer {

    public ClientChannelInitializer(final Supplier<ChannelHandler> handlerSupplier) {
        super(handlerSupplier);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        super.initChannel(socketChannel);
        socketChannel.attr(MCPipeline.PACKET_REGISTRY_ATTRIBUTE_KEY).set(PacketRegistryUtil.getHandshakeRegistry(false));
    }

}
```

From this point on you will receive ``IPacket``s instead of ``ByteBuf``s in your channel handlers.
You need to switch the packet registry when the state changes yourself.

## Example
### Client
```java
NetClient client = new NetClient(new Supplier<ChannelHandler>() {
    @Override
    public ChannelHandler get() {
        return new SimpleChannelInboundHandler<ByteBuf>() {
            long start;

            @Override
            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                super.handlerAdded(ctx);
                start = System.currentTimeMillis();
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                super.channelActive(ctx);
                System.out.println("connected");
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                super.channelInactive(ctx);
                System.out.println("disconnected");
            }

            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                System.out.println("got response in " + (System.currentTimeMillis() - start) + " ms");
                channelHandlerContext.close();
            }
        };
    }
});
ByteBuf handshake = Unpooled.buffer();
PacketTypes.writeVarInt(handshake, 0); // packet id
PacketTypes.writeVarInt(handshake, 47); // protocol version
PacketTypes.writeString(handshake, "localhost"); // server address
handshake.writeShort(25565); // server port
PacketTypes.writeVarInt(handshake, 1); // next state

ByteBuf request = Unpooled.buffer();
PacketTypes.writeVarInt(request, 0); // packet id

client.connect(new ServerAddress("localhost", 25565)).syncUninterruptibly(); // blocking connect
client.getChannel().writeAndFlush(handshake);
client.getChannel().writeAndFlush(request);
client.getChannel().closeFuture().syncUninterruptibly();
```
### Server
```java
NetServer server = new NetServer(new Supplier<ChannelHandler>() {
    @Override
    public ChannelHandler get() {
        return new SimpleChannelInboundHandler<ByteBuf>() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                super.channelActive(ctx);
                System.out.println("New connection from " + ctx.channel().remoteAddress());
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                super.channelInactive(ctx);
                System.out.println(ctx.channel().remoteAddress() + " closed connection");
            }

            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                // Read packets here
            }
        };
    }
});
server.bind("0.0.0.0", 25565, true);
```
### Proxy
```java
final NetServer server = new NetServer(() -> new SimpleChannelInboundHandler<ByteBuf>() {
    private Channel otherChannel;
    private final NetClient client = new NetClient(new Supplier<ChannelHandler>() {
        @Override
        public ChannelHandler get() {
            return new SimpleChannelInboundHandler<ByteBuf>() {

                @Override
                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
                    otherChannel.writeAndFlush(byteBuf.retain());
                }
            };
        }
    }, channelHandlerSupplier -> new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel socketChannel) {
            socketChannel.pipeline().addLast(channelHandlerSupplier.get());
        }
    });

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.otherChannel = ctx.channel();
        this.client.connect(new ServerAddress("localhost", 25566));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        this.client.getChannel().writeAndFlush(byteBuf.retain());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.client.getChannel().close().syncUninterruptibly();
    }
}, channelHandlerSupplier -> new ChannelInitializer<SocketChannel>() {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(channelHandlerSupplier.get());
    }
});
server.bind("0.0.0.0", 25565);
```
