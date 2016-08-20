package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to client to spawn a new dead player
 */
public class Packet11SpawnDead extends Packet {

	/** the id of the dead player */
	public int playerId;
	/** when the player died */
	public int timeOfDeath;
	/** the id of the one who killed the dead player */
	public int killerId;
	/** the item with which the player was killed */
	public int itemId;
	/** the x position of the dead player */
	public float posX;
	/** the y position of the dead player */
	public float posY;
	/** the role of the player who died */
	public int role;
	/** the name of the player who died */
	public String name;

	public Packet11SpawnDead() {
		super((byte) 11);
	}

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
