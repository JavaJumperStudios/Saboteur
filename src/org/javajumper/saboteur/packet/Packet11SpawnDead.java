package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet11SpawnDead extends Packet {

    public int playerId;
    public int timeOfDeath;
    public int killerId;
    public int itemId;

    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	// TODO Auto-generated method stub

    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	// TODO Auto-generated method stub
	return null;
    }

}
