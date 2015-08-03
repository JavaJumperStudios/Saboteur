package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet11SpawnDead extends Packet {

    public int playerId;
    public int timeOfDeath;
    public int killerId;
    public int itemId;

    @Override
    protected void readFromByteBuffer(ByteBuffer bb) {
	// TODO Auto-generated method stub

    }

    @Override
    protected ByteBuffer writeToByteBuffer() {
	// TODO Auto-generated method stub
	return null;
    }

}
