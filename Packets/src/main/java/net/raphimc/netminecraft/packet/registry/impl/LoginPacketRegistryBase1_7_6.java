package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginSuccessPacket1_7_6;

public class LoginPacketRegistryBase1_7_6 extends LoginPacketRegistryBase1_7 {

    public LoginPacketRegistryBase1_7_6(boolean clientside) {
        super(clientside);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_SUCCESS.getId(MCVersion.v1_7_2), S2CLoginSuccessPacket1_7_6::new);
    }

}
