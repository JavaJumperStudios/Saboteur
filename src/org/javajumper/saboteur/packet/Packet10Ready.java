package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet10Ready extends Packet {

    public int playerId;
    public boolean ready;

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
