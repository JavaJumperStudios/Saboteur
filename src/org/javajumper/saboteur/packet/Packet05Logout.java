package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet05Logout extends Packet {

    Packet05Logout() {
	super((byte) 5);
    }

    public int playerId;

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
