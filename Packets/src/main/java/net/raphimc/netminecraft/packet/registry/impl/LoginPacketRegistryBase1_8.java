package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.login.S2CLoginCompressionPacket;

public class LoginPacketRegistryBase1_8 extends LoginPacketRegistryBase1_7_6 {

    public LoginPacketRegistryBase1_8(boolean clientside) {
        super(clientside);

        this.registerS2CPacket(0x03, S2CLoginCompressionPacket::new);
    }

}
