package org.javajumper.saboteur.network;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.packet.Packet;
import org.javajumper.saboteur.packet.Packet01LoginRequest;
import org.javajumper.saboteur.packet.Packet02Login;
import org.javajumper.saboteur.packet.Packet10Ready;
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
	byte[] buffer = new byte[2048];
	System.out.println("Socket geöffnet");
	int size;

	try {
	    while (running
		    && (size = clientSocket.getInputStream().read(buffer)) != -1) {
		byte[] data = new byte[size];
		System.arraycopy(buffer, 0, data, 0, size);

		if (size == buffer.length)
		    System.out.println("SERVER OVERLOADED");
		ByteBuffer bb = ByteBuffer.wrap(data);

		while (bb.hasRemaining()) {

		    int oldPos = bb.position();
		    
		    int packetId = bb.get();
		    int length = bb.getInt();

		    System.out.println("Received Packet, ID: " + packetId + ", length: " + length);
		    
		    switch (packetId) {
		    case 1:
			System.out.println("Login Request received.");
			Packet01LoginRequest packetLoginRequest = new Packet01LoginRequest();
			packetLoginRequest.readFromByteBuffer(bb);

			player = server.addNewPlayer(packetLoginRequest.name);

			login = true;

			System.out.println("Sending Login Packet.");

			Packet02Login packetLogin = new Packet02Login();
			packetLogin.name = packetLoginRequest.name;
			packetLogin.playerId = player.getId();

			sendToClient(packetLogin);
			break;
		    case 10:
			System.out.println("Player");
			Packet10Ready packet10 = new Packet10Ready();
			packet10.readFromByteBuffer(bb);

			// server.setPlayerReady(packet10.playerId);
			// Server starts the Game now
			// TODO Implement ready states

			System.out
				.println("Received one Player ready, Game starts!");
			server.unpause();

			break;
		    default:
			System.out.println("Unknown Package");
			System.out.println("ID:" + packetId);
			break;
		    }

		    if (length != bb.position() - oldPos)
			System.out.println("Malformed Package, ID: " + packetId
				+ ", Discrepancy: "
				+ (bb.position() - oldPos - length));
		}

	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void sendToClient(Packet packet) {
	try {
	    byte[] data = packet.writeToByteArray();
	    clientSocket.getOutputStream().write(data);
	} catch (IOException e) {
	    System.out
		    .println("Could not send packet to Client. Closing Connection.");
	    close();
	}
    }

    public void close() {

    }

}
