package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to client to indicate the end of the game
 */
public class Packet04EndGame extends Packet {

	/**
	 * The reason why the game has ended
	 * <ul>
	 * <li>0 = Zeit ausgelaufen --> Innocent gewinnen</li>
	 * <li>1 = Alle Traitor tot --> Innocent gewinnen</li>
	 * <li>2 = Alle Innocents tot --> Traitor gewinnen</li>
	 * <li>3 = Alle tot --> Reset</li>
	 * </ul>
	 */
	public byte endCause;

	public Packet04EndGame() {
		super((byte) 4);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		endCause = bb.get();

	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id);
		bb.putInt(getLength());

		bb.put(endCause);

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + 1;
	}

}
