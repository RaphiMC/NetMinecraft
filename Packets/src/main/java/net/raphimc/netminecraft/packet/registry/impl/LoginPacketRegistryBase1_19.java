package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginHelloPacket1_19;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginKeyPacket1_19;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginSuccessPacket1_19;

public class LoginPacketRegistryBase1_19 extends LoginPacketRegistryBase1_16 {

    public LoginPacketRegistryBase1_19(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_HELLO.getId(MCVersion.v1_19), C2SLoginHelloPacket1_19::new);
        this.registerC2SPacket(MCPackets.C2S_LOGIN_ENCRYPTION_RESPONSE.getId(MCVersion.v1_19), C2SLoginKeyPacket1_19::new);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_SUCCESS.getId(MCVersion.v1_19), S2CLoginSuccessPacket1_19::new);
    }

}
