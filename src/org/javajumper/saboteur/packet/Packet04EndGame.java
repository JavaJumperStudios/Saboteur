package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet04EndGame extends Packet {
    
    public byte endCause;
    /*
     * 0 = Zeit ausgelaufen --> Innocent gewinnen
     * 1 = Alle Traitor tot --> Innocent gewinnen
     * 2 = Alle Innocents tot --> Traitor gewinnen
     * 3 = Alle tot --> Reset
     */

    public Packet04EndGame() {
	super((byte) 4);
    }

    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	endCause = bb.get();
	
    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());
	bb.put(id);
	bb.putInt(getLength());
	
	bb.put(endCause);
	
	
	return bb;
    }
    
    @Override
    public int getLength() {
	return super.getLength() + 1;
    }

}
