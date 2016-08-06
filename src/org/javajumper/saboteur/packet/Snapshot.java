package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Used to send updates about all players to the clients
 */
public class Snapshot {

	/** The update info about all players */
	public PlayerSnapshot[] player;
	/** The time when the snapshot of the game was taken */
	public int time;

	/**
	 * Create a new empty Snapshot
	 */
	public Snapshot() {}

	/**
	 * Creates a new Snapshot and reads its contents from a given ByteBuffer
	 * 
	 * @param bb
	 *            the ByteBuffer to read from
	 */
	public Snapshot(ByteBuffer bb) {
		// The first 4 bytes represent the number of players whose information
		// is saved in the snapshot
		this.readFromByteBuffer(bb, bb.getInt());
	}

	/**
	 * Writes this snapshot to a ByteBuffer
	 * 
	 * @param bb
	 *            the ByteBuffer to write to
	 */
	public void writeToByteBuffer(ByteBuffer bb) {
		bb.putInt(time);
		for (PlayerSnapshot ps : player) {
			ps.writeToByteBuffer(bb);
		}

	}

	/**
	 * Reads information from a ByteBuffer into this snapshot
	 * 
	 * @param bb
	 *            the ByteBuffer to read from
	 * @param snapshotsize
	 *            the size ofs
	 */
	public void readFromByteBuffer(ByteBuffer bb, int snapshotsize) {
		player = new PlayerSnapshot[snapshotsize];
		time = bb.getInt();
		for (int i = 0; i < snapshotsize; i++) {
			player[i] = PlayerSnapshot.readFromByteBuffer(bb);
		}

	}

	/**
	 * @return the length of this snapshot in bytes
	 */
	public int getLength() {
		return player.length * PlayerSnapshot.getLength() + Integer.BYTES;
	}

	/**
	 * @return the number of players whose information is saved
	 */
	public int getSnapshotPlayerCount() {
		return player.length;
	}

}
