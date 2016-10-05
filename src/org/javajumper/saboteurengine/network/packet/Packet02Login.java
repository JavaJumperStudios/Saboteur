package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteurengine.player.SPPlayer;
import org.newdawn.slick.geom.Vector2f;

/**
 * This packet is sent from server to client to confirm its login and assign an
 * id and a name (based on the request) to the client
 */
public class Packet02Login extends Packet {

	/** The assigned id for the client */
	public int playerId;
	/** The assigned id for the client */
	public String name;

	public Packet02Login() {
		super((byte) 2);
	}

	/**
	 * Creates a new SPPlayer Object from an Login packet. By default, the
	 * player is assigned the login packet id, the login packet name, 100
	 * lifepoints, and spawned with role lobby at position (0, 0) with the
	 * texture "Fuzzi_Neutral.png"
	 * 
	 * @param loginPacket
	 *            the login packet to create the player from
	 * @return the created player
	 */
	public static SPPlayer createPlayerFromLoginPacket(Packet02Login loginPacket) {

		SPPlayer p = new SPPlayer(loginPacket.playerId, Role.LOBBY, loginPacket.name, 100, new Vector2f(0, 0),
				"Fuzzi_Neutral.png");

		return p;
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		playerId = bb.getInt();

		name = "";

		for (int i = 0; i < 16; i++) {
			name = name + bb.getChar();
		}
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(getLength());

		bb.putInt(playerId);

		for (int i = 0; i < 16; i++) {
			if (i < name.length())
				bb.putChar(name.charAt(i));
			else
				bb.putChar('_');
		}

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES + Character.BYTES * 16;
	}

}
