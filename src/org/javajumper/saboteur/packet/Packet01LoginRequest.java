package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

public class Packet01LoginRequest extends Packet {

    public String name;
    public String password;
    
    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	String rname = "";
	String rpassword = "";
	char c;
	
	for (int i = 0; i<16;i++) {
	    rname = rname + bb.getChar();
	}
	
	for (int i = 0; i<16;i++) {
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
	
	for (int i = 0; i<16;i++) {
	    if (i<name.length())
		bb.putChar(name.charAt(i));
	    else
		bb.putChar('_');
	}
	
	for (int i = 0; i<16;i++) {
	    if (i<password.length())
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
