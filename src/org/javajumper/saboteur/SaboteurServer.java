package org.javajumper.saboteur;

import java.util.ArrayList;

import org.javajumper.saboteur.map.MapServer;
import org.javajumper.saboteur.network.ClientAcceptor;
import org.javajumper.saboteur.network.ClientHandler;
import org.javajumper.saboteur.packet.Packet07Snapshot;
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
	server.start();
    }

    private boolean stop = false;
    private boolean pause = true;
    private ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private ArrayList<ClientHandler> removeList = new ArrayList<>();

    ArrayList<Player> players = new ArrayList<>();
    private MapServer map;

    Thread acceptor;

    private void start() {

	map = new MapServer();
	map.loadMap();

	acceptor = new Thread(new ClientAcceptor(this));
	acceptor.start();

	int delta;
	long lastTimeMillis = System.currentTimeMillis();
	while (!stop) {
	    delta = (int) (System.currentTimeMillis() - lastTimeMillis);
	    lastTimeMillis = System.currentTimeMillis();

	    if (delta < 20) {
		try {
		    Thread.sleep(20 - delta);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		delta = 20;
	    }

	    update(delta);
	}

    }

    private void update(int delta) {
	if (!pause) {

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

	snapshot.player = ps;

	return snapshot;
    }

    public Player addNewPlayer(String name) {
	System.out.println("New Player added: " + name);
	Player p = new Player(Player.getNextId(), Role.LOBBY, name, 100, new Vector2f(0, 0));
	p.addItem(new Gun("TestGun", Item.nextId(), 1));
	p.setCurrentWeapon(2);
	players.add(p);
	return p;
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

}
