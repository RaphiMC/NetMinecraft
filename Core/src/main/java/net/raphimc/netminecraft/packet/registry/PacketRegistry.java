package net.raphimc.netminecraft.packet.registry;

import net.raphimc.netminecraft.packet.IPacket;
import net.raphimc.netminecraft.packet.UnknownPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PacketRegistry {

    private final boolean clientside;

    public PacketRegistry(final boolean clientside) {
        this.clientside = clientside;
    }

    private final Map<Integer, Supplier<IPacket>> C2S_ID_PACKET_REGISTRY = new HashMap<>();
    private final Map<Integer, Supplier<IPacket>> S2C_ID_PACKET_REGISTRY = new HashMap<>();

    private final Map<Class<? extends IPacket>, Integer> C2S_PACKET_ID_REGISTRY = new HashMap<>();
    private final Map<Class<? extends IPacket>, Integer> S2C_PACKET_ID_REGISTRY = new HashMap<>();

    protected final void registerC2SPacket(final int packetId, final Supplier<IPacket> packetSupplier) {
        this.unregisterC2SPacket(packetId);
        C2S_PACKET_ID_REGISTRY.put(packetSupplier.get().getClass(), packetId);
        C2S_ID_PACKET_REGISTRY.put(packetId, packetSupplier);
    }

    protected final void unregisterC2SPacket(final int packetId) {
        C2S_PACKET_ID_REGISTRY.entrySet().removeIf(e -> e.getValue() == packetId);
        C2S_ID_PACKET_REGISTRY.remove(packetId);
    }

    protected final void registerS2CPacket(final int packetId, final Supplier<IPacket> packetSupplier) {
        this.unregisterS2CPacket(packetId);
        S2C_PACKET_ID_REGISTRY.put(packetSupplier.get().getClass(), packetId);
        S2C_ID_PACKET_REGISTRY.put(packetId, packetSupplier);
    }

    protected final void unregisterS2CPacket(final int packetId) {
        S2C_PACKET_ID_REGISTRY.entrySet().removeIf(e -> e.getValue() == packetId);
        S2C_ID_PACKET_REGISTRY.remove(packetId);
    }


    public final IPacket getPacketById(final int packetId) {
        if (this.clientside) return S2C_ID_PACKET_REGISTRY.getOrDefault(packetId, UnknownPacket::new).get();
        else return C2S_ID_PACKET_REGISTRY.getOrDefault(packetId, UnknownPacket::new).get();
    }

    @SuppressWarnings("unchecked")
    public final Class<? extends IPacket> getTargetClassByPacket(final Class<? extends IPacket> packetClass) {
        Class<?> clazz = packetClass;
        do {
            if (C2S_PACKET_ID_REGISTRY.containsKey(clazz) || S2C_PACKET_ID_REGISTRY.containsKey(clazz)) return (Class<? extends IPacket>) clazz;
            clazz = clazz.getSuperclass();
        } while (!IPacket.class.equals(clazz) && clazz != null);
        return packetClass;
    }

    public final int getIdByPacket(final Class<? extends IPacket> packetClass) {
        if (this.clientside) return C2S_PACKET_ID_REGISTRY.getOrDefault(packetClass, -1);
        else return S2C_PACKET_ID_REGISTRY.getOrDefault(packetClass, -1);
    }

}
