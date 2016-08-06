package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to clients to indicate that a player died
 */
public class Packet08Death extends Packet {

	/** the player who has died */
	public int playerId;

	public Packet08Death() {
		super((byte) 8);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		// TODO Auto-generated method stub
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLength() {
		return -1;
		// TODO implement
	}

}
