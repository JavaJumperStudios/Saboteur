package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to clients to reset the game state
 */
public class Packet14Reset extends Packet {

	public Packet14Reset() {
		super((byte) 14);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(length);

		return bb;
	}

}
