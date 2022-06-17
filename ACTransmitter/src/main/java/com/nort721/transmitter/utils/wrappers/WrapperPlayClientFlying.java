package com.nort721.transmitter.utils.wrappers;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WrapperPlayClientFlying {
	private final PacketContainer packetData;

	public WrapperPlayClientFlying(PacketEvent packetEvent) {
		packetData = packetEvent.getPacket();
	}

	/**
	 * Retrieve if packet has position.
	 * <p>
	 * Notes: true if the packet has position.
	 * 
	 * @return If packet has position.
	 */
	public boolean hasPosition() {
		return packetData.getBooleans().read(1);
	}

	/**
	 * Retrieve if packet has look.
	 * <p>
	 * Notes: true if the packet has look.
	 *
	 * @return If packet has position.
	 */
	public boolean hasLook() {
		return packetData.getBooleans().read(2);
	}

	/**
	 * Retrieve On Ground.
	 * <p>
	 * Notes: true if the client is on the ground, False otherwise
	 *
	 * @return The current On Ground
	 */
	public boolean getOnGround() {
		return packetData.getBooleans().read(0);
	}

	/**
	 * Set On Ground.
	 * 
	 * @param value - new value.
	 */
	public void setOnGround(boolean value) {
		packetData.getBooleans().write(0, value);
	}

	public double getX() {
		return packetData.getDoubles().read(0);
	}

	public double getY() {
		return packetData.getDoubles().read(1);
	}

	public double getZ() {
		return packetData.getDoubles().read(2);
	}

	public float getPitch() {
		return packetData.getFloat().read(1);
	}

	public float getYaw() {
		return packetData.getFloat().read(0);
	}
}
