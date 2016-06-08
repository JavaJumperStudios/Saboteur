package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet07Snapshot extends Packet {

	public int snapshotsize;
	public Snapshot snapshot;

	public Packet07Snapshot() {
		super((byte) 7);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {

		snapshotsize = bb.getInt();

		snapshot = new Snapshot();
		snapshot.readFromByteBuffer(bb, snapshotsize);

	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(getLength());
		bb.putInt(snapshot.getSnapshotSize());
		snapshot.writeToByteBuffer(bb);

		return bb;
	}

	@Override
	public int getLength() {
		if (snapshot == null)
			return super.getLength() + Integer.BYTES;

		return super.getLength() + snapshot.getLength() + Integer.BYTES;
	}

}
