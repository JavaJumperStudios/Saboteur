package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet11SpawnDead extends Packet {

	public Packet11SpawnDead() {
		super((byte) 11);
	}

	public int playerId;
	public int timeOfDeath;
	public int killerId;
	public int itemId;
	public float posX;
	public float posY;
	public int role;
	public String name;

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		playerId = bb.getInt();
		timeOfDeath = bb.getInt();
		killerId = bb.getInt();
		itemId = bb.getInt();
		posX = bb.getFloat();
		posY = bb.getFloat();
		role = bb.getInt();

		name = "";

		for (int i = 0; i < 16; i++) {
			name += bb.getChar();
		}

	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id);
		bb.putInt(getLength());

		bb.putInt(playerId);
		bb.putInt(timeOfDeath);
		bb.putInt(killerId);
		bb.putInt(itemId);
		bb.putFloat(posX);
		bb.putFloat(posY);
		bb.putInt(role);

		for (int i = 0; i < 16; i++) {
			if (name.length() > i)
				bb.putChar(name.charAt(i));
			else
				bb.putChar('_');
		}

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES * 5 + Float.BYTES * 2 + Character.BYTES * 16;
	}

}
