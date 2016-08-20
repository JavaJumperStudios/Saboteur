package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

/**
 * Sent from client to server to update the server about the movement intentions
 * of the player
 */
public class Packet09PlayerUpdate extends Packet {

	/** the direction the player is looking in degrees */
	public float lookAngle;
	/** the x component of the movement vector of the player */
	public float moveX;
	/** the y component of the movement vector of the player */
	public float moveY;
	/** the slot id of the currently selected item */
	public int currentItem;
	/** true if the player is sprinting */
	public byte sprinting;

	public Packet09PlayerUpdate() {
		super((byte) 9);
	}

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
