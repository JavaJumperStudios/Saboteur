package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet10Ready extends Packet {

    public int playerId;
    /**
     * 0 = false, 1 = true
     * {@link <a href="http://www.stackoverflow.com/questions/18825324/why-bytebuffer-doesnt-provide-method-to-read-write-boolean-data-type">Link to Thread</a>}
     */
    public byte ready;

    public Packet10Ready() {
	super((byte) 10);
    }
    
    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	playerId = bb.getInt();
	ready = bb.get();
    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());
	bb.put(id);
	bb.putInt(getLength());

	bb.putInt(playerId);
	bb.put(ready);

	return bb;
    }

    @Override
    public int getLength() {
	return super.getLength() + Integer.BYTES + 1;
    }

}
