package com.nort721.transmitter;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ACTransmitter extends JavaPlugin {

    private final List<PacketAdapter> packetAdapters = new ArrayList<>();

    @Getter private ComputationServerHandler computationServerHandler;

    public static ACTransmitter getInstance() {
        return getPlugin(ACTransmitter.class);
    }

    @Override
    public void onEnable() {
        packetAdapters.add(new PacketsListener(this));
        computationServerHandler = new ComputationServerHandler("localhost", 1234);
        computationServerHandler.start();

        sendConsoleMessage("has been enabled");
    }

    @Override
    public void onDisable() {
        packetAdapters.forEach(ProtocolLibrary.getProtocolManager()::removePacketListener);
        computationServerHandler.closeConnection();
        sendConsoleMessage("has been disabled");
    }

    public void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ACTransmitter] " + ChatColor.RESET + msg);
    }
}
