package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

/**
 * A player snapshot containing update information about one player
 */
public class PlayerSnapshot {

	/** the id of the player */
	public int playerId;
	/** x position of the player */
	public float x;
	/** y position of the player */
	public float y;
	/** angle the player is looking */
	public float lookAngle;
	/** selected weapon of the player */
	public int currentWeapon;
	/** current lifepoints of the player */
	public int lifepoints;

	/**
	 * Reads information from a ByteBuffer into a newly created PlayerSnapshot
	 * 
	 * @param bb
	 *            the ByteBuffer to read from
	 * @return the created PlayerSnapshot
	 */
	public static PlayerSnapshot readFromByteBuffer(ByteBuffer bb) {
		PlayerSnapshot p = new PlayerSnapshot();

		p.playerId = bb.getInt();
		p.x = bb.getFloat();
		p.y = bb.getFloat();
		p.lookAngle = bb.getFloat();

		p.currentWeapon = bb.getInt();
		p.lifepoints = bb.getInt();

		return p;
	}

	/**
	 * @return the length in bytes of this snapshot
	 */
	public static int getLength() {
		return Float.BYTES * 3 + Integer.BYTES * 3;
	}

	/**
	 * Writes this PlayerSnapshot to a ByteBuffer
	 * 
	 * @param bb
	 *            the ByteBuffer to write to
	 */
	public void writeToByteBuffer(ByteBuffer bb) {

		bb.putInt(playerId);
		bb.putFloat(x);
		bb.putFloat(y);
		bb.putFloat(lookAngle);

		bb.putInt(currentWeapon);
		bb.putInt(lifepoints);
	}

}
