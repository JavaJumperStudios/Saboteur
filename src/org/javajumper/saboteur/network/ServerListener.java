package org.javajumper.saboteur.network;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.javajumper.saboteur.RessourceManager;
import org.javajumper.saboteur.SaboteurGame;
import org.javajumper.saboteur.packet.Packet;
import org.javajumper.saboteur.packet.Packet01LoginRequest;
import org.javajumper.saboteur.packet.Packet02Login;
import org.javajumper.saboteur.packet.Packet07Snapshot;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.SPPlayer;
import org.newdawn.slick.geom.Vector2f;

public class ServerListener implements Runnable {

    private Socket socket;
    private boolean active = false;
    private SaboteurGame instance;

    public ServerListener(SaboteurGame instance, String server, int port) {
	this.instance = instance;

	try {
	    socket = new Socket(server, port);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void run() {
	// Requesting Login...

	Packet01LoginRequest packet = new Packet01LoginRequest();

	packet.id = 1;
	packet.name = "Jakob";
	packet.password = "";

	sendToServer(packet);

	byte[] buffer = new byte[2048];
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
			    System.out
				    .println("Ignoring redundant login Package.");
			else {
			    Packet02Login loginPacket = new Packet02Login();
			    loginPacket.readFromByteBuffer(bb);

			    SPPlayer p = instance
				    .createPlayerFromLoginPacket(loginPacket);

			    instance.setMainPlayer(p);
			    instance.addPlayer(p);
			    System.out.println("Login complete. ID:"
				    + p.getId());
			    active = true;
			}
			break;
		    case 7:
			if (!active) {
			    System.out.println("Ignoring early Snapshot");
			}
			
			Packet07Snapshot packet07Snapshot = new Packet07Snapshot();
			packet07Snapshot.readFromByteBuffer(bb);
			instance.getMap()
				.setSnapshot(packet07Snapshot.snapshot);
			break;
		    /*
		     * case 3: Packet03NewPlayer newPlayerPacket = new
		     * Packet03NewPlayer();
		     * newPlayerPacket.readFromByteBuffer(bb); Player p =
		     * createPlayer(newPlayerPacket); instance.addPlayer(p);
		     * System.out.println("NewPlayer logged in: " +
		     * p.getName()); break; case 6: Packet06PlayerSpawn
		     * spawnpacket = new Packet06PlayerSpawn();
		     * spawnpacket.readFromByteBuffer(bb);
		     * System.out.println("Spawnpacket received ID: " +
		     * spawnpacket.playerId);
		     * instance.spawn(instance.getPlayer(spawnpacket.playerId),
		     * spawnpacket.posX, spawnpacket.posY); break; case 8:
		     * Packet08PlayerDespawn packet08 = new
		     * Packet08PlayerDespawn(); packet08.readFromByteBuffer(bb);
		     * 
		     * instance.theMap.despawn(instance.theMap
		     * .getPlayer(packet08.playerid));
		     * 
		     * break;
		     */
		    default:
			System.out.println("Unknown Package");
			System.out.println("ID:" + id);
			break;
		    }
		    if (length != bb.position() - oldPos)
			System.out.println("Malformed Package, ID: " + id
				+ ", Discrepancy: "
				+ (bb.position() - oldPos - length));
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void close() {
	try {
	    socket.close();
	} catch (IOException e) {
	    System.out.println("Couldn't close");
	    e.printStackTrace();
	}
    }

    public void sendToServer(Packet p) {
	try {
	    socket.getOutputStream().write(p.writeToByteArray());
	} catch (IOException e) {
	    instance.exitGame();
	    System.out.println("Server not reachable. Closing.");
	    System.exit(0);
	}
    }

}
