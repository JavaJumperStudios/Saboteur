package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet06UseItem extends Packet {

    public Packet06UseItem() {
	super((byte) 6);
    }

    public int itemId;

    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	itemId = bb.getInt();
	
    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());
	bb.put(id);
	bb.putInt(getLength());
	bb.putInt(itemId);	
	return bb;
    }
    
    @Override
    public int getLength() {
	return super.getLength() + Integer.BYTES;
    }

}
