package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

/**
 * A snapshot containing information (like movement) about all players on the
 * server which is sent from the server to the client to keep him up-to-date
 * with the game state
 */
public class Packet07Snapshot extends Packet {

	/** the snapshot which is sent containing information about all players */
	public Snapshot snapshot;

	public Packet07Snapshot() {
		super((byte) 7);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		snapshot = new Snapshot(bb);
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(getLength());
		bb.putInt(snapshot.getSnapshotPlayerCount());
		snapshot.writeToByteBuffer(bb);

		return bb;
	}

	@Override
	public int getLength() {
		if (snapshot == null)
			return super.getLength() + Integer.BYTES;

		return super.getLength() + snapshot.getLength() + Integer.BYTES;
	}

}
