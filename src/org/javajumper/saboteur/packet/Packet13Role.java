package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet13Role extends Packet {

	public int playerId;
	public int role;

	public Packet13Role() {
		super((byte) 13);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		playerId = bb.getInt();
		role = bb.getInt();

	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(length);

		bb.putInt(playerId);
		bb.putInt(role);

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES * 2;
	}

}
