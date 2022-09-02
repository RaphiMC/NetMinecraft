package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.login.S2CLoginSuccessPacket1_7_6;

public class LoginPacketRegistryBase1_7_6 extends LoginPacketRegistryBase1_7 {

    public LoginPacketRegistryBase1_7_6(boolean clientside) {
        super(clientside);

        this.registerS2CPacket(0x02, S2CLoginSuccessPacket1_7_6::new);
    }

}
