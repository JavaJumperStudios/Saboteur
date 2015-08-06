package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet03StartGame extends Packet {

    Packet03StartGame() {
	super((byte) 3);
    }

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
