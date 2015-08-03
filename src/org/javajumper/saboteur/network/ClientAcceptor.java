package org.javajumper.saboteur.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.javajumper.saboteur.SaboteurServer;

public class ClientAcceptor implements Runnable {

	private ServerSocket socket;
	private SaboteurServer server;
	
	public ClientAcceptor(SaboteurServer server) {
	    this.server = server;
	}
	
	@Override
	public void run() {
		try {
			socket = new ServerSocket(5000);

			System.out.println("Server gestartet...");
			Socket clientSocket = null;
			
			while ((clientSocket = socket.accept()) != null ) {
				ClientHandler client = new ClientHandler(clientSocket, server);
				Thread clientThread = new Thread(client);
				clientThread.start();
				server.addClientHandler(client);
				System.out.println("New Client Connected.");
			}
		} catch (IOException e) {
			System.out.println("Couldn't open Port. Server already open?");
			System.exit(0);
		}
	}

}
