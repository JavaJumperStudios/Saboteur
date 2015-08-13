package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet13Time extends Packet {
    
    public int time;

    public Packet13Time() {
	super((byte) 13);
    }

    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	time = bb.getInt();

    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());
	
	bb.put(id);
	bb.putInt(getLength());
	bb.putInt(time);
	
	return bb;
    }
    
    @Override
    public int getLength() {
	return super.getLength() + Integer.BYTES;
    }

}
