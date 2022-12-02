package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginCustomPayloadPacket;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginCustomPayloadPacket;

public class LoginPacketRegistryBase1_13 extends LoginPacketRegistryBase1_8 {

    public LoginPacketRegistryBase1_13(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_QUERY_RESPONSE.getId(MCVersion.v1_13), C2SLoginCustomPayloadPacket::new);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_QUERY_REQUEST.getId(MCVersion.v1_13), S2CLoginCustomPayloadPacket::new);
    }

}
