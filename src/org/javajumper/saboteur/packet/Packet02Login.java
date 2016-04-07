package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.SPPlayer;
import org.newdawn.slick.geom.Vector2f;

public class Packet02Login extends Packet {

    public int playerId;
    public String name;

    public Packet02Login() {
	super((byte) 2);
    }
    
    @Override
    public void readFromByteBuffer(ByteBuffer bb) {
	playerId = bb.getInt();
	
	name = "";
	
	for (int i = 0; i < 16; i++) {
	    name = name + bb.getChar();
	}
    }

    @Override
    public ByteBuffer writeToByteBuffer() {
	ByteBuffer bb = ByteBuffer.allocate(getLength());

	bb.put(id);
	bb.putInt(getLength());

	bb.putInt(playerId);
	
	for (int i = 0; i < 16; i++) {
	    if (i< name.length())
		bb.putChar(name.charAt(i));
	    else
		bb.putChar('_');
	}
	
	return bb;
    }

	@Override
	public int getLength() {
		return super.getLength() + Integer.BYTES + Character.BYTES * 16;
	}
    
    public static SPPlayer createPlayerFromLoginPacket(Packet02Login loginPacket) {

	SPPlayer p = new SPPlayer(loginPacket.playerId, Role.LOBBY, loginPacket.name, 100, new Vector2f(0, 0), "Fuzzi_Neutral.png");

	return p;
    }

}
