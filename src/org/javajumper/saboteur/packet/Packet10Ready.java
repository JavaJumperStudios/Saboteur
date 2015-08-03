package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet10Ready extends Packet {

    public int playerId;
    public boolean ready;

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
