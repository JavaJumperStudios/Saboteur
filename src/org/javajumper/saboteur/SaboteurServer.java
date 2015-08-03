package org.javajumper.saboteur;

import java.util.ArrayList;

import org.javajumper.saboteur.map.Map;
import org.javajumper.saboteur.network.ClientAcceptor;
import org.javajumper.saboteur.network.ClientHandler;
import org.javajumper.saboteur.packet.Packet07Snapshot;
import org.javajumper.saboteur.player.Player;

public class SaboteurServer {

    public static void main(String[] args) {
	System.out.println("Server wird gestartet.");
	SaboteurServer server = new SaboteurServer();
	server.start();
    }

    private boolean stop;
    private ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private ArrayList<ClientHandler> removeList = new ArrayList<ClientHandler>();

    ArrayList<Player> players = new ArrayList<>();
    private Map map;

    Thread acceptor;

    private void start() {

	Map map = new Map();
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
	map.update();
	Packet07Snapshot packet = new Packet07Snapshot();
	packet.snapshot = map.generateSnapshot();

	for (ClientHandler c : clientHandler) {
	    if (c != null) {
		c.sendToClient(packet);
	    }
	}
	
	if (!removeList.isEmpty()) {
	    for (ClientHandler c : removeList) {
		clientHandler.remove(c);
	    }
	}
    }

    public void addClientHandler(ClientHandler client) {
	clientHandler.add(client);
    }

}
