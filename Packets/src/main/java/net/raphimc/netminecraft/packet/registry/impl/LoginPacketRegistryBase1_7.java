package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.login.*;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

public class LoginPacketRegistryBase1_7 extends PacketRegistry {

    public LoginPacketRegistryBase1_7(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(0x00, C2SLoginHelloPacket1_7::new);
        this.registerC2SPacket(0x01, C2SLoginKeyPacket1_7::new);

        this.registerS2CPacket(0x00, S2CLoginDisconnectPacket::new);
        this.registerS2CPacket(0x01, S2CLoginKeyPacket1_7::new);
        this.registerS2CPacket(0x02, S2CLoginSuccessPacket1_7::new);
    }

}
