package org.javajumper.saboteurengine.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.javajumper.saboteur.SaboteurGame;
import org.javajumper.saboteur.network.packet.Packet13Role;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteurengine.StateManager;
import org.javajumper.saboteurengine.network.packet.Packet;
import org.javajumper.saboteurengine.network.packet.Packet01LoginRequest;
import org.javajumper.saboteurengine.network.packet.Packet02Login;
import org.javajumper.saboteurengine.network.packet.Packet04EndGame;
import org.javajumper.saboteurengine.network.packet.Packet05Logout;
import org.javajumper.saboteurengine.network.packet.Packet07Snapshot;
import org.javajumper.saboteurengine.network.packet.Packet08RejectLogin;
import org.javajumper.saboteurengine.network.packet.Packet10Ready;
import org.javajumper.saboteurengine.network.packet.Packet11SpawnDead;
import org.javajumper.saboteurengine.network.packet.Packet12PlayerSpawned;
import org.javajumper.saboteurengine.network.packet.Packet15SetMap;
import org.javajumper.saboteurengine.player.DeadPlayer;
import org.javajumper.saboteurengine.player.SPPlayer;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * Handles the connection to the server, sends and receives packets.
 */
public class ServerListener implements Runnable {

	private Socket socket;
	private boolean active = false;
	private SaboteurGame instance;
	private String name;
	private String password;

	/**
	 * Create a new ServerListener
	 * 
	 * @param instance
	 *            an instance of the current game
	 * @param server
	 *            the server ip
	 * @param port
	 *            the server port
	 * @param password
	 *            the password for the server
	 * @param name
	 *            the name of the main player/user
	 * @throws UnknownHostException
	 *             if the host could not be reached
	 * @throws IOException
	 */
	public ServerListener(SaboteurGame instance, String server, int port, String password, String name)
			throws UnknownHostException, IOException {
		this.instance = instance;
		this.name = name;
		this.password = password;
		socket = new Socket(server, port);
	}

	@Override
	public void run() {
		// Requesting Login...

		Packet01LoginRequest packet = new Packet01LoginRequest();

		packet.id = 1;
		if (name.equals("")) {
			packet.name = "JayJay";
		} else {
			packet.name = name;
		}
		packet.password = password;

		// Noch nicht benutzt
		// TODO packet.password = password;

		try {
			sendToServer(packet);
		} catch (Exception e) {
			System.out.println("Programm wird beendet.");
			System.exit(0);
		}

		byte[] buffer = new byte[524288];
		int size;

		System.out.println("Request sent, waiting for Login Packet...");

		try {
			while ((size = socket.getInputStream().read(buffer)) != -1) {

				byte[] data = new byte[size];

				System.arraycopy(buffer, 0, data, 0, size);

				if (size == buffer.length)
					System.out.println("CLIENT OVERLOADED");

				ByteBuffer bb = ByteBuffer.wrap(data);

				while (bb.hasRemaining()) {

					int oldPos = bb.position();

					byte id = bb.get();
					int length = bb.getInt();
					switch (id) {
					case 2:
						if (active)
							System.out.println("Ignoring redundant login Package.");
						else {
							Packet02Login loginPacket = new Packet02Login();
							loginPacket.readFromByteBuffer(bb);

							SPPlayer p = Packet02Login.createPlayerFromLoginPacket(loginPacket);

							instance.setMainPlayer(p);
							instance.addPlayer(p);
							System.out.println("Login complete. ID:" + p.getId());
							active = true;
						}
						break;
					case 3:
						System.out.println("Startpaket erhalten");
						instance.start();
						break;
					case 4:
						System.out.println("Endpacket erhalten");
						Packet04EndGame packet04 = new Packet04EndGame();
						packet04.readFromByteBuffer(bb);
						instance.setEndCause(packet04.endCause);
						break;
					case 5:
						System.out.println("Logout Packet erhalten");
						Packet05Logout packet05 = new Packet05Logout();
						packet05.readFromByteBuffer(bb);
						instance.handlePlayerLogout(packet05.playerId);
						break;
					case 7:
						if (!active) {
							System.out.println("Ignoring early Snapshot");
						}
						Packet07Snapshot packet07Snapshot = new Packet07Snapshot();
						packet07Snapshot.readFromByteBuffer(bb);
						instance.setSnapshot(packet07Snapshot.snapshot);
						instance.setTime(packet07Snapshot.snapshot.time);
						break;
					case 8:
						System.out.println("Login got rejected");
						Packet08RejectLogin packet08RejectLogin = new Packet08RejectLogin();
						packet08RejectLogin.readFromByteBuffer(bb);
						StateManager.changeState(0, new FadeOutTransition(Color.black, 1000),
								new FadeInTransition(Color.black, 1000));
						break;
					case 10:
						System.out.println("Player ready packet erhalten");
						Packet10Ready packet10 = new Packet10Ready();
						packet10.readFromByteBuffer(bb);
						instance.setPlayerReadyState(packet10.playerId, packet10.ready == 1);
						break;
					case 11:
						Packet11SpawnDead packet11 = new Packet11SpawnDead();
						packet11.readFromByteBuffer(bb);
						instance.spawnDeadPlayer(new DeadPlayer(packet11.playerId, packet11.name,
								Role.values()[packet11.role], packet11.timeOfDeath, packet11.killerId, packet11.itemId,
								new Vector2f(packet11.posX, packet11.posY)));
						for (SPPlayer p : instance.getPlayers()) {
							if (p.getId() == packet11.playerId) {
								p.setRole(Role.SPECTATE);
								p.setDead(true);
							}
						}
						break;
					case 12:
						Packet12PlayerSpawned packet12 = new Packet12PlayerSpawned();
						packet12.readFromByteBuffer(bb);
						instance.addPlayer(new SPPlayer(packet12.playerId, Role.values()[packet12.role], packet12.name,
								100, new Vector2f(packet12.x, packet12.y), "Fuzzi_Neutral.png"));
						instance.setPlayerReadyState(packet12.playerId, packet12.ready == 1);
						break;
					case 13:
						Packet13Role packet13 = new Packet13Role();
						packet13.readFromByteBuffer(bb);
						instance.setRole(packet13.playerId, Role.values()[packet13.role]);
						break;
					case 14:
						System.out.println("Reset-Packet erhalten");
						instance.reset();
						break;
					case 15:
						Packet15SetMap packet15 = new Packet15SetMap();
						packet15.readFromByteBuffer(bb);
						try {
							instance.getMap().saveMap(instance.deleteUnderscores(packet15.mapName), packet15.map, packet15.width, packet15.height, packet15.collisionShapes);
							instance.loadMap(instance.deleteUnderscores(packet15.mapName));
						} catch (IOException e) {
							// TODO log, map could not be saved
							e.printStackTrace();
						}
						break;
					default:
						System.out.println("Unknown Package");
						System.out.println("ID:" + id);
						break;
					}
					if (length != bb.position() - oldPos)
						System.out.println(
								"Malformed Package, ID: " + id + ", Discrepancy: " + (bb.position() - oldPos - length));
				}
			}
		} catch (IOException e) {
			// TODO LOG something went wrong with the socket this also happens
			// if the socket is closed
			e.printStackTrace();
		}
	}

	/**
	 * Closes the connection to the server
	 */
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Couldn't close");
			e.printStackTrace();
		}
	}

	/**
	 * Sends a packet to the server
	 * 
	 * @param p
	 *            the packet to send
	 */
	public void sendToServer(Packet p) {
		try {
			socket.getOutputStream().write(p.writeToByteArray());
		} catch (IOException e) {
			instance.scheduleGameExit();
			System.out.println("Server not reachable. Closing.");
			System.exit(0);
		}
	}

}
