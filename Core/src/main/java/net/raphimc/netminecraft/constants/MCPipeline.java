package net.raphimc.netminecraft.constants;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.AttributeKey;
import net.raphimc.netminecraft.netty.codec.*;
import net.raphimc.netminecraft.netty.crypto.AESEncryption;
import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

import java.util.function.Supplier;

public class MCPipeline {

    // Handler names
    public static final String ENCRYPTION_HANDLER_NAME = "encryption";
    public static final String SIZER_HANDLER_NAME = "sizer";
    public static final String COMPRESSION_HANDLER_NAME = "compression";
    public static final String PACKET_CODEC_HANDLER_NAME = "packet_codec";
    public static final String HANDLER_HANDLER_NAME = "handler";

    // Keys to change handler settings
    public static final AttributeKey<Integer> COMPRESSION_THRESHOLD_ATTRIBUTE_KEY = AttributeKey.valueOf("compression_threshold");
    public static final AttributeKey<AESEncryption> ENCRYPTION_ATTRIBUTE_KEY = AttributeKey.valueOf("encryption");
    public static final AttributeKey<PacketRegistry> PACKET_REGISTRY_ATTRIBUTE_KEY = AttributeKey.valueOf("packet_registry");

    // Default handlers
    public static final Supplier<ByteToMessageCodec<ByteBuf>> DEFAULT_ENCRYPTION_HANDLER = PacketCryptor::new;
    public static final Supplier<ByteToMessageCodec<ByteBuf>> DEFAULT_SIZER_HANDLER = PacketSizer::new;
    public static final Supplier<ByteToMessageCodec<ByteBuf>> DEFAULT_COMPRESSION_HANDLER = PacketCompressor::new;
    public static final Supplier<ByteToMessageCodec<IPacket>> DEFAULT_PACKET_CODEC_HANDLER = PacketCodec::new;
    // Optimized handlers
    public static final Supplier<ByteToMessageCodec<ByteBuf>> OPTIMIZED_SIZER_HANDLER = OptimizedPacketSizer::new;

    // Handlers which are getting used
    public static Supplier<ByteToMessageCodec<ByteBuf>> ENCRYPTION_HANDLER;
    public static Supplier<ByteToMessageCodec<ByteBuf>> SIZER_HANDLER;
    public static Supplier<ByteToMessageCodec<ByteBuf>> COMPRESSION_HANDLER;
    public static Supplier<ByteToMessageCodec<IPacket>> PACKET_CODEC_HANDLER;

    static {
        useDefaultPipeline();
    }

    public static void useDefaultPipeline() {
        ENCRYPTION_HANDLER = DEFAULT_ENCRYPTION_HANDLER;
        SIZER_HANDLER = DEFAULT_SIZER_HANDLER;
        COMPRESSION_HANDLER = DEFAULT_COMPRESSION_HANDLER;
        PACKET_CODEC_HANDLER = DEFAULT_PACKET_CODEC_HANDLER;
    }

    public static void useOptimizedPipeline() {
        useDefaultPipeline();
        SIZER_HANDLER = OPTIMIZED_SIZER_HANDLER;
    }

}
