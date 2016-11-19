package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to client if the connection got rejected
 */
public class Packet08RejectLogin extends Packet {

	/** the cause why the connection got rejected */
	public byte rejectionCause; // Unused, 0 by default

	/** the msg from the server to the client */
	public String msg = "";

	public Packet08RejectLogin() {
		super((byte) 8);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		rejectionCause = bb.get();

		msg = "";
		for (int i = 0; i < 16; i++) {
			msg = msg + bb.getChar();
		}
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id);
		bb.putInt(getLength());

		bb.put(rejectionCause);

		for (int i = 0; i < 16; i++) {
			if (msg.length() > i) {
				bb.putChar(msg.charAt(i));
			} else {
				bb.putChar(' ');
			}
		}

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + 1;
	}

}
