package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

import org.javajumper.saboteur.SaboteurServer;

/**
 * Sent from server to client to indicate the end of the game
 */
public class Packet04EndGame extends Packet {

	/**
	 * The reason why the game has ended
	 * <ul>
	 * <li>{@link SaboteurServer#TIME_RAN_OUT} = Zeit ausgelaufen --> Innocent
	 * gewinnen</li>
	 * <li>{@link SaboteurServer#NO_TRAITORS_LEFT} = Alle Traitor tot -->
	 * Innocent gewinnen</li>
	 * <li>{@link SaboteurServer#NO_INNOCENTS_LEFT} = Alle Innocents tot -->
	 * Traitor gewinnen</li>
	 * <li>{@link SaboteurServer#NO_PLAYERS_LEFT} = Alle tot --> Reset</li>
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
