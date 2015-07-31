package org.javajumper.saboteur.network;

import java.net.Socket;

import org.javajumper.saboteur.Saboteur;
import org.javajumper.saboteur.packet.Packet;
import org.javajumper.saboteur.player.Player;

public class ClientHandler implements Runnable {
	
	private Socket socket;
	private Player player;
	private boolean login;
	private Saboteur server;
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void sendToClient(Packet p) {
		
	}
	
	public void close() {
		
	}
	
	

	
	

}
