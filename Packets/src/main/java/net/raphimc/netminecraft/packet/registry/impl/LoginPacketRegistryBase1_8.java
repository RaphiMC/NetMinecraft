package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.login.*;

public class LoginPacketRegistryBase1_8 extends LoginPacketRegistryBase1_7_6 {

    public LoginPacketRegistryBase1_8(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(0x01, C2SLoginKeyPacket1_8::new);

        this.registerS2CPacket(0x01, S2CLoginKeyPacket1_8::new);
        this.registerS2CPacket(0x03, S2CLoginCompressionPacket::new);
    }

}
