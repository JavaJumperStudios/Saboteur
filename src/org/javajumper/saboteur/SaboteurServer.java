package org.javajumper.saboteur;

import java.util.ArrayList;

import org.javajumper.saboteur.map.Map;
import org.javajumper.saboteur.network.ClientAcceptor;
import org.javajumper.saboteur.network.ClientHandler;
import org.javajumper.saboteur.packet.Packet07Snapshot;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.packet.Snapshot;
import org.javajumper.saboteur.player.Player;
import org.javajumper.saboteur.player.Role;
import org.newdawn.slick.geom.Vector2f;

public class SaboteurServer {

    public static void main(String[] args) {
	System.out.println("Server wird gestartet.");
	SaboteurServer server = new SaboteurServer();
	server.start();
    }

    private boolean stop;
    private boolean pause = true;
    private ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private ArrayList<ClientHandler> removeList = new ArrayList<>();

    ArrayList<Player> players = new ArrayList<>();
    private Map map;

    Thread acceptor;

    private void start() {

	map = new Map();
	map.loadMap();

	acceptor = new Thread(new ClientAcceptor(this));
	acceptor.start();

	int delta;
	long lastTimeMillis = System.currentTimeMillis();

	while (!stop) {
	    delta = (int) (System.currentTimeMillis() - lastTimeMillis);
	    lastTimeMillis = System.currentTimeMillis();

	    update(delta);
	}

    }

    private void update(int delta) {
	if (!pause) {
	    map.update(delta);

	    Packet07Snapshot packet = new Packet07Snapshot();
	    packet.snapshot = generateSnapshot();

	    for (ClientHandler c : clientHandler) {
		if (c != null) {
		    c.sendToClient(packet);
		}
	    }

	    if (!removeList.isEmpty()) {
		for (ClientHandler c : removeList) {
		    clientHandler.remove(c);
		}
		removeList.clear();
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
	Player p = new Player(Player.getNextId(), Role.LOBBY, name, 100,
		new Vector2f(0, 0));
	players.add(p);
	return p;
    }

}
