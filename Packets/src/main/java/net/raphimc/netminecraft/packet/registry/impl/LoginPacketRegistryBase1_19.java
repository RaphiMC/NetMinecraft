package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.login.*;

public class LoginPacketRegistryBase1_19 extends LoginPacketRegistryBase1_16 {

    public LoginPacketRegistryBase1_19(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(0x00, C2SLoginHelloPacket1_19::new);
        this.registerC2SPacket(0x01, C2SLoginKeyPacket1_19::new);

        this.registerS2CPacket(0x02, S2CLoginSuccessPacket1_19::new);
    }

}
