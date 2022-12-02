package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginKeyPacket1_8;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginCompressionPacket;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginKeyPacket1_8;

public class LoginPacketRegistryBase1_8 extends LoginPacketRegistryBase1_7_6 {

    public LoginPacketRegistryBase1_8(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_ENCRYPTION_RESPONSE.getId(MCVersion.v1_8), C2SLoginKeyPacket1_8::new);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_ENCRYPTION_REQUEST.getId(MCVersion.v1_8), S2CLoginKeyPacket1_8::new);
        this.registerS2CPacket(MCPackets.S2C_LOGIN_COMPRESSION.getId(MCVersion.v1_8), S2CLoginCompressionPacket::new);
    }

}
