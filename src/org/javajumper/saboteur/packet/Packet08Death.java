package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet08Death extends Packet {

	Packet08Death() {
		super((byte) 8);
	}

	public int playerId;

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
