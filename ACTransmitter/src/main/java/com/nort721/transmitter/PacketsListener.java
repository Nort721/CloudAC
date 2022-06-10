package com.nort721.transmitter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PacketsListener extends PacketAdapter {

    private final ACTransmitter acTransmitter;

    public PacketsListener(ACTransmitter acTransmitter) {
        super(ACTransmitter.getInstance(), ListenerPriority.LOWEST, getSupportedPacketTypes());
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
        this.acTransmitter = acTransmitter;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        if (player instanceof TemporaryPlayer) return;

        /* doing this on the netty thread to get the most accurate time */
        long currentTime = System.currentTimeMillis();

        StringBuilder packetData = new StringBuilder();

        if (event.getPacketType() == PacketType.Play.Client.ABILITIES) {

            // ToDo find a dynamic way to send packets values without checking types

            boolean isFlying = event.getPacket().getBooleans().read(1);
            boolean canFly = event.getPacket().getBooleans().read(2);

            packetData.append("1").append("|");
            packetData.append(player.getUniqueId()).append("|");
            packetData.append("timeStamp=").append(currentTime).append('|');
            packetData.append("isflying=").append(isFlying).append('|');
            packetData.append("canfly=").append(canFly).append('|');
        }

        if (packetData.length() > 0) {
            acTransmitter.getComputationServerHandler().sendDataToServer(packetData.toString());
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();

        if (player instanceof TemporaryPlayer) return;

        /* doing this on the netty thread to get the most accurate time */
        long currentTime = System.currentTimeMillis();
    }

    /**
     * @return - All supported Packet Types for the server's corresponding version
     * (Earlier and later versions may contain packets that have not been implemented to the server's version)
     * <p>
     * I also hate how it has to be static. Thanks ProtocolLib!
     */
    private static Iterable<PacketType> getSupportedPacketTypes() {
        final Iterator<PacketType> packetTypeIterator = PacketType.values().iterator();

        List<PacketType> packets = StreamSupport.stream(Spliterators.spliteratorUnknownSize(packetTypeIterator, Spliterator.ORDERED), false)
                .filter(PacketType::isSupported)
                .collect(Collectors.toList());

        // fix for red server ping in server menu
        packets.remove(PacketType.Status.Server.PONG);

        // No need to run this in parallel. Will only be called once upon server start.
        return packets;
    }
}
