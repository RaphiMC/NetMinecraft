package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginHelloPacket1_19_1;

public class LoginPacketRegistryBase1_19_1 extends LoginPacketRegistryBase1_19 {

    public LoginPacketRegistryBase1_19_1(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_HELLO.getId(MCVersion.v1_19_1), C2SLoginHelloPacket1_19_1::new);
    }

}
