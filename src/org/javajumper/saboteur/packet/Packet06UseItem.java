package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet06UseItem extends Packet {

    Packet06UseItem() {
	super((byte) 6);
    }

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
