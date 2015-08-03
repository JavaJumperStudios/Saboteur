package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet02Login extends Packet {

    public int playerId;
    public String name;

    @Override
    protected void readFromByteBuffer(ByteBuffer bb) {
	playerId = bb.getInt();
	
	for (int i = 0; i < 16; i++) {
	    bb.putChar(name.charAt(i));
	}
    }

    @Override
    protected ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());

	bb.put(id);
	bb.putInt(getLength());

	bb.putInt(playerId);
	
	for (int i = 0; i < 16; i++) {
	    if (i< name.length())
		bb.putChar(name.charAt(i));
	    else
		bb.putChar('_');
	}
	
	return bb;
    }

    @Override
    public int getLength() {
	return super.getLength() + Integer.BYTES + Character.BYTES * 16;
    }

}
