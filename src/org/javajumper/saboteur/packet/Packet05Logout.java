package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet05Logout extends Packet {

	public int playerId;

	public Packet05Logout() {
		super((byte) 5);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		playerId = bb.getInt();

	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(getLength());

		bb.putInt(playerId);

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES;
	}

}
