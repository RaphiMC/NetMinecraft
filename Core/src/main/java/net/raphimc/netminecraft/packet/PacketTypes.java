package net.raphimc.netminecraft.packet;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketTypes {

    private static final int MAX_VAR_INT_LENGTH = 5;

    public static int getVarIntLength(final int value) {
        for (int i = 1; i < MAX_VAR_INT_LENGTH; ++i) {
            if ((value & -1 << i * 7) == 0) {
                return i;
            }
        }

        return MAX_VAR_INT_LENGTH;
    }

    public static int readVarInt(final ByteBuf byteBuf) {
        int value = 0;
        int bytes = 0;
        byte in;
        do {
            in = byteBuf.readByte();
            value |= (in & 127) << (bytes++ * 7);
            if (bytes > MAX_VAR_INT_LENGTH) {
                throw new RuntimeException("VarInt too big");
            }

        } while ((in & 128) == 128);

        return value;
    }

    public static ByteBuf writeVarInt(final ByteBuf byteBuf, int value) {
        while ((value & -128) != 0) {
            byteBuf.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        return byteBuf.writeByte(value);
    }

    public static String readString(final ByteBuf byteBuf, final int maxLength) {
        final int length = readVarInt(byteBuf);
        if (length > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")");
        } else if (length < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            final String string = byteBuf.toString(byteBuf.readerIndex(), length, StandardCharsets.UTF_8);
            byteBuf.skipBytes(length);
            if (string.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + length + " > " + maxLength + ")");
            }

            return string;
        }
    }

    public static ByteBuf writeString(final ByteBuf byteBuf, final String string) {
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        return writeVarInt(byteBuf, bytes.length).writeBytes(bytes);
    }

    public static byte[] readByteArray(final ByteBuf byteBuf) {
        return readByteArray(byteBuf, byteBuf.readableBytes());
    }

    public static byte[] readByteArray(final ByteBuf byteBuf, final int maxSize) {
        final int length = readVarInt(byteBuf);
        if (length > maxSize) {
            throw new DecoderException("The received byte array is bigger than the maximum allowed (" + length + " > " + maxSize + ")");
        } else {
            final byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            return bytes;
        }
    }

    public static ByteBuf writeByteArray(final ByteBuf byteBuf, final byte[] array) {
        return writeVarInt(byteBuf, array.length).writeBytes(array);
    }

    public static byte[] readShortByteArray(final ByteBuf byteBuf) {
        return readShortByteArray(byteBuf, byteBuf.readableBytes());
    }

    public static byte[] readShortByteArray(final ByteBuf byteBuf, final int maxSize) {
        final int length = byteBuf.readShort();
        if (length > maxSize) {
            throw new DecoderException("The received byte array is bigger than the maximum allowed (" + length + " > " + maxSize + ")");
        } else {
            final byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            return bytes;
        }
    }

    public static ByteBuf writeShortByteArray(final ByteBuf byteBuf, final byte[] array) {
        return byteBuf.writeShort(array.length).writeBytes(array);
    }

    public static UUID readUuid(final ByteBuf byteBuf) {
        return new UUID(byteBuf.readLong(), byteBuf.readLong());
    }

    public static ByteBuf writeUuid(final ByteBuf byteBuf, final UUID uuid) {
        return byteBuf.writeLong(uuid.getMostSignificantBits()).writeLong(uuid.getLeastSignificantBits());
    }

    public static byte[] readReadableBytes(final ByteBuf byteBuf) {
        final byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }

}
