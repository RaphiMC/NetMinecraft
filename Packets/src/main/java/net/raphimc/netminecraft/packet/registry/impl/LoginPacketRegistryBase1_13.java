package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.login.C2SLoginCustomPayloadPacket;
import net.raphimc.netminecraft.packet.impl.login.S2CLoginCustomPayloadPacket;

public class LoginPacketRegistryBase1_13 extends LoginPacketRegistryBase1_8 {

    public LoginPacketRegistryBase1_13(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(0x02, C2SLoginCustomPayloadPacket::new);

        this.registerS2CPacket(0x04, S2CLoginCustomPayloadPacket::new);
    }

}
