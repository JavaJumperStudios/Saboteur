package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class PlayerSnapshot {

	public int playerId;
	public float x;
	public float y;
	public float lookAngle;
	public int currentWeapon;
	public int lifepoints;

	public void writeToByteBuffer(ByteBuffer bb) {

		bb.putInt(playerId);
		bb.putFloat(x);
		bb.putFloat(y);
		bb.putFloat(lookAngle);

		bb.putInt(currentWeapon);
		bb.putInt(lifepoints);
	}

	public static int getLength() {
		return Float.BYTES * 3 + Integer.BYTES * 3;
	}

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

}
