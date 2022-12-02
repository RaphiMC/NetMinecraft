package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginHelloPacket1_19_3;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginKeyPacket1_19_3;

public class LoginPacketRegistryBase1_19_3 extends LoginPacketRegistryBase1_19_1 {

    public LoginPacketRegistryBase1_19_3(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_HELLO.getId(MCVersion.v1_19_3), C2SLoginHelloPacket1_19_3::new);
        this.registerC2SPacket(MCPackets.C2S_LOGIN_ENCRYPTION_RESPONSE.getId(MCVersion.v1_19_3), C2SLoginKeyPacket1_19_3::new);
    }

}
