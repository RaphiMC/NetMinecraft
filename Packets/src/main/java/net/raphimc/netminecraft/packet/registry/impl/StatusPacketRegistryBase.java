package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.packet.impl.status.C2SPingRequestPacket;
import net.raphimc.netminecraft.packet.impl.status.C2SStatusRequestPacket;
import net.raphimc.netminecraft.packet.impl.status.S2CPingResponsePacket;
import net.raphimc.netminecraft.packet.impl.status.S2CStatusResponsePacket;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

public class StatusPacketRegistryBase extends PacketRegistry {

    public StatusPacketRegistryBase(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_STATUS_REQUEST.getId(0), C2SStatusRequestPacket::new);
        this.registerC2SPacket(MCPackets.C2S_STATUS_PING.getId(0), C2SPingRequestPacket::new);

        this.registerS2CPacket(MCPackets.S2C_STATUS_RESPONSE.getId(0), S2CStatusResponsePacket::new);
        this.registerS2CPacket(MCPackets.S2C_STATUS_PONG.getId(0), S2CPingResponsePacket::new);
    }

}
