package net.raphimc.netminecraft.packet.registry;

import net.raphimc.netminecraft.constants.MCVersion;
import net.raphimc.netminecraft.packet.registry.impl.*;

public class PacketRegistryUtil {

    public static PacketRegistry getHandshakeRegistry(final boolean clientside) {
        return new HandshakePacketRegistryBase(clientside);
    }

    public static PacketRegistry getStatusRegistry(final boolean clientside) {
        return new StatusPacketRegistryBase(clientside);
    }

    public static PacketRegistry getLoginRegistry(final boolean clientside, final int protocolVersion) {
        if (protocolVersion >= MCVersion.v1_19_1) {
            return new LoginPacketRegistryBase1_19_1(clientside);
        } else if (protocolVersion >= MCVersion.v1_19) {
            return new LoginPacketRegistryBase1_19(clientside);
        } else if (protocolVersion >= MCVersion.v1_16) {
            return new LoginPacketRegistryBase1_16(clientside);
        } else if (protocolVersion >= MCVersion.v1_13) {
            return new LoginPacketRegistryBase1_13(clientside);
        } else if (protocolVersion >= MCVersion.v1_8) {
            return new LoginPacketRegistryBase1_8(clientside);
        } else if (protocolVersion >= MCVersion.v1_7_6) {
            return new LoginPacketRegistryBase1_7_6(clientside);
        }
        return new LoginPacketRegistryBase1_7(clientside);
    }

    public static PacketRegistry getPlayRegistry(final boolean clientside, final int protocolVersion) {
        return new PlayPacketRegistryBase(clientside);
    }

}
