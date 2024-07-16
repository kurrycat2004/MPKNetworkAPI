package io.github.kurrycat2004.mpknetapi.bukkit;

import io.github.kurrycat2004.mpknetapi.bukkit.command.MPKCommand;
import io.github.kurrycat2004.mpknetapi.bukkit.listener.MPKPlayerListener;
import io.github.kurrycat2004.mpknetapi.bukkit.network.event.MPKPacketReceivedEvent;
import io.github.kurrycat2004.mpknetapi.bukkit.network.impl.MPKPacketListenerServerImpl;
import io.github.kurrycat2004.mpknetapi.common.MPKNetworking;
import io.github.kurrycat2004.mpknetapi.common.network.packet.MPKPacket;
import io.github.kurrycat2004.mpknetapi.common.network.packet.impl.MPKPacketListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

public final class MPKApiPlugin extends JavaPlugin {
    private static MPKApiPlugin instance;

    private final MPKPacketListener packetListener = new MPKPacketListenerServerImpl();
    private Messenger messenger;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        MPKCommand command = new MPKCommand();
        this.getCommand("mpk").setExecutor(command);
        this.getCommand("mpk").setTabCompleter(command);

        this.messenger = this.getServer().getMessenger();
        this.messenger.registerOutgoingPluginChannel(this, MPKNetworking.MESSENGER_CHANNEL);
        this.messenger.registerIncomingPluginChannel(this, MPKNetworking.MESSENGER_CHANNEL, ((channel, player, data) -> {
            MPKPacket packet = MPKPacket.handle(this.packetListener, data, player.getUniqueId());
            if (packet == null) {
                return;
            }

            MPKPacketReceivedEvent event = new MPKPacketReceivedEvent(player, packet);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                packet.process(this.packetListener);
            }
        }));

        this.getServer().getPluginManager().registerEvents(new MPKPlayerListener(), this);
    }

    @Override
    public void onDisable() {
        this.messenger.unregisterOutgoingPluginChannel(this);
        this.messenger.unregisterIncomingPluginChannel(this);
    }

    public static MPKApiPlugin getInstance() {
        return instance;
    }
}