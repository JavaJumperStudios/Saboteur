package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet12PlayerSpawned extends Packet {

    public String name;
    public int playerId;
    public float x;
    public float y;
    public int role;

    public Packet12PlayerSpawned() {
	super((byte) 12);
    }

    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	name = "";
	
	for (int i = 0; i < 16; i++) {
	    name += bb.getChar();
	}
	
	playerId = bb.getInt();
	x = bb.getFloat();
	y = bb.getFloat();
	role = bb.getInt();
    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());

	bb.put(id);
	bb.putInt(length);

	for (int i = 0; i < 16; i++) {
	    if (name.length() > i)
		bb.putChar(name.charAt(i));
	    else
		bb.putChar('_');
	}

	bb.putInt(playerId);
	bb.putFloat(x);
	bb.putFloat(y);
	bb.putInt(role);

	return bb;
    }

    @Override
    public int getLength() {
	return super.getLength() + Integer.BYTES * 2 + Float.BYTES * 2 + Character.BYTES * 16;
    }

}
