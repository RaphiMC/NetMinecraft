package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.impl.login.*;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

public class LoginPacketRegistryBase1_7 extends PacketRegistry {

    public LoginPacketRegistryBase1_7(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_LOGIN_HELLO.getId(MCVersion.v1_7_2), C2SLoginHelloPacket1_7::new);
        this.registerC2SPacket(MCPackets.C2S_LOGIN_ENCRYPTION_RESPONSE.getId(MCVersion.v1_7_2), C2SLoginKeyPacket1_7::new);

        this.registerS2CPacket(MCPackets.S2C_LOGIN_DISCONNECT.getId(MCVersion.v1_7_2), S2CLoginDisconnectPacket::new);
        this.registerS2CPacket(MCPackets.S2C_LOGIN_ENCRYPTION_REQUEST.getId(MCVersion.v1_7_2), S2CLoginKeyPacket1_7::new);
        this.registerS2CPacket(MCPackets.S2C_LOGIN_SUCCESS.getId(MCVersion.v1_7_2), S2CLoginSuccessPacket1_7::new);
    }

}
