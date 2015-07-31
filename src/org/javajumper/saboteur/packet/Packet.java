package org.javajumper.saboteur.packet;

public class Packet {

    private byte id;
    private int length;

    protected int getLength() {
	return length;
    }

}
