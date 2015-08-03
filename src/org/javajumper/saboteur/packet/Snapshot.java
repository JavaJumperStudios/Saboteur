package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Snapshot {

    public PlayerSnapshot[] player;

    public void writeToByteBuffer(ByteBuffer bb) {
	for (PlayerSnapshot ps : player) {
	    ps.writeToByteBuffer(bb);
	}
    }
    
    public int getLength() {
	return player.length * PlayerSnapshot.getLength();
    }

    public int getSnapshotSize() {
	return player.length;
    }

    public void readFromByteBuffer(ByteBuffer bb, int snapshotsize) {
	player = new PlayerSnapshot[snapshotsize];
	
	for (int i = 0; i < snapshotsize; i++) {
	    player[i] = PlayerSnapshot.readFromByteBuffer(bb);
	}
    }

}
