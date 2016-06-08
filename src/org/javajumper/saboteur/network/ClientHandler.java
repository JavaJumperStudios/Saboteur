package org.javajumper.saboteur.network;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.packet.Packet;
import org.javajumper.saboteur.packet.Packet01LoginRequest;
import org.javajumper.saboteur.packet.Packet02Login;
import org.javajumper.saboteur.packet.Packet06UseItem;
import org.javajumper.saboteur.packet.Packet09PlayerUpdate;
import org.javajumper.saboteur.packet.Packet10Ready;
import org.javajumper.saboteur.packet.Packet12PlayerSpawned;
import org.javajumper.saboteur.packet.Packet15SetMap;
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
		byte[] buffer = new byte[524288];
		System.out.println("Socket ge�ffnet");
		int size;

		try {
			while (running && (size = clientSocket.getInputStream().read(buffer)) != -1) {
				byte[] data = new byte[size];
				System.arraycopy(buffer, 0, data, 0, size);

				if (size == buffer.length)
					System.out.println("SERVER OVERLOADED");
				ByteBuffer bb = ByteBuffer.wrap(data);

				while (bb.hasRemaining()) {

					int oldPos = bb.position();

					int packetId = bb.get();
					int length = bb.getInt();

					switch (packetId) {
					case 1:
						System.out.println("Login Request received.");
						Packet01LoginRequest packetLoginRequest = new Packet01LoginRequest();
						packetLoginRequest.readFromByteBuffer(bb);
						Packet12PlayerSpawned[] spawnPackets = server.getPlayerSpawnPackets();
						player = server.addNewPlayer(packetLoginRequest.name);

						login = true;

						System.out.println("Sending Login Packet.");

						Packet02Login packetLogin = new Packet02Login();
						packetLogin.name = packetLoginRequest.name;
						packetLogin.playerId = player.getId();

						sendToClient(packetLogin);

						Packet15SetMap packetSetMap = new Packet15SetMap();
						packetSetMap.mapName = server.getMap().getName();

						int height = server.getMap().getHeight();
						int width = server.getMap().getWidth();

						packetSetMap.height = height;
						packetSetMap.width = width;

						packetSetMap.map = new Integer[width][height];

						for (int i = 0; i < width; i++) {
							for (int j = 0; j < height; j++) {
								packetSetMap.map[i][j] = server.getMap().getTile(i, j).getTypeId();
							}
						}

						sendToClient(packetSetMap);

						for (Packet p : spawnPackets) {
							sendToClient(p);
						}

						break;

					case 6:
						Packet06UseItem packet06 = new Packet06UseItem();
						packet06.readFromByteBuffer(bb);

						player.getInventory()[2].use(player, server);

						break;
					case 9:
						Packet09PlayerUpdate packet09 = new Packet09PlayerUpdate();
						packet09.readFromByteBuffer(bb);

						player.getMove().x = packet09.moveX;
						player.getMove().y = packet09.moveY;
						player.setCurrentWeapon(packet09.currentItem);
						player.setSprint(packet09.sprinting != 0);
						player.setAngle(packet09.lookAngle);
						break;
					case 10:
						Packet10Ready packet10 = new Packet10Ready();
						packet10.readFromByteBuffer(bb);
						server.setPlayerReadyState(packet10.playerId, packet10.ready);
						server.broadcastPacket(packet10);
						// Server starts the Game now
						break;
					case 14:
						server.resetServer();
						break;
					default:
						System.out.println("Unknown Package");
						System.out.println("ID:" + packetId);
						break;
					}

					if (length != bb.position() - oldPos)
						System.out.println("Malformed Package, ID: " + packetId + ", Discrepancy: "
								+ (bb.position() - oldPos - length));
				}

			}
		} catch (IOException e) {
			System.out.println("Player " + player.getName() + " has closed the Connection.");
			close();
		}
	}

	public void sendToClient(Packet packet) {
		try {
			byte[] data = packet.writeToByteArray();
			clientSocket.getOutputStream().write(data);
		} catch (IOException e) {
			System.out.println("Could not send packet to Client. Closing Connection.");
			close();
		}
	}

	public void close() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Could not even close the Socket :( Client sad");
		}
		login = false;
		server.removeClientHandler(this);
		server.handlePlayerLogout(player);
	}

	public boolean isLoggedIn() {
		return login;
	}

}
