package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Represent an login attempt by a user/player
 */
public class Packet01LoginRequest extends Packet {

	/** The name of the player */
	public String name;
	/** The password used */
	public String password;

	public Packet01LoginRequest() {
		super((byte) 1);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		String rname = "";
		String rpassword = "";

		for (int i = 0; i < 16; i++) {
			rname = rname + bb.getChar();
		}

		for (int i = 0; i < 16; i++) {
			rpassword = rpassword + bb.getChar();
		}

		this.name = rname;
		this.password = rpassword;
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(getLength());

		for (int i = 0; i < 16; i++) {
			if (i < name.length())
				bb.putChar(name.charAt(i));
			else
				bb.putChar('_');
		}

		for (int i = 0; i < 16; i++) {
			if (i < password.length())
				bb.putChar(password.charAt(i));
			else
				bb.putChar('_');
		}

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Character.BYTES * 32;
	}

}
