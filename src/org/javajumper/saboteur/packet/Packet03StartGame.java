package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to client to indicate the start of the game
 */
public class Packet03StartGame extends Packet {

	public Packet03StartGame() {
		super((byte) 3);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id);
		bb.putInt(getLength());

		return bb;
	}

}
