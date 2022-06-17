package com.nort721.transmitter.listeners;

import com.nort721.transmitter.ACTransmitter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {

    public BukkitListener(ACTransmitter acTransmitter) {
        acTransmitter.getServer().getPluginManager().registerEvents(this, acTransmitter);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        String packetData = "1" + "|" +
                player.getUniqueId() + "|" +
                System.currentTimeMillis();

        ACTransmitter.getInstance().getComputationServerSender().sendDataToServer(packetData);
    }

}
