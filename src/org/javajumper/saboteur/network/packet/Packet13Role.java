package org.javajumper.saboteur.network.packet;

import java.nio.ByteBuffer;

import org.javajumper.saboteurengine.network.packet.Packet;

/**
 * Sent from server to clients to indicate the role change of a player
 */
public class Packet13Role extends Packet {

	/** the id of the player whose role was changed */
	public int playerId;
	/** the new role of the player */
	public int role;

	public Packet13Role() {
		super((byte) 13);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		playerId = bb.getInt();
		role = bb.getInt();

	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(length);

		bb.putInt(playerId);
		bb.putInt(role);

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES * 2;
	}

}
