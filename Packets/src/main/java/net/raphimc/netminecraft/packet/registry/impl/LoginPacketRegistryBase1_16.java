package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginSuccessPacket1_16;

public class LoginPacketRegistryBase1_16 extends LoginPacketRegistryBase1_13 {

    public LoginPacketRegistryBase1_16(boolean clientside) {
        super(clientside);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_SUCCESS.getId(MCVersion.v1_16), S2CLoginSuccessPacket1_16::new);
    }

}
