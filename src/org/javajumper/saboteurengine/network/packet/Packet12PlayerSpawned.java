package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to clients to indicate that a player was spawned
 */
public class Packet12PlayerSpawned extends Packet {

	/** the name of the spawned player */
	public String name;
	/** the id of the spawned player */
	public int playerId;
	/** the x position of the spawned player */
	public float x;
	/** the y position of the spawned player */
	public float y;
	/** the role of the spawned player */
	public int role;
	/** the ready-state of the spawned player */
	public byte ready;

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
		ready = bb.get();
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
		bb.put(ready);

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES * 2 + Float.BYTES * 2 + Character.BYTES * 16 + 1;
	}

}
