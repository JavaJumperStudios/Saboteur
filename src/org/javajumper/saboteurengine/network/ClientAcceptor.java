package org.javajumper.saboteurengine.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.javajumper.saboteur.SaboteurServer;

/**
 * Creates a new ServerSocket on port 5000 and accepts new client connections
 * and starts a new "ClientHandler" thread for each one
 */
public class ClientAcceptor implements Runnable {

	private ServerSocket socket;
	private SaboteurServer server;
	private boolean stop = false;

	/**
	 * Creates a new runnable ClientAcceptor. Start a new thread with this to
	 * start accepting new client connections
	 * 
	 * @param server
	 *            an instance of the current server
	 */
	public ClientAcceptor(SaboteurServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		Socket clientSocket = null;

		try {
			socket = new ServerSocket(5000);
			socket.setSoTimeout(1000);

			System.out.println("Server gestartet...");

			while (!stop) {
				try {
					clientSocket = socket.accept();
				} catch (SocketTimeoutException e) {
					continue;
				}

				ClientHandler client = new ClientHandler(clientSocket, server);
				Thread clientThread = new Thread(client);
				clientThread.start();
				server.addClientHandler(client);
				System.out.println("New Client Connected.");
			}
		} catch (IOException e) {
			System.out.println("Couldn't open Port. Server already open?");
			System.exit(0);
		} finally {
			try {
				if (clientSocket != null)
					clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Shuts down this client acceptor on the next update
	 */
	public void shutDown() {
		stop = true;
	}

}
