package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.login.C2SLoginHelloPacket1_19_1;

public class LoginPacketRegistryBase1_19_1 extends LoginPacketRegistryBase1_19 {

    public LoginPacketRegistryBase1_19_1(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(0x00, C2SLoginHelloPacket1_19_1::new);
    }

}
