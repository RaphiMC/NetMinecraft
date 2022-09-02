package net.raphimc.netminecraft.packet;

import io.netty.buffer.*;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ByteProcessor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.UUID;

public class PacketByteBuf extends ByteBuf {

    /**
     * The max number of bytes an encoded var int value may use.
     *
     * <p>Its value is {@value}. A regular int value always use 4 bytes in contrast.
     *
     * @see #getVarIntLength(int)
     */
    private static final int MAX_VAR_INT_LENGTH = 5;

    private final ByteBuf byteBuf;

    public PacketByteBuf() {
        this(Unpooled.buffer());
    }

    public PacketByteBuf(final ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    /**
     * Returns the number of bytes needed to encode {@code value} as a
     * {@linkplain #writeVarInt(int) var int}. Guaranteed to be between {@code
     * 1} and {@value #MAX_VAR_INT_LENGTH}.
     *
     * @param value the value to encode
     * @return the number of bytes a var int {@code value} uses
     */
    public static int getVarIntLength(final int value) {
        for (int i = 1; i < MAX_VAR_INT_LENGTH; ++i) {
            if ((value & -1 << i * 7) == 0) {
                return i;
            }
        }

        return MAX_VAR_INT_LENGTH;
    }

    /**
     * Reads a single var int from this buf.
     *
     * @return the value read
     * @see #writeVarInt(int)
     */
    public int readVarInt() {
        int value = 0;
        int i = 0;
        byte b;

        do {
            b = this.byteBuf.readByte();
            value |= (b & 127) << i++ * 7;
            if (i > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b & 128) == 128);

        return value;
    }

    /**
     * Writes a single var int to this buf.
     *
     * <p>Compared to regular ints, var ints may use less bytes (ranging from 1
     * to 5, where regular ints use 4) when representing smaller positive
     * numbers.
     *
     * @param value the value to write
     * @return this buf, for chaining
     * @see #readVarInt()
     * @see #getVarIntLength(int)
     */
    public PacketByteBuf writeVarInt(int value) {
        while ((value & -128) != 0) {
            this.byteBuf.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        this.byteBuf.writeByte(value);
        return this;
    }

    /**
     * Reads a string from this buf. A string is represented by a byte array of
     * its UTF-8 data. The string can have a maximum length of {@code maxLength}.
     *
     * @param maxLength the maximum length of the string read
     * @return the string read
     * @throws io.netty.handler.codec.DecoderException if the string read
     *                                                 is longer than {@code maxLength}
     * @see #writeString(String)
     */
    public String readString(final int maxLength) {
        final int length = this.readVarInt();
        if (length > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")");
        } else if (length < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            final String string = this.byteBuf.toString(this.byteBuf.readerIndex(), length, StandardCharsets.UTF_8);
            this.byteBuf.readerIndex(this.byteBuf.readerIndex() + length);
            if (string.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + length + " > " + maxLength + ")");
            } else {
                return string;
            }
        }
    }

    /**
     * Writes a string to this buf. A string is represented by a byte array of
     * its UTF-8 data.
     *
     * @param string the string to write
     * @return this buf, for chaining
     * @see #readString(int)
     */
    public PacketByteBuf writeString(final String string) {
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        this.writeVarInt(bytes.length);
        this.byteBuf.writeBytes(bytes);
        return this;
    }

    /**
     * Reads an array of primitive bytes from this buf. The array first has a
     * var int indicating its length, followed by the actual bytes. The array
     * does not have a length limit.
     *
     * @return the read byte array
     * @see #readByteArray(int)
     * @see #writeByteArray(byte[])
     */
    public byte[] readByteArray() {
        return this.readByteArray(this.byteBuf.readableBytes());
    }

    /**
     * Reads an array of primitive bytes from this buf. The array first has a
     * var int indicating its length, followed by the actual bytes. The array
     * has a length limit given by {@code maxSize}.
     *
     * @param maxSize the max length of the read array
     * @return the read byte array
     * @throws io.netty.handler.codec.DecoderException if the read array has a
     *                                                 length over {@code maxSize}
     * @see #readByteArray()
     * @see #writeByteArray(byte[])
     */
    public byte[] readByteArray(final int maxSize) {
        int length = this.readVarInt();
        if (length > maxSize) {
            throw new DecoderException("ByteArray with size " + length + " is bigger than allowed " + maxSize);
        } else {
            final byte[] bytes = new byte[length];
            this.byteBuf.readBytes(bytes);
            return bytes;
        }
    }

    /**
     * Writes an array of primitive bytes to this buf. The array first has a
     * var int indicating its length, followed by the actual bytes.
     *
     * @param array the array to write
     * @return this buf, for chaining
     * @see #readByteArray()
     */
    public PacketByteBuf writeByteArray(final byte[] array) {
        this.writeVarInt(array.length);
        this.byteBuf.writeBytes(array);
        return this;
    }

    /**
     * Writes an array of primitive ints to this buf. The array first has a
     * var int indicating its length, followed by the var int entries.
     *
     * @param array the array to write
     * @return this buf, for chaining
     * @implNote An int array has the same format as a list of ints.
     * @see #readIntArray(int)
     * @see #writeIntArray(int[])
     */
    public PacketByteBuf writeIntArray(final int[] array) {
        this.writeVarInt(array.length);
        for (int i : array) {
            this.writeVarInt(i);
        }
        return this;
    }

    /**
     * Reads an array of primitive ints from this buf. The array first has a
     * var int indicating its length, followed by the var int entries. The array
     * does not have a length limit.
     *
     * @return the read byte array
     * @implNote An int array has the same format as a list of ints.
     * @see #readIntArray(int)
     * @see #writeIntArray(int[])
     */
    public int[] readIntArray() {
        return this.readIntArray(this.byteBuf.readableBytes());
    }

    /**
     * Reads an array of primitive ints from this buf. The array first has a
     * var int indicating its length, followed by the var int entries. The array
     * has a length limit given by {@code maxSize}.
     *
     * @param maxSize the max length of the read array
     * @return the read byte array
     * @throws io.netty.handler.codec.DecoderException if the read array has a
     *                                                 length over {@code maxSize}
     * @implNote An int array has the same format as a list of ints.
     * @see #readIntArray()
     * @see #writeIntArray(int[])
     */
    public int[] readIntArray(final int maxSize) {
        final int length = this.readVarInt();
        if (length > maxSize) {
            throw new DecoderException("VarIntArray with size " + length + " is bigger than allowed " + maxSize);
        } else {
            final int[] ints = new int[length];
            for (int j = 0; j < ints.length; ++j) {
                ints[j] = this.readVarInt();
            }
            return ints;
        }
    }

    /**
     * Writes an array of primitive longs to this buf. The array first has a
     * var int indicating its length, followed by the regular long (not var
     * long) values.
     *
     * @param array the array to write
     * @return this buf, for chaining
     * @see #readLongArray()
     */
    public PacketByteBuf writeLongArray(final long[] array) {
        this.writeVarInt(array.length);
        for (long l : array) {
            this.writeLong(l);
        }
        return this;
    }

    /**
     * Reads an array of primitive longs from this buf. The array first has a
     * var int indicating its length, followed by the regular long (not var
     * long) values. The array does not have a length limit.
     *
     * <p>Only when {@code toArray} is not {@code null} and {@code
     * toArray.length} equals to the length var int read will the {@code
     * toArray} be reused and returned; otherwise, a new array
     * of proper size is created.
     *
     * @return the read long array
     * @see #writeLongArray(long[])
     * @see #readLongArray()
     */
    public long[] readLongArray() {
        return this.readLongArray(this.byteBuf.readableBytes() / 8);
    }

    /**
     * Reads an array of primitive longs from this buf. The array first has a
     * var int indicating its length, followed by the regular long (not var
     * long) values. The array has a length limit of {@code maxSize}.
     *
     * <p>Only when {@code toArray} is not {@code null} and {@code
     * toArray.length} equals to the length var int read will the {@code
     * toArray} be reused and returned; otherwise, a new array
     * of proper size is created.
     *
     * @param maxSize the max length of the read array
     * @return the read long array
     * @throws io.netty.handler.codec.DecoderException if the read array has a
     *                                                 length over {@code maxSize}
     * @see #writeLongArray(long[])
     * @see #readLongArray()
     */
    public long[] readLongArray(int maxSize) {
        final int length = this.readVarInt();
        if (length > maxSize) {
            throw new DecoderException("LongArray with size " + length + " is bigger than allowed " + maxSize);
        } else {
            final long[] longs = new long[length];
            for (int j = 0; j < longs.length; ++j) {
                longs[j] = this.byteBuf.readLong();
            }
            return longs;
        }
    }


    /**
     * Writes a UUID (universally unique identifier) to this buf. A UUID is
     * represented by two regular longs.
     *
     * @param uuid the UUID to write
     * @return this buf, for chaining
     * @see #readUuid()
     */
    public PacketByteBuf writeUuid(final UUID uuid) {
        this.byteBuf.writeLong(uuid.getMostSignificantBits());
        this.byteBuf.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    /**
     * Reads a UUID (universally unique identifier) from this buf. A UUID is
     * represented by two regular longs.
     *
     * @return the read UUID
     * @see #writeUuid(UUID)
     */
    public UUID readUuid() {
        return new UUID(this.byteBuf.readLong(), this.byteBuf.readLong());
    }


    /**
     * Returns the number of bytes (octets) this buffer can contain.
     */
    @Override
    public int capacity() {
        return this.byteBuf.capacity();
    }

    /**
     * Adjusts the capacity of this buffer.  If the {@code newCapacity} is less than the current
     * capacity, the content of this buffer is truncated.  If the {@code newCapacity} is greater
     * than the current capacity, the buffer is appended with unspecified data whose length is
     * {@code (newCapacity - currentCapacity)}.
     *
     * @throws IllegalArgumentException if the {@code newCapacity} is greater than {@link #maxCapacity()}
     */
    @Override
    public ByteBuf capacity(int newCapacity) {
        return this.byteBuf.capacity(newCapacity);
    }

    /**
     * Returns the maximum allowed capacity of this buffer. This value provides an upper
     * bound on {@link #capacity()}.
     */
    @Override
    public int maxCapacity() {
        return this.byteBuf.maxCapacity();
    }

    /**
     * Returns the {@link ByteBufAllocator} which created this buffer.
     */
    @Override
    public ByteBufAllocator alloc() {
        return this.byteBuf.alloc();
    }

    /**
     * Returns the <a href="https://en.wikipedia.org/wiki/Endianness">endianness</a>
     * of this buffer.
     *
     * @deprecated use the Little Endian accessors, e.g. {@code getShortLE}, {@code getIntLE}
     * instead of creating a buffer with swapped {@code endianness}.
     */
    @Deprecated
    @Override
    public ByteOrder order() {
        return this.byteBuf.order();
    }

    /**
     * Returns a buffer with the specified {@code endianness} which shares the whole region,
     * indexes, and marks of this buffer.  Modifying the content, the indexes, or the marks of the
     * returned buffer or this buffer affects each other's content, indexes, and marks.  If the
     * specified {@code endianness} is identical to this buffer's byte order, this method can
     * return {@code this}.  This method does not modify {@code readerIndex} or {@code writerIndex}
     * of this buffer.
     *
     * @deprecated use the Little Endian accessors, e.g. {@code getShortLE}, {@code getIntLE}
     * instead of creating a buffer with swapped {@code endianness}.
     */
    @Deprecated
    @Override
    public ByteBuf order(ByteOrder endianness) {
        return this.byteBuf.order(endianness);
    }

    /**
     * Return the underlying buffer instance if this buffer is a wrapper of another buffer.
     *
     * @return {@code null} if this buffer is not a wrapper
     */
    @Override
    public ByteBuf unwrap() {
        return this.byteBuf.unwrap();
    }

    /**
     * Returns {@code true} if and only if this buffer is backed by an
     * NIO direct buffer.
     */
    @Override
    public boolean isDirect() {
        return this.byteBuf.isDirect();
    }

    /**
     * Returns {@code true} if and only if this buffer is read-only.
     */
    @Override
    public boolean isReadOnly() {
        return this.byteBuf.isReadOnly();
    }

    /**
     * Returns a read-only version of this buffer.
     */
    @Override
    public ByteBuf asReadOnly() {
        return this.byteBuf.asReadOnly();
    }

    /**
     * Returns the {@code readerIndex} of this buffer.
     */
    @Override
    public int readerIndex() {
        return this.byteBuf.readerIndex();
    }

    /**
     * Sets the {@code readerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code readerIndex} is
     *                                   less than {@code 0} or
     *                                   greater than {@code this.writerIndex}
     */
    @Override
    public ByteBuf readerIndex(int readerIndex) {
        return this.byteBuf.readerIndex(readerIndex);
    }

    /**
     * Returns the {@code writerIndex} of this buffer.
     */
    @Override
    public int writerIndex() {
        return this.byteBuf.writerIndex();
    }

    /**
     * Sets the {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code writerIndex} is
     *                                   less than {@code this.readerIndex} or
     *                                   greater than {@code this.capacity}
     */
    @Override
    public ByteBuf writerIndex(int writerIndex) {
        return this.byteBuf.writerIndex(writerIndex);
    }

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer
     * in one shot.  This method is useful when you have to worry about the
     * invocation order of {@link #readerIndex(int)} and {@link #writerIndex(int)}
     * methods.  For example, the following code will fail:
     *
     * <pre>
     * // Create a buffer whose readerIndex, writerIndex and capacity are
     * // 0, 0 and 8 respectively.
     * {@link ByteBuf} buf = {@link Unpooled}.buffer(8);
     *
     * // IndexOutOfBoundsException is thrown because the specified
     * // readerIndex (2) cannot be greater than the current writerIndex (0).
     * buf.readerIndex(2);
     * buf.writerIndex(4);
     * </pre>
     * <p>
     * The following code will also fail:
     *
     * <pre>
     * // Create a buffer whose readerIndex, writerIndex and capacity are
     * // 0, 8 and 8 respectively.
     * {@link ByteBuf} buf = {@link Unpooled}.wrappedBuffer(new byte[8]);
     *
     * // readerIndex becomes 8.
     * buf.readLong();
     *
     * // IndexOutOfBoundsException is thrown because the specified
     * // writerIndex (4) cannot be less than the current readerIndex (8).
     * buf.writerIndex(4);
     * buf.readerIndex(2);
     * </pre>
     * <p>
     * By contrast, this method guarantees that it never
     * throws an {@link IndexOutOfBoundsException} as long as the specified
     * indexes meet basic constraints, regardless what the current index
     * values of the buffer are:
     *
     * <pre>
     * // No matter what the current state of the buffer is, the following
     * // call always succeeds as long as the capacity of the buffer is not
     * // less than 4.
     * buf.setIndex(2, 4);
     * </pre>
     *
     * @throws IndexOutOfBoundsException if the specified {@code readerIndex} is less than 0,
     *                                   if the specified {@code writerIndex} is less than the specified
     *                                   {@code readerIndex} or if the specified {@code writerIndex} is
     *                                   greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return this.byteBuf.setIndex(readerIndex, writerIndex);
    }

    /**
     * Returns the number of readable bytes which is equal to
     * {@code (this.writerIndex - this.readerIndex)}.
     */
    @Override
    public int readableBytes() {
        return this.byteBuf.readableBytes();
    }

    /**
     * Returns the number of writable bytes which is equal to
     * {@code (this.capacity - this.writerIndex)}.
     */
    @Override
    public int writableBytes() {
        return this.byteBuf.writableBytes();
    }

    /**
     * Returns the maximum possible number of writable bytes, which is equal to
     * {@code (this.maxCapacity - this.writerIndex)}.
     */
    @Override
    public int maxWritableBytes() {
        return this.byteBuf.maxWritableBytes();
    }

    /**
     * Returns {@code true}
     * if and only if {@code (this.writerIndex - this.readerIndex)} is greater
     * than {@code 0}.
     */
    @Override
    public boolean isReadable() {
        return this.byteBuf.isReadable();
    }

    /**
     * Returns {@code true} if and only if this buffer contains equal to or more than the specified number of elements.
     */
    @Override
    public boolean isReadable(int size) {
        return this.byteBuf.isReadable(size);
    }

    /**
     * Returns {@code true}
     * if and only if {@code (this.capacity - this.writerIndex)} is greater
     * than {@code 0}.
     */
    @Override
    public boolean isWritable() {
        return this.byteBuf.isWritable();
    }

    /**
     * Returns {@code true} if and only if this buffer has enough room to allow writing the specified number of
     * elements.
     */
    @Override
    public boolean isWritable(int size) {
        return this.byteBuf.isWritable(size);
    }

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer to
     * {@code 0}.
     * This method is identical to {@link #setIndex(int, int) setIndex(0, 0)}.
     * <p>
     * Please note that the behavior of this method is different
     * from that of NIO buffer, which sets the {@code limit} to
     * the {@code capacity} of the buffer.
     */
    @Override
    public ByteBuf clear() {
        return this.byteBuf.clear();
    }

    /**
     * Marks the current {@code readerIndex} in this buffer.  You can
     * reposition the current {@code readerIndex} to the marked
     * {@code readerIndex} by calling {@link #resetReaderIndex()}.
     * The initial value of the marked {@code readerIndex} is {@code 0}.
     */
    @Override
    public ByteBuf markReaderIndex() {
        return this.byteBuf.markReaderIndex();
    }

    /**
     * Repositions the current {@code readerIndex} to the marked
     * {@code readerIndex} in this buffer.
     *
     * @throws IndexOutOfBoundsException if the current {@code writerIndex} is less than the marked
     *                                   {@code readerIndex}
     */
    @Override
    public ByteBuf resetReaderIndex() {
        return this.byteBuf.resetReaderIndex();
    }

    /**
     * Marks the current {@code writerIndex} in this buffer.  You can
     * reposition the current {@code writerIndex} to the marked
     * {@code writerIndex} by calling {@link #resetWriterIndex()}.
     * The initial value of the marked {@code writerIndex} is {@code 0}.
     */
    @Override
    public ByteBuf markWriterIndex() {
        return this.byteBuf.markWriterIndex();
    }

    /**
     * Repositions the current {@code writerIndex} to the marked
     * {@code writerIndex} in this buffer.
     *
     * @throws IndexOutOfBoundsException if the current {@code readerIndex} is greater than the marked
     *                                   {@code writerIndex}
     */
    @Override
    public ByteBuf resetWriterIndex() {
        return this.byteBuf.resetWriterIndex();
    }

    /**
     * Discards the bytes between the 0th index and {@code readerIndex}.
     * It moves the bytes between {@code readerIndex} and {@code writerIndex}
     * to the 0th index, and sets {@code readerIndex} and {@code writerIndex}
     * to {@code 0} and {@code oldWriterIndex - oldReaderIndex} respectively.
     * <p>
     * Please refer to the class documentation for more detailed explanation.
     */
    @Override
    public ByteBuf discardReadBytes() {
        return this.byteBuf.discardReadBytes();
    }

    /**
     * Similar to {@link ByteBuf#discardReadBytes()} except that this method might discard
     * some, all, or none of read bytes depending on its internal implementation to reduce
     * overall memory bandwidth consumption at the cost of potentially additional memory
     * consumption.
     */
    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.byteBuf.discardSomeReadBytes();
    }

    /**
     * Expands the buffer {@link #capacity()} to make sure the number of
     * {@linkplain #writableBytes() writable bytes} is equal to or greater than the
     * specified value.  If there are enough writable bytes in this buffer, this method
     * returns with no side effect.
     *
     * @param minWritableBytes the expected minimum number of writable bytes
     * @throws IndexOutOfBoundsException if {@link #writerIndex()} + {@code minWritableBytes} &gt; {@link #maxCapacity()}.
     * @see #capacity(int)
     */
    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        return this.byteBuf.ensureWritable(minWritableBytes);
    }

    /**
     * Expands the buffer {@link #capacity()} to make sure the number of
     * {@linkplain #writableBytes() writable bytes} is equal to or greater than the
     * specified value. Unlike {@link #ensureWritable(int)}, this method returns a status code.
     *
     * @param minWritableBytes the expected minimum number of writable bytes
     * @param force            When {@link #writerIndex()} + {@code minWritableBytes} &gt; {@link #maxCapacity()}:
     *                         <ul>
     *                         <li>{@code true} - the capacity of the buffer is expanded to {@link #maxCapacity()}</li>
     *                         <li>{@code false} - the capacity of the buffer is unchanged</li>
     *                         </ul>
     * @return {@code 0} if the buffer has enough writable bytes, and its capacity is unchanged.
     * {@code 1} if the buffer does not have enough bytes, and its capacity is unchanged.
     * {@code 2} if the buffer has enough writable bytes, and its capacity has been increased.
     * {@code 3} if the buffer does not have enough bytes, but its capacity has been
     * increased to its maximum.
     */
    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return this.byteBuf.ensureWritable(minWritableBytes, force);
    }

    /**
     * Gets a boolean at the specified absolute (@code index) in this buffer.
     * This method does not modify the {@code readerIndex} or {@code writerIndex}
     * of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 1} is greater than {@code this.capacity}
     */
    @Override
    public boolean getBoolean(int index) {
        return this.byteBuf.getBoolean(index);
    }

    /**
     * Gets a byte at the specified absolute {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 1} is greater than {@code this.capacity}
     */
    @Override
    public byte getByte(int index) {
        return this.byteBuf.getByte(index);
    }

    /**
     * Gets an unsigned byte at the specified absolute {@code index} in this
     * buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 1} is greater than {@code this.capacity}
     */
    @Override
    public short getUnsignedByte(int index) {
        return this.byteBuf.getUnsignedByte(index);
    }

    /**
     * Gets a 16-bit short integer at the specified absolute {@code index} in
     * this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public short getShort(int index) {
        return this.byteBuf.getShort(index);
    }

    /**
     * Gets a 16-bit short integer at the specified absolute {@code index} in
     * this buffer in Little Endian Byte Order. This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public short getShortLE(int index) {
        return this.byteBuf.getShortLE(index);
    }

    /**
     * Gets an unsigned 16-bit short integer at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public int getUnsignedShort(int index) {
        return this.byteBuf.getUnsignedShort(index);
    }

    /**
     * Gets an unsigned 16-bit short integer at the specified absolute
     * {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public int getUnsignedShortLE(int index) {
        return this.byteBuf.getUnsignedShortLE(index);
    }

    /**
     * Gets a 24-bit medium integer at the specified absolute {@code index} in
     * this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 3} is greater than {@code this.capacity}
     */
    @Override
    public int getMedium(int index) {
        return this.byteBuf.getMedium(index);
    }

    /**
     * Gets a 24-bit medium integer at the specified absolute {@code index} in
     * this buffer in the Little Endian Byte Order. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 3} is greater than {@code this.capacity}
     */
    @Override
    public int getMediumLE(int index) {
        return this.byteBuf.getMediumLE(index);
    }

    /**
     * Gets an unsigned 24-bit medium integer at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 3} is greater than {@code this.capacity}
     */
    @Override
    public int getUnsignedMedium(int index) {
        return this.byteBuf.getUnsignedMedium(index);
    }

    /**
     * Gets an unsigned 24-bit medium integer at the specified absolute
     * {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 3} is greater than {@code this.capacity}
     */
    @Override
    public int getUnsignedMediumLE(int index) {
        return this.byteBuf.getUnsignedMediumLE(index);
    }

    /**
     * Gets a 32-bit integer at the specified absolute {@code index} in
     * this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public int getInt(int index) {
        return this.byteBuf.getInt(index);
    }

    /**
     * Gets a 32-bit integer at the specified absolute {@code index} in
     * this buffer with Little Endian Byte Order. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public int getIntLE(int index) {
        return this.byteBuf.getIntLE(index);
    }

    /**
     * Gets an unsigned 32-bit integer at the specified absolute {@code index}
     * in this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public long getUnsignedInt(int index) {
        return this.byteBuf.getUnsignedInt(index);
    }

    /**
     * Gets an unsigned 32-bit integer at the specified absolute {@code index}
     * in this buffer in Little Endian Byte Order. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public long getUnsignedIntLE(int index) {
        return this.byteBuf.getUnsignedIntLE(index);
    }

    /**
     * Gets a 64-bit long integer at the specified absolute {@code index} in
     * this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 8} is greater than {@code this.capacity}
     */
    @Override
    public long getLong(int index) {
        return this.byteBuf.getLong(index);
    }

    /**
     * Gets a 64-bit long integer at the specified absolute {@code index} in
     * this buffer in Little Endian Byte Order. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 8} is greater than {@code this.capacity}
     */
    @Override
    public long getLongLE(int index) {
        return this.byteBuf.getLongLE(index);
    }

    /**
     * Gets a 2-byte UTF-16 character at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public char getChar(int index) {
        return this.byteBuf.getChar(index);
    }

    /**
     * Gets a 32-bit floating point number at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public float getFloat(int index) {
        return this.byteBuf.getFloat(index);
    }

    /**
     * Gets a 64-bit floating point number at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 8} is greater than {@code this.capacity}
     */
    @Override
    public double getDouble(int index) {
        return this.byteBuf.getDouble(index);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the specified absolute {@code index} until the destination becomes
     * non-writable.  This method is basically same with
     * {@link #getBytes(int, ByteBuf, int, int)}, except that this
     * method increases the {@code writerIndex} of the destination by the
     * number of the transferred bytes while
     * {@link #getBytes(int, ByteBuf, int, int)} does not.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * the source buffer (i.e. {@code this}).
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + dst.writableBytes} is greater than
     *                                   {@code this.capacity}
     */
    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {
        return this.byteBuf.getBytes(index, dst);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the specified absolute {@code index}.  This method is basically same
     * with {@link #getBytes(int, ByteBuf, int, int)}, except that this
     * method increases the {@code writerIndex} of the destination by the
     * number of the transferred bytes while
     * {@link #getBytes(int, ByteBuf, int, int)} does not.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * the source buffer (i.e. {@code this}).
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}, or
     *                                   if {@code length} is greater than {@code dst.writableBytes}
     */
    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        return this.byteBuf.getBytes(index, dst, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex}
     * of both the source (i.e. {@code this}) and the destination.
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                   if the specified {@code dstIndex} is less than {@code 0},
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}, or
     *                                   if {@code dstIndex + length} is greater than
     *                                   {@code dst.capacity}
     */
    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return this.byteBuf.getBytes(index, dst, dstIndex, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + dst.length} is greater than
     *                                   {@code this.capacity}
     */
    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        return this.byteBuf.getBytes(index, dst);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex}
     * of this buffer.
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                   if the specified {@code dstIndex} is less than {@code 0},
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}, or
     *                                   if {@code dstIndex + length} is greater than
     *                                   {@code dst.length}
     */
    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return this.byteBuf.getBytes(index, dst, dstIndex, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the specified absolute {@code index} until the destination's position
     * reaches its limit.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer while the destination's {@code position} will be increased.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + dst.remaining()} is greater than
     *                                   {@code this.capacity}
     */
    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        return this.byteBuf.getBytes(index, dst);
    }

    /**
     * Transfers this buffer's data to the specified stream starting at the
     * specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}
     * @throws IOException               if the specified stream threw an exception during I/O
     */
    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        return this.byteBuf.getBytes(index, out, length);
    }

    /**
     * Transfers this buffer's data to the specified channel starting at the
     * specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @param length the maximum number of bytes to transfer
     * @return the actual number of bytes written out to the specified channel
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}
     * @throws IOException               if the specified channel threw an exception during I/O
     */
    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return this.byteBuf.getBytes(index, out, length);
    }

    /**
     * Transfers this buffer's data starting at the specified absolute {@code index}
     * to the specified channel starting at the given file position.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer. This method does not modify the channel's position.
     *
     * @param position the file position at which the transfer is to begin
     * @param length   the maximum number of bytes to transfer
     * @return the actual number of bytes written out to the specified channel
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}
     * @throws IOException               if the specified channel threw an exception during I/O
     */
    @Override
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return this.byteBuf.getBytes(index, out, position, length);
    }

    /**
     * Gets a {@link CharSequence} with the given length at the given index.
     *
     * @param length  the length to read
     * @param charset that should be used
     * @return the sequence
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     */
    @Override
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return this.byteBuf.getCharSequence(index, length, charset);
    }

    /**
     * Sets the specified boolean at the specified absolute {@code index} in this
     * buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 1} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        return this.byteBuf.setBoolean(index, value);
    }

    /**
     * Sets the specified byte at the specified absolute {@code index} in this
     * buffer.  The 24 high-order bits of the specified value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 1} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setByte(int index, int value) {
        return this.byteBuf.setByte(index, value);
    }

    /**
     * Sets the specified 16-bit short integer at the specified absolute
     * {@code index} in this buffer.  The 16 high-order bits of the specified
     * value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setShort(int index, int value) {
        return this.byteBuf.setShort(index, value);
    }

    /**
     * Sets the specified 16-bit short integer at the specified absolute
     * {@code index} in this buffer with the Little Endian Byte Order.
     * The 16 high-order bits of the specified value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setShortLE(int index, int value) {
        return this.byteBuf.setShortLE(index, value);
    }

    /**
     * Sets the specified 24-bit medium integer at the specified absolute
     * {@code index} in this buffer.  Please note that the most significant
     * byte is ignored in the specified value.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 3} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setMedium(int index, int value) {
        return this.byteBuf.setMedium(index, value);
    }

    /**
     * Sets the specified 24-bit medium integer at the specified absolute
     * {@code index} in this buffer in the Little Endian Byte Order.
     * Please note that the most significant byte is ignored in the
     * specified value.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 3} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setMediumLE(int index, int value) {
        return this.byteBuf.setMediumLE(index, value);
    }

    /**
     * Sets the specified 32-bit integer at the specified absolute
     * {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setInt(int index, int value) {
        return this.byteBuf.setInt(index, value);
    }

    /**
     * Sets the specified 32-bit integer at the specified absolute
     * {@code index} in this buffer with Little Endian byte order
     * .
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setIntLE(int index, int value) {
        return this.byteBuf.setIntLE(index, value);
    }

    /**
     * Sets the specified 64-bit long integer at the specified absolute
     * {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 8} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setLong(int index, long value) {
        return this.byteBuf.setLong(index, value);
    }

    /**
     * Sets the specified 64-bit long integer at the specified absolute
     * {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 8} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setLongLE(int index, long value) {
        return this.byteBuf.setLongLE(index, value);
    }

    /**
     * Sets the specified 2-byte UTF-16 character at the specified absolute
     * {@code index} in this buffer.
     * The 16 high-order bits of the specified value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 2} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setChar(int index, int value) {
        return this.byteBuf.setChar(index, value);
    }

    /**
     * Sets the specified 32-bit floating-point number at the specified
     * absolute {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 4} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setFloat(int index, float value) {
        return this.byteBuf.setFloat(index, value);
    }

    /**
     * Sets the specified 64-bit floating-point number at the specified
     * absolute {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   {@code index + 8} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setDouble(int index, double value) {
        return this.byteBuf.setDouble(index, value);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index} until the source buffer becomes
     * unreadable.  This method is basically same with
     * {@link #setBytes(int, ByteBuf, int, int)}, except that this
     * method increases the {@code readerIndex} of the source buffer by
     * the number of the transferred bytes while
     * {@link #setBytes(int, ByteBuf, int, int)} does not.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer (i.e. {@code this}).
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + src.readableBytes} is greater than
     *                                   {@code this.capacity}
     */
    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        return this.byteBuf.setBytes(index, src);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index}.  This method is basically same
     * with {@link #setBytes(int, ByteBuf, int, int)}, except that this
     * method increases the {@code readerIndex} of the source buffer by
     * the number of the transferred bytes while
     * {@link #setBytes(int, ByteBuf, int, int)} does not.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer (i.e. {@code this}).
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}, or
     *                                   if {@code length} is greater than {@code src.readableBytes}
     */
    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        return this.byteBuf.setBytes(index, src, length);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex}
     * of both the source (i.e. {@code this}) and the destination.
     *
     * @param srcIndex the first index of the source
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                   if the specified {@code srcIndex} is less than {@code 0},
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}, or
     *                                   if {@code srcIndex + length} is greater than
     *                                   {@code src.capacity}
     */
    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return this.byteBuf.setBytes(index, src, srcIndex, length);
    }

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + src.length} is greater than
     *                                   {@code this.capacity}
     */
    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        return this.byteBuf.setBytes(index, src);
    }

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                   if the specified {@code srcIndex} is less than {@code 0},
     *                                   if {@code index + length} is greater than
     *                                   {@code this.capacity}, or
     *                                   if {@code srcIndex + length} is greater than {@code src.length}
     */
    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return this.byteBuf.setBytes(index, src, srcIndex, length);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index} until the source buffer's position
     * reaches its limit.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + src.remaining()} is greater than
     *                                   {@code this.capacity}
     */
    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        return this.byteBuf.setBytes(index, src);
    }

    /**
     * Transfers the content of the specified source stream to this buffer
     * starting at the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @param length the number of bytes to transfer
     * @return the actual number of bytes read in from the specified channel.
     * {@code -1} if the specified channel is closed.
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + length} is greater than {@code this.capacity}
     * @throws IOException               if the specified stream threw an exception during I/O
     */
    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return this.byteBuf.setBytes(index, in, length);
    }

    /**
     * Transfers the content of the specified source channel to this buffer
     * starting at the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @param length the maximum number of bytes to transfer
     * @return the actual number of bytes read in from the specified channel.
     * {@code -1} if the specified channel is closed.
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + length} is greater than {@code this.capacity}
     * @throws IOException               if the specified channel threw an exception during I/O
     */
    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return this.byteBuf.setBytes(index, in, length);
    }

    /**
     * Transfers the content of the specified source channel starting at the given file position
     * to this buffer starting at the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer. This method does not modify the channel's position.
     *
     * @param position the file position at which the transfer is to begin
     * @param length   the maximum number of bytes to transfer
     * @return the actual number of bytes read in from the specified channel.
     * {@code -1} if the specified channel is closed.
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + length} is greater than {@code this.capacity}
     * @throws IOException               if the specified channel threw an exception during I/O
     */
    @Override
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return this.byteBuf.setBytes(index, in, position, length);
    }

    /**
     * Fills this buffer with <tt>NUL (0x00)</tt> starting at the specified
     * absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @param length the number of <tt>NUL</tt>s to write to the buffer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                   if {@code index + length} is greater than {@code this.capacity}
     */
    @Override
    public ByteBuf setZero(int index, int length) {
        return this.byteBuf.setZero(index, length);
    }

    /**
     * Writes the specified {@link CharSequence} at the current {@code writerIndex} and increases
     * the {@code writerIndex} by the written bytes.
     *
     * @param index    on which the sequence should be written
     * @param sequence to write
     * @param charset  that should be used.
     * @return the written number of bytes.
     * @throws IndexOutOfBoundsException if {@code this.writableBytes} is not large enough to write the whole sequence
     */
    @Override
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return this.byteBuf.setCharSequence(index, sequence, charset);
    }

    /**
     * Gets a boolean at the current {@code readerIndex} and increases
     * the {@code readerIndex} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
     */
    @Override
    public boolean readBoolean() {
        return this.byteBuf.readBoolean();
    }

    /**
     * Gets a byte at the current {@code readerIndex} and increases
     * the {@code readerIndex} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
     */
    @Override
    public byte readByte() {
        return this.byteBuf.readByte();
    }

    /**
     * Gets an unsigned byte at the current {@code readerIndex} and increases
     * the {@code readerIndex} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
     */
    @Override
    public short readUnsignedByte() {
        return this.byteBuf.readUnsignedByte();
    }

    /**
     * Gets a 16-bit short integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
     */
    @Override
    public short readShort() {
        return this.byteBuf.readShort();
    }

    /**
     * Gets a 16-bit short integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
     */
    @Override
    public short readShortLE() {
        return this.byteBuf.readShortLE();
    }

    /**
     * Gets an unsigned 16-bit short integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
     */
    @Override
    public int readUnsignedShort() {
        return this.byteBuf.readUnsignedShort();
    }

    /**
     * Gets an unsigned 16-bit short integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
     */
    @Override
    public int readUnsignedShortLE() {
        return this.byteBuf.readUnsignedShortLE();
    }

    /**
     * Gets a 24-bit medium integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 3}
     */
    @Override
    public int readMedium() {
        return this.byteBuf.readMedium();
    }

    /**
     * Gets a 24-bit medium integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the
     * {@code readerIndex} by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 3}
     */
    @Override
    public int readMediumLE() {
        return this.byteBuf.readMediumLE();
    }

    /**
     * Gets an unsigned 24-bit medium integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 3}
     */
    @Override
    public int readUnsignedMedium() {
        return this.byteBuf.readUnsignedMedium();
    }

    /**
     * Gets an unsigned 24-bit medium integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 3}
     */
    @Override
    public int readUnsignedMediumLE() {
        return this.byteBuf.readUnsignedMediumLE();
    }

    /**
     * Gets a 32-bit integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
     */
    @Override
    public int readInt() {
        return this.byteBuf.readInt();
    }

    /**
     * Gets a 32-bit integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
     */
    @Override
    public int readIntLE() {
        return this.byteBuf.readIntLE();
    }

    /**
     * Gets an unsigned 32-bit integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
     */
    @Override
    public long readUnsignedInt() {
        return this.byteBuf.readUnsignedInt();
    }

    /**
     * Gets an unsigned 32-bit integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
     */
    @Override
    public long readUnsignedIntLE() {
        return this.byteBuf.readUnsignedIntLE();
    }

    /**
     * Gets a 64-bit integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
     */
    @Override
    public long readLong() {
        return this.byteBuf.readLong();
    }

    /**
     * Gets a 64-bit integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
     */
    @Override
    public long readLongLE() {
        return this.byteBuf.readLongLE();
    }

    /**
     * Gets a 2-byte UTF-16 character at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
     */
    @Override
    public char readChar() {
        return this.byteBuf.readChar();
    }

    /**
     * Gets a 32-bit floating point number at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
     */
    @Override
    public float readFloat() {
        return this.byteBuf.readFloat();
    }

    /**
     * Gets a 64-bit floating point number at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
     */
    @Override
    public double readDouble() {
        return this.byteBuf.readDouble();
    }

    /**
     * Transfers this buffer's data to a newly created buffer starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code length}).
     * The returned buffer's {@code readerIndex} and {@code writerIndex} are
     * {@code 0} and {@code length} respectively.
     *
     * @param length the number of bytes to transfer
     * @return the newly created buffer which contains the transferred bytes
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     */
    @Override
    public ByteBuf readBytes(int length) {
        return this.byteBuf.readBytes(length);
    }

    /**
     * Returns a new slice of this buffer's sub-region starting at the current
     * {@code readerIndex} and increases the {@code readerIndex} by the size
     * of the new slice (= {@code length}).
     * <p>
     * Also be aware that this method will NOT call {@link #retain()} and so the
     * reference count will NOT be increased.
     *
     * @param length the size of the new slice
     * @return the newly created slice
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     */
    @Override
    public ByteBuf readSlice(int length) {
        return this.byteBuf.readSlice(length);
    }

    /**
     * Returns a new retained slice of this buffer's sub-region starting at the current
     * {@code readerIndex} and increases the {@code readerIndex} by the size
     * of the new slice (= {@code length}).
     * <p>
     * Note that this method returns a {@linkplain #retain() retained} buffer unlike {@link #readSlice(int)}.
     * This method behaves similarly to {@code readSlice(...).retain()} except that this method may return
     * a buffer implementation that produces less garbage.
     *
     * @param length the size of the new slice
     * @return the newly created slice
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     */
    @Override
    public ByteBuf readRetainedSlice(int length) {
        return this.byteBuf.readRetainedSlice(length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} until the destination becomes
     * non-writable, and increases the {@code readerIndex} by the number of the
     * transferred bytes.  This method is basically same with
     * {@link #readBytes(ByteBuf, int, int)}, except that this method
     * increases the {@code writerIndex} of the destination by the number of
     * the transferred bytes while {@link #readBytes(ByteBuf, int, int)}
     * does not.
     *
     * @throws IndexOutOfBoundsException if {@code dst.writableBytes} is greater than
     *                                   {@code this.readableBytes}
     */
    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        return this.byteBuf.readBytes(dst);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code length}).  This method
     * is basically same with {@link #readBytes(ByteBuf, int, int)},
     * except that this method increases the {@code writerIndex} of the
     * destination by the number of the transferred bytes (= {@code length})
     * while {@link #readBytes(ByteBuf, int, int)} does not.
     *
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes} or
     *                                   if {@code length} is greater than {@code dst.writableBytes}
     */
    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        return this.byteBuf.readBytes(dst, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code length}).
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code dstIndex} is less than {@code 0},
     *                                   if {@code length} is greater than {@code this.readableBytes}, or
     *                                   if {@code dstIndex + length} is greater than
     *                                   {@code dst.capacity}
     */
    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return this.byteBuf.readBytes(dst, dstIndex, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code dst.length}).
     *
     * @throws IndexOutOfBoundsException if {@code dst.length} is greater than {@code this.readableBytes}
     */
    @Override
    public ByteBuf readBytes(byte[] dst) {
        return this.byteBuf.readBytes(dst);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code length}).
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code dstIndex} is less than {@code 0},
     *                                   if {@code length} is greater than {@code this.readableBytes}, or
     *                                   if {@code dstIndex + length} is greater than {@code dst.length}
     */
    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return this.byteBuf.readBytes(dst, dstIndex, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} until the destination's position
     * reaches its limit, and increases the {@code readerIndex} by the
     * number of the transferred bytes.
     *
     * @throws IndexOutOfBoundsException if {@code dst.remaining()} is greater than
     *                                   {@code this.readableBytes}
     */
    @Override
    public ByteBuf readBytes(ByteBuffer dst) {
        return this.byteBuf.readBytes(dst);
    }

    /**
     * Transfers this buffer's data to the specified stream starting at the
     * current {@code readerIndex}.
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     * @throws IOException               if the specified stream threw an exception during I/O
     */
    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        return this.byteBuf.readBytes(out, length);
    }

    /**
     * Transfers this buffer's data to the specified stream starting at the
     * current {@code readerIndex}.
     *
     * @param length the maximum number of bytes to transfer
     * @return the actual number of bytes written out to the specified channel
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     * @throws IOException               if the specified channel threw an exception during I/O
     */
    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return this.byteBuf.readBytes(out, length);
    }

    /**
     * Gets a {@link CharSequence} with the given length at the current {@code readerIndex}
     * and increases the {@code readerIndex} by the given length.
     *
     * @param length  the length to read
     * @param charset that should be used
     * @return the sequence
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     */
    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return this.byteBuf.readCharSequence(length, charset);
    }

    /**
     * Transfers this buffer's data starting at the current {@code readerIndex}
     * to the specified channel starting at the given file position.
     * This method does not modify the channel's position.
     *
     * @param position the file position at which the transfer is to begin
     * @param length   the maximum number of bytes to transfer
     * @return the actual number of bytes written out to the specified channel
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     * @throws IOException               if the specified channel threw an exception during I/O
     */
    @Override
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return this.byteBuf.readBytes(out, position, length);
    }

    /**
     * Increases the current {@code readerIndex} by the specified
     * {@code length} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     */
    @Override
    public ByteBuf skipBytes(int length) {
        return this.byteBuf.skipBytes(length);
    }

    /**
     * Sets the specified boolean at the current {@code writerIndex}
     * and increases the {@code writerIndex} by {@code 1} in this buffer.
     * If {@code this.writableBytes} is less than {@code 1}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeBoolean(boolean value) {
        return this.byteBuf.writeBoolean(value);
    }

    /**
     * Sets the specified byte at the current {@code writerIndex}
     * and increases the {@code writerIndex} by {@code 1} in this buffer.
     * The 24 high-order bits of the specified value are ignored.
     * If {@code this.writableBytes} is less than {@code 1}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeByte(int value) {
        return this.byteBuf.writeByte(value);
    }

    /**
     * Sets the specified 16-bit short integer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
     * in this buffer.  The 16 high-order bits of the specified value are ignored.
     * If {@code this.writableBytes} is less than {@code 2}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeShort(int value) {
        return this.byteBuf.writeShort(value);
    }

    /**
     * Sets the specified 16-bit short integer in the Little Endian Byte
     * Order at the current {@code writerIndex} and increases the
     * {@code writerIndex} by {@code 2} in this buffer.
     * The 16 high-order bits of the specified value are ignored.
     * If {@code this.writableBytes} is less than {@code 2}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeShortLE(int value) {
        return this.byteBuf.writeShortLE(value);
    }

    /**
     * Sets the specified 24-bit medium integer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 3}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 3}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeMedium(int value) {
        return this.byteBuf.writeMedium(value);
    }

    /**
     * Sets the specified 24-bit medium integer at the current
     * {@code writerIndex} in the Little Endian Byte Order and
     * increases the {@code writerIndex} by {@code 3} in this
     * buffer.
     * If {@code this.writableBytes} is less than {@code 3}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeMediumLE(int value) {
        return this.byteBuf.writeMediumLE(value);
    }

    /**
     * Sets the specified 32-bit integer at the current {@code writerIndex}
     * and increases the {@code writerIndex} by {@code 4} in this buffer.
     * If {@code this.writableBytes} is less than {@code 4}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeInt(int value) {
        return this.byteBuf.writeInt(value);
    }

    /**
     * Sets the specified 32-bit integer at the current {@code writerIndex}
     * in the Little Endian Byte Order and increases the {@code writerIndex}
     * by {@code 4} in this buffer.
     * If {@code this.writableBytes} is less than {@code 4}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeIntLE(int value) {
        return this.byteBuf.writeIntLE(value);
    }

    /**
     * Sets the specified 64-bit long integer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 8}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeLong(long value) {
        return this.byteBuf.writeLong(value);
    }

    /**
     * Sets the specified 64-bit long integer at the current
     * {@code writerIndex} in the Little Endian Byte Order and
     * increases the {@code writerIndex} by {@code 8}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 8}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeLongLE(long value) {
        return this.byteBuf.writeLongLE(value);
    }

    /**
     * Sets the specified 2-byte UTF-16 character at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
     * in this buffer.  The 16 high-order bits of the specified value are ignored.
     * If {@code this.writableBytes} is less than {@code 2}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeChar(int value) {
        return this.byteBuf.writeChar(value);
    }

    /**
     * Sets the specified 32-bit floating point number at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 4}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 4}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeFloat(float value) {
        return this.byteBuf.writeFloat(value);
    }

    /**
     * Sets the specified 64-bit floating point number at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 8}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeDouble(double value) {
        return this.byteBuf.writeDouble(value);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} until the source buffer becomes
     * unreadable, and increases the {@code writerIndex} by the number of
     * the transferred bytes.  This method is basically same with
     * {@link #writeBytes(ByteBuf, int, int)}, except that this method
     * increases the {@code readerIndex} of the source buffer by the number of
     * the transferred bytes while {@link #writeBytes(ByteBuf, int, int)}
     * does not.
     * If {@code this.writableBytes} is less than {@code src.readableBytes},
     * {@link #ensureWritable(int)} will be called in an attempt to expand
     * capacity to accommodate.
     */
    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        return this.byteBuf.writeBytes(src);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex}
     * by the number of the transferred bytes (= {@code length}).  This method
     * is basically same with {@link #writeBytes(ByteBuf, int, int)},
     * except that this method increases the {@code readerIndex} of the source
     * buffer by the number of the transferred bytes (= {@code length}) while
     * {@link #writeBytes(ByteBuf, int, int)} does not.
     * If {@code this.writableBytes} is less than {@code length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if {@code length} is greater then {@code src.readableBytes}
     */
    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        return this.byteBuf.writeBytes(src, length);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex}
     * by the number of the transferred bytes (= {@code length}).
     * If {@code this.writableBytes} is less than {@code length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     *
     * @param srcIndex the first index of the source
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code srcIndex} is less than {@code 0}, or
     *                                   if {@code srcIndex + length} is greater than {@code src.capacity}
     */
    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return this.byteBuf.writeBytes(src, srcIndex, length);
    }

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex}
     * by the number of the transferred bytes (= {@code src.length}).
     * If {@code this.writableBytes} is less than {@code src.length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     */
    @Override
    public ByteBuf writeBytes(byte[] src) {
        return this.byteBuf.writeBytes(src);
    }

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex}
     * by the number of the transferred bytes (= {@code length}).
     * If {@code this.writableBytes} is less than {@code length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     *
     * @param srcIndex the first index of the source
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code srcIndex} is less than {@code 0}, or
     *                                   if {@code srcIndex + length} is greater than {@code src.length}
     */
    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return this.byteBuf.writeBytes(src, srcIndex, length);
    }

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} until the source buffer's position
     * reaches its limit, and increases the {@code writerIndex} by the
     * number of the transferred bytes.
     * If {@code this.writableBytes} is less than {@code src.remaining()},
     * {@link #ensureWritable(int)} will be called in an attempt to expand
     * capacity to accommodate.
     */
    @Override
    public ByteBuf writeBytes(ByteBuffer src) {
        return this.byteBuf.writeBytes(src);
    }

    /**
     * Transfers the content of the specified stream to this buffer
     * starting at the current {@code writerIndex} and increases the
     * {@code writerIndex} by the number of the transferred bytes.
     * If {@code this.writableBytes} is less than {@code length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     *
     * @param length the number of bytes to transfer
     * @return the actual number of bytes read in from the specified stream
     * @throws IOException if the specified stream threw an exception during I/O
     */
    @Override
    public int writeBytes(InputStream in, int length) throws IOException {
        return this.byteBuf.writeBytes(in, length);
    }

    /**
     * Transfers the content of the specified channel to this buffer
     * starting at the current {@code writerIndex} and increases the
     * {@code writerIndex} by the number of the transferred bytes.
     * If {@code this.writableBytes} is less than {@code length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     *
     * @param length the maximum number of bytes to transfer
     * @return the actual number of bytes read in from the specified channel
     * @throws IOException if the specified channel threw an exception during I/O
     */
    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return this.byteBuf.writeBytes(in, length);
    }

    /**
     * Transfers the content of the specified channel starting at the given file position
     * to this buffer starting at the current {@code writerIndex} and increases the
     * {@code writerIndex} by the number of the transferred bytes.
     * This method does not modify the channel's position.
     * If {@code this.writableBytes} is less than {@code length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     *
     * @param position the file position at which the transfer is to begin
     * @param length   the maximum number of bytes to transfer
     * @return the actual number of bytes read in from the specified channel
     * @throws IOException if the specified channel threw an exception during I/O
     */
    @Override
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return this.byteBuf.writeBytes(in, position, length);
    }

    /**
     * Fills this buffer with <tt>NUL (0x00)</tt> starting at the current
     * {@code writerIndex} and increases the {@code writerIndex} by the
     * specified {@code length}.
     * If {@code this.writableBytes} is less than {@code length}, {@link #ensureWritable(int)}
     * will be called in an attempt to expand capacity to accommodate.
     *
     * @param length the number of <tt>NUL</tt>s to write to the buffer
     */
    @Override
    public ByteBuf writeZero(int length) {
        return this.byteBuf.writeZero(length);
    }

    /**
     * Writes the specified {@link CharSequence} at the current {@code writerIndex} and increases
     * the {@code writerIndex} by the written bytes.
     * in this buffer.
     * If {@code this.writableBytes} is not large enough to write the whole sequence,
     * {@link #ensureWritable(int)} will be called in an attempt to expand capacity to accommodate.
     *
     * @param sequence to write
     * @param charset  that should be used
     * @return the written number of bytes
     */
    @Override
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return this.byteBuf.writeCharSequence(sequence, charset);
    }

    /**
     * Locates the first occurrence of the specified {@code value} in this
     * buffer. The search takes place from the specified {@code fromIndex}
     * (inclusive) to the specified {@code toIndex} (exclusive).
     * <p>
     * If {@code fromIndex} is greater than {@code toIndex}, the search is
     * performed in a reversed order from {@code fromIndex} (exclusive)
     * down to {@code toIndex} (inclusive).
     * <p>
     * Note that the lower index is always included and higher always excluded.
     * <p>
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @return the absolute index of the first occurrence if found.
     * {@code -1} otherwise.
     */
    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return this.byteBuf.indexOf(fromIndex, toIndex, value);
    }

    /**
     * Locates the first occurrence of the specified {@code value} in this
     * buffer.  The search takes place from the current {@code readerIndex}
     * (inclusive) to the current {@code writerIndex} (exclusive).
     * <p>
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @return the number of bytes between the current {@code readerIndex}
     * and the first occurrence if found. {@code -1} otherwise.
     */
    @Override
    public int bytesBefore(byte value) {
        return this.byteBuf.bytesBefore(value);
    }

    /**
     * Locates the first occurrence of the specified {@code value} in this
     * buffer.  The search starts from the current {@code readerIndex}
     * (inclusive) and lasts for the specified {@code length}.
     * <p>
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @return the number of bytes between the current {@code readerIndex}
     * and the first occurrence if found. {@code -1} otherwise.
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.readableBytes}
     */
    @Override
    public int bytesBefore(int length, byte value) {
        return this.byteBuf.bytesBefore(length, value);
    }

    /**
     * Locates the first occurrence of the specified {@code value} in this
     * buffer.  The search starts from the specified {@code index} (inclusive)
     * and lasts for the specified {@code length}.
     * <p>
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @return the number of bytes between the specified {@code index}
     * and the first occurrence if found. {@code -1} otherwise.
     * @throws IndexOutOfBoundsException if {@code index + length} is greater than {@code this.capacity}
     */
    @Override
    public int bytesBefore(int index, int length, byte value) {
        return this.byteBuf.bytesBefore(index, length, value);
    }

    /**
     * Iterates over the readable bytes of this buffer with the specified {@code processor} in ascending order.
     *
     * @return {@code -1} if the processor iterated to or beyond the end of the readable bytes.
     * The last-visited index If the {@link ByteProcessor#process(byte)} returned {@code false}.
     */
    @Override
    public int forEachByte(ByteProcessor processor) {
        return this.byteBuf.forEachByte(processor);
    }

    /**
     * Iterates over the specified area of this buffer with the specified {@code processor} in ascending order.
     * (i.e. {@code index}, {@code (index + 1)},  .. {@code (index + length - 1)})
     *
     * @return {@code -1} if the processor iterated to or beyond the end of the specified area.
     * The last-visited index If the {@link ByteProcessor#process(byte)} returned {@code false}.
     */
    @Override
    public int forEachByte(int index, int length, ByteProcessor processor) {
        return this.byteBuf.forEachByte(index, length, processor);
    }

    /**
     * Iterates over the readable bytes of this buffer with the specified {@code processor} in descending order.
     *
     * @return {@code -1} if the processor iterated to or beyond the beginning of the readable bytes.
     * The last-visited index If the {@link ByteProcessor#process(byte)} returned {@code false}.
     */
    @Override
    public int forEachByteDesc(ByteProcessor processor) {
        return this.byteBuf.forEachByteDesc(processor);
    }

    /**
     * Iterates over the specified area of this buffer with the specified {@code processor} in descending order.
     * (i.e. {@code (index + length - 1)}, {@code (index + length - 2)}, ... {@code index})
     *
     * @return {@code -1} if the processor iterated to or beyond the beginning of the specified area.
     * The last-visited index If the {@link ByteProcessor#process(byte)} returned {@code false}.
     */
    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return this.byteBuf.forEachByteDesc(index, length, processor);
    }

    /**
     * Returns a copy of this buffer's readable bytes.  Modifying the content
     * of the returned buffer or this buffer does not affect each other at all.
     * This method is identical to {@code buf.copy(buf.readerIndex(), buf.readableBytes())}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     */
    @Override
    public ByteBuf copy() {
        return this.byteBuf.copy();
    }

    /**
     * Returns a copy of this buffer's sub-region.  Modifying the content of
     * the returned buffer or this buffer does not affect each other at all.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     */
    @Override
    public ByteBuf copy(int index, int length) {
        return this.byteBuf.copy(index, length);
    }

    /**
     * Returns a slice of this buffer's readable bytes. Modifying the content
     * of the returned buffer or this buffer affects each other's content
     * while they maintain separate indexes and marks.  This method is
     * identical to {@code buf.slice(buf.readerIndex(), buf.readableBytes())}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * Also be aware that this method will NOT call {@link #retain()} and so the
     * reference count will NOT be increased.
     */
    @Override
    public ByteBuf slice() {
        return this.byteBuf.slice();
    }

    /**
     * Returns a retained slice of this buffer's readable bytes. Modifying the content
     * of the returned buffer or this buffer affects each other's content
     * while they maintain separate indexes and marks.  This method is
     * identical to {@code buf.slice(buf.readerIndex(), buf.readableBytes())}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * Note that this method returns a {@linkplain #retain() retained} buffer unlike {@link #slice()}.
     * This method behaves similarly to {@code slice().retain()} except that this method may return
     * a buffer implementation that produces less garbage.
     */
    @Override
    public ByteBuf retainedSlice() {
        return this.byteBuf.retainedSlice();
    }

    /**
     * Returns a slice of this buffer's sub-region. Modifying the content of
     * the returned buffer or this buffer affects each other's content while
     * they maintain separate indexes and marks.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * Also be aware that this method will NOT call {@link #retain()} and so the
     * reference count will NOT be increased.
     */
    @Override
    public ByteBuf slice(int index, int length) {
        return this.byteBuf.slice(index, length);
    }

    /**
     * Returns a retained slice of this buffer's sub-region. Modifying the content of
     * the returned buffer or this buffer affects each other's content while
     * they maintain separate indexes and marks.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * Note that this method returns a {@linkplain #retain() retained} buffer unlike {@link #slice(int, int)}.
     * This method behaves similarly to {@code slice(...).retain()} except that this method may return
     * a buffer implementation that produces less garbage.
     */
    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return this.byteBuf.retainedSlice(index, length);
    }

    /**
     * Returns a buffer which shares the whole region of this buffer.
     * Modifying the content of the returned buffer or this buffer affects
     * each other's content while they maintain separate indexes and marks.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * The reader and writer marks will not be duplicated. Also be aware that this method will
     * NOT call {@link #retain()} and so the reference count will NOT be increased.
     *
     * @return A buffer whose readable content is equivalent to the buffer returned by {@link #slice()}.
     * However this buffer will share the capacity of the underlying buffer, and therefore allows access to all of the
     * underlying content if necessary.
     */
    @Override
    public ByteBuf duplicate() {
        return this.byteBuf.duplicate();
    }

    /**
     * Returns a retained buffer which shares the whole region of this buffer.
     * Modifying the content of the returned buffer or this buffer affects
     * each other's content while they maintain separate indexes and marks.
     * This method is identical to {@code buf.slice(0, buf.capacity())}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * Note that this method returns a {@linkplain #retain() retained} buffer unlike {@link #slice(int, int)}.
     * This method behaves similarly to {@code duplicate().retain()} except that this method may return
     * a buffer implementation that produces less garbage.
     */
    @Override
    public ByteBuf retainedDuplicate() {
        return this.byteBuf.retainedDuplicate();
    }

    /**
     * Returns the maximum number of NIO {@link ByteBuffer}s that consist this buffer.  Note that {@link #nioBuffers()}
     * or {@link #nioBuffers(int, int)} might return a less number of {@link ByteBuffer}s.
     *
     * @return {@code -1} if this buffer has no underlying {@link ByteBuffer}.
     * the number of the underlying {@link ByteBuffer}s if this buffer has at least one underlying
     * {@link ByteBuffer}.  Note that this method does not return {@code 0} to avoid confusion.
     * @see #nioBuffer()
     * @see #nioBuffer(int, int)
     * @see #nioBuffers()
     * @see #nioBuffers(int, int)
     */
    @Override
    public int nioBufferCount() {
        return this.byteBuf.nioBufferCount();
    }

    /**
     * Exposes this buffer's readable bytes as an NIO {@link ByteBuffer}. The returned buffer
     * either share or contains the copied content of this buffer, while changing the position
     * and limit of the returned NIO buffer does not affect the indexes and marks of this buffer.
     * This method is identical to {@code buf.nioBuffer(buf.readerIndex(), buf.readableBytes())}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     * Please note that the returned NIO buffer will not see the changes of this buffer if this buffer
     * is a dynamic buffer and it adjusted its capacity.
     *
     * @throws UnsupportedOperationException if this buffer cannot create a {@link ByteBuffer} that shares the content with itself
     * @see #nioBufferCount()
     * @see #nioBuffers()
     * @see #nioBuffers(int, int)
     */
    @Override
    public ByteBuffer nioBuffer() {
        return this.byteBuf.nioBuffer();
    }

    /**
     * Exposes this buffer's sub-region as an NIO {@link ByteBuffer}. The returned buffer
     * either share or contains the copied content of this buffer, while changing the position
     * and limit of the returned NIO buffer does not affect the indexes and marks of this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     * Please note that the returned NIO buffer will not see the changes of this buffer if this buffer
     * is a dynamic buffer and it adjusted its capacity.
     *
     * @throws UnsupportedOperationException if this buffer cannot create a {@link ByteBuffer} that shares the content with itself
     * @see #nioBufferCount()
     * @see #nioBuffers()
     * @see #nioBuffers(int, int)
     */
    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return this.byteBuf.nioBuffer(index, length);
    }

    /**
     * Internal use only: Exposes the internal NIO buffer.
     */
    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return this.byteBuf.internalNioBuffer(index, length);
    }

    /**
     * Exposes this buffer's readable bytes as an NIO {@link ByteBuffer}'s. The returned buffer
     * either share or contains the copied content of this buffer, while changing the position
     * and limit of the returned NIO buffer does not affect the indexes and marks of this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     * Please note that the returned NIO buffer will not see the changes of this buffer if this buffer
     * is a dynamic buffer and it adjusted its capacity.
     *
     * @throws UnsupportedOperationException if this buffer cannot create a {@link ByteBuffer} that shares the content with itself
     * @see #nioBufferCount()
     * @see #nioBuffer()
     * @see #nioBuffer(int, int)
     */
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.byteBuf.nioBuffers();
    }

    /**
     * Exposes this buffer's bytes as an NIO {@link ByteBuffer}'s for the specified index and length
     * The returned buffer either share or contains the copied content of this buffer, while changing
     * the position and limit of the returned NIO buffer does not affect the indexes and marks of this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of this buffer. Please note that the
     * returned NIO buffer will not see the changes of this buffer if this buffer is a dynamic
     * buffer and it adjusted its capacity.
     *
     * @throws UnsupportedOperationException if this buffer cannot create a {@link ByteBuffer} that shares the content with itself
     * @see #nioBufferCount()
     * @see #nioBuffer()
     * @see #nioBuffer(int, int)
     */
    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return this.byteBuf.nioBuffers(index, length);
    }

    /**
     * Returns {@code true} if and only if this buffer has a backing byte array.
     * If this method returns true, you can safely call {@link #array()} and
     * {@link #arrayOffset()}.
     */
    @Override
    public boolean hasArray() {
        return this.byteBuf.hasArray();
    }

    /**
     * Returns the backing byte array of this buffer.
     *
     * @throws UnsupportedOperationException if there no accessible backing byte array
     */
    @Override
    public byte[] array() {
        return this.byteBuf.array();
    }

    /**
     * Returns the offset of the first byte within the backing byte array of
     * this buffer.
     *
     * @throws UnsupportedOperationException if there no accessible backing byte array
     */
    @Override
    public int arrayOffset() {
        return this.byteBuf.arrayOffset();
    }

    /**
     * Returns {@code true} if and only if this buffer has a reference to the low-level memory address that points
     * to the backing data.
     */
    @Override
    public boolean hasMemoryAddress() {
        return this.byteBuf.hasMemoryAddress();
    }

    /**
     * Returns the low-level memory address that point to the first byte of ths backing data.
     *
     * @throws UnsupportedOperationException if this buffer does not support accessing the low-level memory address
     */
    @Override
    public long memoryAddress() {
        return this.byteBuf.memoryAddress();
    }

    /**
     * Decodes this buffer's readable bytes into a string with the specified
     * character set name.  This method is identical to
     * {@code buf.toString(buf.readerIndex(), buf.readableBytes(), charsetName)}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     *
     * @throws UnsupportedCharsetException if the specified character set name is not supported by the
     *                                     current VM
     */
    @Override
    public String toString(Charset charset) {
        return this.byteBuf.toString(charset);
    }

    /**
     * Decodes this buffer's sub-region into a string with the specified
     * character set.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     */
    @Override
    public String toString(int index, int length, Charset charset) {
        return this.byteBuf.toString(index, length, charset);
    }

    /**
     * Returns a hash code which was calculated from the content of this
     * buffer.  If there's a byte array which is
     * {@linkplain #equals(Object) equal to} this array, both arrays should
     * return the same value.
     */
    @Override
    public int hashCode() {
        return this.byteBuf.hashCode();
    }

    /**
     * Determines if the content of the specified buffer is identical to the
     * content of this array.  'Identical' here means:
     * <ul>
     * <li>the size of the contents of the two buffers are same and</li>
     * <li>every single byte of the content of the two buffers are same.</li>
     * </ul>
     * Please note that it does not compare {@link #readerIndex()} nor
     * {@link #writerIndex()}.  This method also returns {@code false} for
     * {@code null} and an object which is not an instance of
     * {@link ByteBuf} type.
     */
    @Override
    public boolean equals(Object obj) {
        return this.byteBuf.equals(obj);
    }

    /**
     * Compares the content of the specified buffer to the content of this
     * buffer. Comparison is performed in the same manner with the string
     * comparison functions of various languages such as {@code strcmp},
     * {@code memcmp} and {@link String#compareTo(String)}.
     */
    @Override
    public int compareTo(ByteBuf buffer) {
        return this.byteBuf.compareTo(buffer);
    }

    /**
     * Returns the string representation of this buffer.  This method does not
     * necessarily return the whole content of the buffer but returns
     * the values of the key properties such as {@link #readerIndex()},
     * {@link #writerIndex()} and {@link #capacity()}.
     */
    @Override
    public String toString() {
        return this.byteBuf.toString();
    }

    @Override
    public ByteBuf retain(int increment) {
        return this.byteBuf.retain(increment);
    }

    @Override
    public ByteBuf retain() {
        return this.byteBuf.retain();
    }

    @Override
    public ByteBuf touch() {
        return this.byteBuf.touch();
    }

    @Override
    public ByteBuf touch(Object hint) {
        return this.byteBuf.touch(hint);
    }

    /**
     * Returns the reference count of this object.  If {@code 0}, it means this object has been deallocated.
     */
    @Override
    public int refCnt() {
        return this.byteBuf.refCnt();
    }

    /**
     * Decreases the reference count by {@code 1} and deallocates this object if the reference count reaches at
     * {@code 0}.
     *
     * @return {@code true} if and only if the reference count became {@code 0} and this object has been deallocated
     */
    @Override
    public boolean release() {
        return this.byteBuf.release();
    }

    /**
     * Decreases the reference count by the specified {@code decrement} and deallocates this object if the reference
     * count reaches at {@code 0}.
     *
     * @return {@code true} if and only if the reference count became {@code 0} and this object has been deallocated
     */
    @Override
    public boolean release(int decrement) {
        return this.byteBuf.release(decrement);
    }

    public byte[] readReadableBytes() {
        final byte[] bytes = new byte[this.byteBuf.readableBytes()];
        this.byteBuf.readBytes(bytes);
        return bytes;
    }

}

/*
List<String> lines = new ArrayList<>();
for (String s : FileIterable.of("C:/Users/User/Desktop/ByteBuf.java")) {
    lines.add(s.trim());
}

Scanner s = new Scanner(new File("C:/Users/User/Desktop/ByteBuf.java"));
BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/User/Desktop/fucker.txt"));
while (s.hasNextLine()) {
    String line = s.nextLine();
    line = line.trim();
    if (line.contains("abstract") && line.startsWith("public") && line.endsWith(";")) {
        int lineIndex = lines.indexOf(line);

        List<String> temp = new ArrayList<>();
        for (int i = lineIndex - 1; i >= 0; i--) {
            String prepend = lines.get(i);
            if (prepend.isEmpty()) break;
            if (prepend.equals("@Override")) continue;
            temp.add(0, prepend);
        }
        for (String s1 : temp) {
            bw.write(s1);
            bw.newLine();
        }
        bw.write("@Override");
        bw.newLine();
        bw.write(line.substring(0, line.length() - 1).replace("abstract ", ""));
        bw.write(" {");
        bw.newLine();
        bw.write("    ");
        bw.write("return this.byteBuf.");
        {
            String mn = line.substring(0, line.indexOf("("));
            mn = mn.substring(mn.lastIndexOf(" ") + 1);
            bw.write(mn);
            bw.write("(");
            String rawArgs = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            String[] argss = rawArgs.split(",");
            for (int i = 0; i < argss.length; i++) {
                if (i != 0) bw.write(", ");
                String arg = argss[i];
                arg = arg.substring(arg.lastIndexOf(" ") + 1);
                bw.write(arg);
            }
            bw.write(")");
        }
        bw.write(";");
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.newLine();
    }
}
s.close();
bw.close();
 */
