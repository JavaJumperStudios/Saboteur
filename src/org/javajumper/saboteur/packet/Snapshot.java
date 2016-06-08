package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Snapshot {

	public PlayerSnapshot[] player;
	public int time;

	public void writeToByteBuffer(ByteBuffer bb) {
		bb.putInt(time);
		for (PlayerSnapshot ps : player) {
			ps.writeToByteBuffer(bb);
		}

	}

	public int getLength() {
		return player.length * PlayerSnapshot.getLength() + Integer.BYTES;
	}

	public int getSnapshotSize() {
		return player.length;
	}

	public void readFromByteBuffer(ByteBuffer bb, int snapshotsize) {
		player = new PlayerSnapshot[snapshotsize];
		time = bb.getInt();
		for (int i = 0; i < snapshotsize; i++) {
			player[i] = PlayerSnapshot.readFromByteBuffer(bb);
		}

	}

}
