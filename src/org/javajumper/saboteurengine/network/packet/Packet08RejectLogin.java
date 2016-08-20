package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to client if the connection got rejected
 */
public class Packet08RejectLogin extends Packet {

	/** the cause why the connection got rejected */
	public byte rejectionCause; // Unused, 0 by default

	public Packet08RejectLogin() {
		super((byte) 8);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {
		rejectionCause = bb.get();
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id);
		bb.putInt(getLength());
		
		bb.put(rejectionCause);
		
		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + 1;
	}

}
