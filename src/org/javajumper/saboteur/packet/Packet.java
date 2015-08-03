package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public abstract class Packet {

    public byte id;
    public int length;

    public Packet() {
	length = getLength();
    }
    
    protected int getLength() {
	return length;
    }
    
    public void readFromByteArray(byte[] data) {
	ByteBuffer bb = ByteBuffer.wrap(data);
	
	id = bb.get();
	length = bb.getInt();
	
	readFromByteBuffer(bb);
    }
    
    public byte[] writeToByteArray() {
	ByteBuffer bb = writeToByteBuffer();
	
	return bb.array();
    }

    protected abstract void readFromByteBuffer(ByteBuffer bb);
    protected abstract ByteBuffer writeToByteBuffer();

}
