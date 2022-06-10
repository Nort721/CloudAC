package com.nort721.transmitter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        String packetData = "0" + "|" +
                player.getUniqueId() + "|" +
                System.currentTimeMillis() + '|';

        ACTransmitter.getInstance().getComputationServerHandler().sendDataToServer(packetData);
        System.out.println("sent sockets - " + packetData);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        String packetData = "1" + "|" +
                player.getUniqueId() + "|" +
                System.currentTimeMillis() + '|';

        ACTransmitter.getInstance().getComputationServerHandler().sendDataToServer(packetData);
    }
}
