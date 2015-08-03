package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet01LoginRequest extends Packet {

    public char[] name;
    public char[] password;
    
    @Override
    protected void readFromByteBuffer(ByteBuffer bb) {
	int leftBytes = length - super.getLength();
	String rname = "";
	String rpassword = "";
	char c;
	
	while ((c = bb.getChar()) != '#') {
	    rname = rname + c;
	    leftBytes -= Character.BYTES;
	}
	
	while (leftBytes != 0) {
	    c = bb.getChar();
	    rpassword = rpassword + c;
	    leftBytes -= Character.BYTES;
	}
	
	this.name = rname.toCharArray();
	this.password = rpassword.toCharArray();
    }
    
    @Override
    protected ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());
	
	bb.put(id);
	bb.putInt(getLength());
	
	for (char c : name) {
	    bb.putChar(c);
	}
	
	bb.putChar('#');
	
	for (char c : password) {
	    bb.putChar(c);
	}
	
	return bb;
    }
    
    @Override
    public int getLength() {
	return super.getLength() + Character.BYTES * (name.length + 1) + Character.BYTES * password.length;
    }

}
