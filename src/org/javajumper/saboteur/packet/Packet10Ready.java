package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Sent between server and client to indicate that the ready-state of a player
 * has changed
 */
public class Packet10Ready extends Packet {

	/** The id of the player whose ready state has changed */
	public int playerId;
	/**
	 * if the player is ready or not<br>
	 * 0 = false,<br>
	 * 1 = true
	 */
	public byte ready;

	public Packet10Ready() {
		super((byte) 10);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		playerId = bb.getInt();
		ready = bb.get();
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id);
		bb.putInt(getLength());

		bb.putInt(playerId);
		bb.put(ready);

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES + 1;
	}

}
