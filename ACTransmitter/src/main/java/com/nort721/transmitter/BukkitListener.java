package com.nort721.transmitter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        String packetData = "1" + "|" +
                player.getUniqueId() + "|" +
                System.currentTimeMillis();

        ACTransmitter.getInstance().getComputationServerHandler().sendDataToServer(packetData);
    }
}
