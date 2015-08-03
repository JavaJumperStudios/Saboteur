package org.javajumper.saboteur.network;

import java.io.IOException;
import java.net.Socket;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.packet.Packet;
import org.javajumper.saboteur.packet.Packet01LoginRequest;
import org.javajumper.saboteur.player.Player;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Player player;
    private boolean login = false;
    private SaboteurServer server;
    private boolean running = true;

    public ClientHandler(Socket clientSocket, SaboteurServer server) {
	this.clientSocket = clientSocket;
	this.server = server;
    }
    
    @Override
    public void run() {
	byte[] buffer = new byte[512];
	System.out.println("Socket geöffnet");
	int size;
	
	try {
	    while (running && (size = clientSocket.getInputStream().read(buffer)) != -1) {
	        byte[] data = new byte[size];
	        System.arraycopy(buffer, 0, data, 0, size);
	        
	        if (!login) {
	            if(data[0] == 1) {
	        	System.out.println("Login Request received.");
	        	Packet01LoginRequest packet = new Packet01LoginRequest();
	        	packet.readFromByteArray(data);
	            }
	        }
	        
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void sendToClient(Packet p) {

    }

    public void close() {

    }

}
