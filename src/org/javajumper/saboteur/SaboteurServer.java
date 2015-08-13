package org.javajumper.saboteur;

import java.awt.TextArea;
import java.io.IOException;
import java.util.ArrayList;

import org.javajumper.saboteur.map.MapServer;
import org.javajumper.saboteur.network.ClientAcceptor;
import org.javajumper.saboteur.network.ClientHandler;
import org.javajumper.saboteur.packet.Packet;
import org.javajumper.saboteur.packet.Packet07Snapshot;
import org.javajumper.saboteur.packet.Packet12PlayerSpawned;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.packet.Snapshot;
import org.javajumper.saboteur.player.Player;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.inventory.Gun;
import org.javajumper.saboteur.player.inventory.Item;
import org.newdawn.slick.geom.Vector2f;

public class SaboteurServer {

    public static void main(String[] args) {
	System.out.println("Server wird gestartet.");
	SaboteurServer server = new SaboteurServer();
	instance = server;
	server.start();
    }

    private boolean stop = false;
    private boolean pause = true;
    private ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private ArrayList<ClientHandler> removeList = new ArrayList<>();
    public static SaboteurServer instance;
    private int time;

    ArrayList<Player> players = new ArrayList<>();
    private MapServer map;

    Thread acceptor;

    private void start() {

	map = new MapServer();
	try {
	    map.loadMap("room.map");
	} catch (IOException e1) {
	    System.out.println("Karte konnte nicht geladen werden");
	}

	acceptor = new Thread(new ClientAcceptor(this));
	acceptor.start();

	time = 300000;
	int delta;
	long lastTimeMillis = System.currentTimeMillis();
	while (!stop) {
	    delta = (int) (System.currentTimeMillis() - lastTimeMillis);
	    if (delta < 10) {
		try {
		    Thread.sleep(10 - delta);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		delta = 10;
	    }
	    lastTimeMillis = System.currentTimeMillis();
	    update(delta);
	}

    }

    private void update(int delta) {
	if (!pause) {

	    time -= delta;

	    map.update(delta);

	    Packet07Snapshot packet = new Packet07Snapshot();
	    packet.snapshot = generateSnapshot();

	    if (!removeList.isEmpty()) {
		for (ClientHandler c : removeList) {
		    clientHandler.remove(c);
		}
		removeList.clear();
	    }

	    for (ClientHandler c : clientHandler) {
		if (c != null) {
		    c.sendToClient(packet);
		}
	    }

	    for (Player p : players) {
		p.update(delta);
	    }

	    checkWinConditions();
	}
    }

    public void checkWinConditions() {

	if (time <= 0) {
	    pause = true;

	}

    }

    public void addClientHandler(ClientHandler client) {
	clientHandler.add(client);
    }

    public Snapshot generateSnapshot() {
	Snapshot snapshot = new Snapshot();
	Player[] pl = new Player[players.size()];
	players.toArray(pl);

	PlayerSnapshot[] ps = new PlayerSnapshot[pl.length];

	for (int i = 0; i < pl.length; i++) {
	    ps[i] = pl[i].generateSnapshot();
	}

	snapshot.time = time;

	snapshot.player = ps;

	return snapshot;
    }

    public Player addNewPlayer(String name) {
	System.out.println("New Player added: " + name);
	Player p = new Player(Player.getNextId(), Role.LOBBY, name, 100, new Vector2f(0, 0));
	p.addItem(new Gun("TestGun", Item.nextId(), 1));
	players.add(p);

	Packet12PlayerSpawned packet12 = new Packet12PlayerSpawned();

	packet12.name = name;
	packet12.playerId = p.getId();
	packet12.role = Role.LOBBY.ordinal();
	packet12.x = 0;
	packet12.y = 0;

	broadcastPacket(packet12);

	return p;
    }

    public void broadcastPacket(Packet packet) {
	for (ClientHandler c : clientHandler) {
	    if (c.isLoggedIn())
		c.sendToClient(packet);
	}
    }

    public void pause() {
	pause = true;
	System.out.println("Paused.");
    }

    public void unpause() {
	pause = false;
	System.out.println("Unpaused!");
    }

    public ArrayList<Player> getPlayers() {
	return players;
    }

    public void removeClientHandler(ClientHandler ch) {
	removeList.add(ch);
    }

    /**
     * Wird aufgerufen, wenn ein Player ausgeloggt wird.
     * 
     * @param player
     *            der ausloggende Spieler
     */
    public void handlePlayerLogout(Player player) {
	System.out.println("Player " + player.getName() + " logged out.");
	players.remove(player);
    }

    public Packet12PlayerSpawned[] getPlayerSpawnPackets() {
	Packet12PlayerSpawned[] packets = new Packet12PlayerSpawned[players.size()];
	int i = 0;

	for (Player p : players) {
	    Packet12PlayerSpawned packet = new Packet12PlayerSpawned();
	    packet.name = p.getName();
	    packet.playerId = p.getId();
	    packet.role = p.getRole().ordinal();
	    packet.x = p.getPos().x;
	    packet.y = p.getPos().y;
	    packets[i++] = packet;
	}

	return packets;
    }

}
