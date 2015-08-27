package org.javajumper.saboteur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.javajumper.saboteur.map.MapServer;
import org.javajumper.saboteur.network.ClientAcceptor;
import org.javajumper.saboteur.network.ClientHandler;
import org.javajumper.saboteur.packet.Packet;
import org.javajumper.saboteur.packet.Packet03StartGame;
import org.javajumper.saboteur.packet.Packet04EndGame;
import org.javajumper.saboteur.packet.Packet05Logout;
import org.javajumper.saboteur.packet.Packet07Snapshot;
import org.javajumper.saboteur.packet.Packet11SpawnDead;
import org.javajumper.saboteur.packet.Packet12PlayerSpawned;
import org.javajumper.saboteur.packet.Packet13Role;
import org.javajumper.saboteur.packet.Packet14Reset;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.packet.Snapshot;
import org.javajumper.saboteur.player.DeadPlayer;
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
    private int time; // Zeit in Millisekunden
    private boolean start;
    private boolean[] blocked = new boolean[5];

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<DeadPlayer> deadplayers = new ArrayList<>();
    private MapServer map;

    Thread acceptor;

    private void start() {

	time = 0;
	start = false;
	map = new MapServer();
	for (int j = 0; j <= 4; j++) {
	    blocked[j] = false;
	}
	try {
	    map.loadMap("room.map");
	} catch (IOException e1) {
	    System.out.println("Karte konnte nicht geladen werden");
	}

	acceptor = new Thread(new ClientAcceptor(this));
	acceptor.start();

	time = 60000;
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

	if (start) {

	    if (!pause) {
		map.update(delta);

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
	} else {

	    if (players.size() == 0)
		return;
	    boolean allReady = true;

	    for (Player p : (ArrayList<Player>) players.clone()) {
		if (!p.ready())
		    allReady = false;
	    }

	    if (allReady) {
		Packet03StartGame packet03 = new Packet03StartGame();
		broadcastPacket(packet03);
		start = true;
		unpause();
		initGameStats();
		System.out.println("Alle bereit");
	    }

	}

    }

    public void initGameStats() {

	for (Player p : players) {
	    p.setPos(getSpawnPosition());
	    p.setRole(Role.INNOCENT);
	}

	Player[] pl = new Player[players.size()];
	players.toArray(pl);
	Random r = new Random();
	if (players.size() <= 4) {
	    pl[r.nextInt(players.size())].setRole(Role.TRAITOR);
	} else {
	    pl[r.nextInt(players.size())].setRole(Role.TRAITOR);
	    pl[r.nextInt(players.size())].setRole(Role.TRAITOR); // Falls es 2
								 // mal den
								 // gleichen
								 // Player
								 // treffen
								 // sollte, gibt
								 // es eben nur
								 // 1 Traitor.
	}

	for (Player p : players) {
	    Packet13Role packet13 = new Packet13Role();
	    packet13.playerId = p.getId();
	    packet13.role = p.getRole().ordinal();
	    broadcastPacket(packet13);
	}

    }

    public void checkWinConditions() {

	if (time <= 0) {
	    // Innocent gewinnt durch Zeit

	    sendEndPacket(0);

	} else {

	    boolean innoAlive = false;
	    boolean traitorAlive = false;

	    for (Player p : players) {
		if (!p.getDead() && p.getRole() == Role.INNOCENT) {
		    innoAlive = true;
		} else if (!p.getDead() && p.getRole() == Role.TRAITOR) {
		    traitorAlive = true;
		}
	    }

	    if (!innoAlive && !traitorAlive) {
		// Unentschieden
		sendEndPacket(3);
	    } else if (innoAlive && !traitorAlive) {
		// Inno gewonnen
		sendEndPacket(1);
	    } else if (!innoAlive && traitorAlive) {
		// Traitor gewonnen
		sendEndPacket(2);
	    }

	}
    }

    public void sendEndPacket(int endCause) {
	Packet04EndGame packet04 = new Packet04EndGame();
	packet04.endCause = (byte) endCause;
	broadcastPacket(packet04);
	pause = true;

	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	resetServer();
    }

    public void resetServer() {

	start = false;

	time = 60000;
	map = new MapServer();
	for (int j = 0; j <= 4; j++) {
	    blocked[j] = false;
	}
	try {
	    map.loadMap("room.map");
	} catch (IOException e1) {
	    System.out.println("Karte konnte nicht geladen werden");
	}

	for (Player p : (ArrayList<Player>) players.clone()) {

	    p.setDead(false);
	    p.setSprint(false);
	    p.setLivepoints(100);
	    p.setRole(Role.LOBBY);
	    p.setReadyState(false);

	}
	deadplayers.clear();

	Packet14Reset packet14 = new Packet14Reset();
	broadcastPacket(packet14);
    }

    public void setPlayerReadyState(int id, byte ready) {

	for (Player p : players) {
	    if (p.getId() == id) {
		if (ready == 0) {
		    p.setReadyState(false);
		    System.out.println("Player is not Ready");
		} else {
		    p.setReadyState(true);
		    System.out.println("Player is now Ready");
		}
	    }
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

    public Vector2f getSpawnPosition() {
	Vector2f v = new Vector2f(0, 0);
	boolean found = false;

	while (!found) {
	    double i = Math.random();

	    if (i <= 0.2 && !blocked[0]) {
		v = new Vector2f(0, 0);
		blocked[0] = true;
		found = true;
	    } else if (i > 0.2 && i <= 0.4 && !blocked[1]) {
		v = new Vector2f(1248, 0);
		blocked[1] = true;
		found = true;
	    } else if (i > 0.4 && i <= 0.6 && !blocked[2]) {
		v = new Vector2f(0, 928);
		blocked[2] = true;
		found = true;
	    } else if (i > 0.6 && i <= 0.8 && !blocked[3]) {
		v = new Vector2f(1248, 928);
		blocked[3] = true;
		found = true;
	    } else if (i > 0.8 && i <= 1.0 && !blocked[4]) {
		v = new Vector2f(640, 512);
		blocked[4] = true;
		found = true;
	    }
	}

	return v;
    }

    public Player addNewPlayer(String name) {
	System.out.println("New Player added: " + name);

	Player p = new Player(Player.getNextId(), Role.LOBBY, name, 100, new Vector2f(0, 0));
	p.addItem(new Gun(this, "TestGun", Item.nextId(), 1));
	players.add(p);

	Packet12PlayerSpawned packet12 = new Packet12PlayerSpawned();

	packet12.name = name;
	packet12.playerId = p.getId();
	packet12.role = Role.LOBBY.ordinal();
	packet12.x = 0;
	packet12.y = 0;
	packet12.ready = (byte) (p.ready() ? 1 : 0);
	broadcastPacket(packet12);
	return p;
    }

    public void broadcastPacket(Packet packet) {
	for (ClientHandler c : clientHandler) {
	    if (c.isLoggedIn()) {
		c.sendToClient(packet);
	    }
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

    public int getTime() {
	return time;
    }

    public ArrayList<Player> getPlayers() {
	return players;
    }

    public void removeClientHandler(ClientHandler ch) {
	removeList.add(ch);
    }

    public void deadPlayer(DeadPlayer dp) {
	for (Player p : players) {
	    if (p.getId() == dp.getId()) {
		p.setDead(true);
		break;
	    }
	}

	deadplayers.add(dp);
	Packet11SpawnDead packet11 = new Packet11SpawnDead();
	packet11.playerId = dp.getId();
	packet11.timeOfDeath = dp.getTimeOfDeath();
	packet11.killerId = dp.getPlayerOfImpact();
	packet11.itemId = dp.getItemOfImpact();
	packet11.posX = dp.getPos().x;
	packet11.posY = dp.getPos().y;
	packet11.role = dp.getRole().ordinal();
	packet11.name = dp.getName();

	broadcastPacket(packet11);
    }

    /**
     * Wird aufgerufen, wenn ein Player ausgeloggt wird.
     * 
     * @param player
     *            der ausloggende Spieler
     */
    public void handlePlayerLogout(Player player) {
	System.out.println("Player " + player.getName() + " logged out.");
	if (players.contains(player)) {
	    players.remove(player);
	}
	for (DeadPlayer dp : (ArrayList<DeadPlayer>) deadplayers.clone()) {
	    if (dp.getId() == player.getId()) {
		deadplayers.remove(dp);
	    }
	}

	Packet05Logout packet05 = new Packet05Logout();
	packet05.playerId = player.getId();
	broadcastPacket(packet05);

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
	    packet.ready = (byte) (p.ready() ? 1 : 0);
	    packets[i++] = packet;
	}

	return packets;
    }

    public MapServer getMap() {
	return map;
    }

}
