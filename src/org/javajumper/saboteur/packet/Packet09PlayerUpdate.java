package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet09PlayerUpdate extends Packet {

	public Packet09PlayerUpdate() {
		super((byte) 9);
	}

	public float lookAngle;
	public float moveX;
	public float moveY;
	public int currentItem;
	public byte sprinting;

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		lookAngle = bb.getFloat();
		moveX = bb.getFloat();
		moveY = bb.getFloat();
		currentItem = bb.getInt();
		sprinting = bb.get();
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id);
		bb.putInt(getLength());

		bb.putFloat(lookAngle);
		bb.putFloat(moveX);
		bb.putFloat(moveY);

		bb.putInt(currentItem);
		bb.put(sprinting);

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Float.BYTES * 3 + Integer.BYTES + 1;
	}

}
