package io.github.kurrycat2004.mpknetapi.common.network.packet.impl.serverbound;

import io.github.kurrycat2004.mpknetapi.common.network.packet.impl.MPKPacketListener;

/**
 * Packet listener interface for the server.
 */
public interface MPKPacketListenerServer extends MPKPacketListener {
    void handleRegister(MPKPacketRegister packet);
    void handleModuleUpdate(MPKPacketModuleUpdate packet);
}
