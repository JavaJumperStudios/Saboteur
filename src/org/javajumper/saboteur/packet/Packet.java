package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public abstract class Packet {

    public byte id;
    public int length;

    Packet(byte id) {
	this.id = id;
	this.length = getLength();
    }
    
    protected int getLength() {
	return Byte.BYTES + Integer.BYTES;
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

    public abstract void readFromByteBuffer(ByteBuffer bb);

    public abstract ByteBuffer writeToByteBuffer();

}
