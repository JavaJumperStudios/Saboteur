package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet09PlayerUpdate extends Packet {

    public float lookAngle;
    public int currentItem;
    public float moveX;
    public float moveY;
    public boolean sprinting;

    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	// TODO Auto-generated method stub

    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	// TODO Auto-generated method stub
	return null;
    }

}
