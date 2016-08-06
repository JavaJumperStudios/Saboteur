package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Sent between client and server to indicate that an item is used
 */
public class Packet06UseItem extends Packet {

	/** the id of the used item */
	public int itemId;

	public Packet06UseItem() {
		super((byte) 6);
	}

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
