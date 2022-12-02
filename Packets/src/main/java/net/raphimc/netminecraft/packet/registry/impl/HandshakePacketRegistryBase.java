package net.raphimc.netminecraft.packet.registry.impl;

import net.raphimc.netminecraft.constants.MCPackets;
import net.raphimc.netminecraft.packet.impl.handshake.C2SHandshakePacket;
import net.raphimc.netminecraft.packet.registry.PacketRegistry;

public class HandshakePacketRegistryBase extends PacketRegistry {

    public HandshakePacketRegistryBase(boolean clientside) {
        super(clientside);

        this.registerC2SPacket(MCPackets.C2S_HANDSHAKE.getId(0), C2SHandshakePacket::new);
    }

}
