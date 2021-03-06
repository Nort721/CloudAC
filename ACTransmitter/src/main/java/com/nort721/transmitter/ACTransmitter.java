package com.nort721.transmitter;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.nort721.transmitter.cserver.ComputationServerListener;
import com.nort721.transmitter.cserver.ComputationServerSender;
import com.nort721.transmitter.listeners.BukkitListener;
import com.nort721.transmitter.listeners.PacketsListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ACTransmitter extends JavaPlugin {

    private final List<PacketAdapter> packetAdapters = new ArrayList<>();

    @Getter private ComputationServerSender computationServerSender;

    /**
     * Gets the instance of the plugin that's currently active in memory
     * @return An ACTransmitter instance
     */
    public static ACTransmitter getInstance() {
        return getPlugin(ACTransmitter.class);
    }

    @Override
    public void onEnable() {
        new BukkitListener(this);
        packetAdapters.add(new PacketsListener(this));

        ComputationServerListener computationServerListener = new ComputationServerListener();
        computationServerListener.start();

        computationServerSender = new ComputationServerSender("localhost", 1234);
        computationServerSender.start();

        sendConsoleMessage("has been enabled");
    }

    @Override
    public void onDisable() {
        packetAdapters.forEach(ProtocolLibrary.getProtocolManager()::removePacketListener);
        computationServerSender.closeConnection();
        sendConsoleMessage("has been disabled");
    }

    public void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + getDescription().getName() + "] " + ChatColor.RESET + msg);
    }

}
