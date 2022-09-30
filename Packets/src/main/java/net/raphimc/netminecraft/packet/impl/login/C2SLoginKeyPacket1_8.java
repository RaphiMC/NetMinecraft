package net.raphimc.netminecraft.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.raphimc.netminecraft.packet.PacketTypes;

public class C2SLoginKeyPacket1_8 extends C2SLoginKeyPacket1_7 {

    public C2SLoginKeyPacket1_8() {
    }

    public C2SLoginKeyPacket1_8(final byte[] encryptedSecretKey, final byte[] encryptedNonce) {
        super(encryptedSecretKey, encryptedNonce);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.encryptedSecretKey = PacketTypes.readByteArray(byteBuf);
        this.encryptedNonce = PacketTypes.readByteArray(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        PacketTypes.writeByteArray(byteBuf, this.encryptedSecretKey);
        PacketTypes.writeByteArray(byteBuf, this.encryptedNonce);
    }

}
