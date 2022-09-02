package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.packet.impl.status.*;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

public class StatusPacketRegistryBase extends PacketRegistry {

    public StatusPacketRegistryBase(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(0x00, C2SStatusRequestPacket::new);
        this.registerC2SPacket(0x01, C2SPingRequestPacket::new);

        this.registerS2CPacket(0x00, S2CStatusResponsePacket::new);
        this.registerS2CPacket(0x01, S2CPingResponsePacket::new);
    }

}
