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
package net.raphimc.netminecraft.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.lenni0451.mcstructs.nbt.NbtTag;
import net.lenni0451.mcstructs.nbt.io.NbtIO;
import net.lenni0451.mcstructs.nbt.io.NbtReadTracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketTypes {

    private static final int MAX_VAR_INT_BYTES = 5;
    private static final int MAX_VAR_LONG_BYTES = 10;

    public static int getVarIntLength(final int value) {
        for (int i = 1; i < MAX_VAR_INT_BYTES; ++i) {
            if ((value & -1 << i * 7) == 0) {
                return i;
            }
        }

        return MAX_VAR_INT_BYTES;
    }

    public static int readVarInt(final ByteBuf byteBuf) {
        int value = 0;
        int bytes = 0;
        byte in;
        do {
            in = byteBuf.readByte();
            value |= (in & 127) << (bytes++ * 7);
            if (bytes > MAX_VAR_INT_BYTES) {
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

    public static long readVarLong(final ByteBuf byteBuf) {
        long value = 0;
        int bytes = 0;
        byte in;
        do {
            in = byteBuf.readByte();
            value |= (long) (in & 127) << (bytes++ * 7);
            if (bytes > MAX_VAR_LONG_BYTES) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((in & 128) == 128);

        return value;
    }

    public static ByteBuf writeVarLong(final ByteBuf byteBuf, long value) {
        while ((value & -128L) != 0L) {
            byteBuf.writeByte((int) (value & 127L) | 128);
            value >>>= 7;
        }

        return byteBuf.writeByte((int) value);
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
        return readByteArray(byteBuf, Short.MAX_VALUE);
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

    public static int[] readVarIntArray(final ByteBuf byteBuf) {
        return readVarIntArray(byteBuf, Short.MAX_VALUE);
    }

    public static int[] readVarIntArray(final ByteBuf byteBuf, final int maxSize) {
        final int length = readVarInt(byteBuf);
        if (length > maxSize) {
            throw new DecoderException("The received integer array is bigger than the maximum allowed (" + length + " > " + maxSize + ")");
        } else {
            final int[] array = new int[length];
            for (int i = 0; i < length; i++) {
                array[i] = readVarInt(byteBuf);
            }
            return array;
        }
    }

    public static ByteBuf writeVarIntArray(final ByteBuf byteBuf, final int[] array) {
        writeVarInt(byteBuf, array.length);
        for (int i : array) {
            writeVarInt(byteBuf, i);
        }
        return byteBuf;
    }

    public static byte[] readShortByteArray(final ByteBuf byteBuf) {
        return readShortByteArray(byteBuf, Short.MAX_VALUE);
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

    public static NbtTag readNamedTag(final ByteBuf byteBuf) {
        return readNamedTag(byteBuf, new NbtReadTracker());
    }

    public static NbtTag readNamedTag(final ByteBuf byteBuf, final NbtReadTracker readTracker) {
        try {
            return NbtIO.LATEST.read(new ByteBufInputStream(byteBuf), readTracker);
        } catch (IOException e) {
            throw new DecoderException(e);
        }
    }

    public static void writeNamedTag(final ByteBuf byteBuf, final NbtTag nbt) {
        try {
            NbtIO.LATEST.write(new ByteBufOutputStream(byteBuf), "", nbt);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    public static NbtTag readUnnamedTag(final ByteBuf byteBuf) {
        return readUnnamedTag(byteBuf, new NbtReadTracker());
    }

    public static NbtTag readUnnamedTag(final ByteBuf byteBuf, final NbtReadTracker readTracker) {
        try {
            return NbtIO.LATEST.readUnnamed(new ByteBufInputStream(byteBuf), readTracker);
        } catch (IOException e) {
            throw new DecoderException(e);
        }
    }

    public static void writeUnnamedTag(final ByteBuf byteBuf, final NbtTag nbt) {
        try {
            NbtIO.LATEST.writeUnnamed(new ByteBufOutputStream(byteBuf), nbt);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

}
