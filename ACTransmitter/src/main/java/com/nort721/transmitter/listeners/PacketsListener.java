package com.nort721.transmitter.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.nort721.transmitter.ACTransmitter;
import com.nort721.transmitter.utils.wrappers.WrapperPlayClientFlying;
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

        long currentTime = System.currentTimeMillis();

        // ToDo send packets values dynamically without checking types

        StringBuilder packetData = new StringBuilder();

        if (event.getPacketType() == PacketType.Play.Client.FLYING ||
                event.getPacketType() == PacketType.Play.Client.POSITION ||
                event.getPacketType() == PacketType.Play.Client.POSITION_LOOK ||
                event.getPacketType() == PacketType.Play.Client.LOOK) {

            WrapperPlayClientFlying flying = new WrapperPlayClientFlying(event);

            packetData.append("2").append("|");
            packetData.append(player.getUniqueId()).append("|");
            packetData.append(currentTime).append('|');
            packetData.append(flying.hasPosition()).append('|');
            packetData.append(flying.hasLook()).append('|');
            packetData.append(flying.getOnGround()).append('|');
            packetData.append(flying.getX()).append('|');
            packetData.append(flying.getY()).append('|');
            packetData.append(flying.getZ()).append('|');
            packetData.append(flying.getYaw()).append('|');
            packetData.append(flying.getPitch());

        } else if (event.getPacketType() == PacketType.Play.Client.ABILITIES) {

            boolean isFlying = event.getPacket().getBooleans().read(1);
            boolean canFly = event.getPacket().getBooleans().read(2);

            packetData.append("3").append("|");
            packetData.append(player.getUniqueId()).append("|");
            packetData.append(currentTime).append('|');
            packetData.append(isFlying).append('|');
            packetData.append(canFly);
        }

        if (packetData.length() > 0) {
            acTransmitter.getComputationServerSender().sendDataToServer(packetData.toString());
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();

        if (player instanceof TemporaryPlayer) return;

        long currentTime = System.currentTimeMillis();

        // ToDo send packets values dynamically without checking types

        StringBuilder packetData = new StringBuilder();

        if (event.getPacketType() == PacketType.Play.Server.LOGIN) {

            packetData.append("0").append("|");
            packetData.append(player.getUniqueId()).append("|");
            packetData.append(currentTime);

        } else if (event.getPacketType() == PacketType.Play.Server.ABILITIES) {

            boolean isFlying = event.getPacket().getBooleans().read(1);
            boolean canFly = event.getPacket().getBooleans().read(2);

            packetData.append("4").append("|");
            packetData.append(player.getUniqueId()).append("|");
            packetData.append(currentTime).append('|');
            packetData.append(isFlying).append('|');
            packetData.append(canFly);
        }

        if (packetData.length() > 0) {
            acTransmitter.getComputationServerSender().sendDataToServer(packetData.toString());
        }
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
