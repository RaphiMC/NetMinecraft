/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2026 RK_01/RaphiMC and contributors
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
package net.raphimc.netminecraft.constants;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.AttributeKey;
import net.raphimc.netminecraft.netty.codec.*;
import net.raphimc.netminecraft.netty.crypto.AESEncryption;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

import java.util.function.Supplier;

public class MCPipeline {

    // Handler names
    public static final String ENCRYPTION_HANDLER_NAME = "encryption";
    public static final String SIZER_HANDLER_NAME = "sizer";
    public static final String FLOW_CONTROL_HANDLER_NAME = "flow-control";
    public static final String COMPRESSION_HANDLER_NAME = "compression";
    public static final String PACKET_CODEC_HANDLER_NAME = "packet-codec";
    public static final String HANDLER_HANDLER_NAME = "handler";

    // Keys to change handler settings
    public static final AttributeKey<Integer> COMPRESSION_THRESHOLD_ATTRIBUTE_KEY = AttributeKey.valueOf("compression_threshold");
    public static final AttributeKey<AESEncryption> ENCRYPTION_ATTRIBUTE_KEY = AttributeKey.valueOf("encryption");
    public static final AttributeKey<PacketRegistry> PACKET_REGISTRY_ATTRIBUTE_KEY = AttributeKey.valueOf("packet_registry");

    // Default handlers
    public static final Supplier<ByteToMessageCodec<ByteBuf>> DEFAULT_ENCRYPTION_HANDLER = PacketCryptor::new;
    public static final Supplier<ByteToMessageCodec<ByteBuf>> DEFAULT_SIZER_HANDLER = PacketSizer::new;
    public static final Supplier<ChannelHandler> DEFAULT_FLOW_CONTROL_HANDLER = NoReadFlowControlHandler::new;
    public static final Supplier<ByteToMessageCodec<ByteBuf>> DEFAULT_COMPRESSION_HANDLER = PacketCompressor::new;
    public static final Supplier<ByteToMessageCodec<Packet>> DEFAULT_PACKET_CODEC_HANDLER = PacketCodec::new;
    // Optimized handlers
    public static final Supplier<ByteToMessageCodec<ByteBuf>> OPTIMIZED_SIZER_HANDLER = OptimizedPacketSizer::new;

    // Handlers which are getting used
    public static Supplier<ByteToMessageCodec<ByteBuf>> ENCRYPTION_HANDLER;
    public static Supplier<ByteToMessageCodec<ByteBuf>> SIZER_HANDLER;
    public static Supplier<ChannelHandler> FLOW_CONTROL_HANDLER;
    public static Supplier<ByteToMessageCodec<ByteBuf>> COMPRESSION_HANDLER;
    public static Supplier<ByteToMessageCodec<Packet>> PACKET_CODEC_HANDLER;

    static {
        useDefaultPipeline();
    }

    public static void useDefaultPipeline() {
        ENCRYPTION_HANDLER = DEFAULT_ENCRYPTION_HANDLER;
        SIZER_HANDLER = DEFAULT_SIZER_HANDLER;
        FLOW_CONTROL_HANDLER = DEFAULT_FLOW_CONTROL_HANDLER;
        COMPRESSION_HANDLER = DEFAULT_COMPRESSION_HANDLER;
        PACKET_CODEC_HANDLER = DEFAULT_PACKET_CODEC_HANDLER;
    }

    public static void useOptimizedPipeline() {
        useDefaultPipeline();
        SIZER_HANDLER = OPTIMIZED_SIZER_HANDLER;
    }

}
