package org.javajumper.saboteur;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import org.javajumper.saboteur.map.Map;
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

/**
 * The server class for the saboteur game.
 */
public class SaboteurServer {

	/** The main instance of the server made public for convenience */
	public static SaboteurServer instance;

	/** The main Logger for the server */
	public final static Logger LOGGER = Logger.getLogger(SaboteurServer.class.getName());

	public static final int TIME_RAN_OUT = 0;
	public static final int NO_TRAITORS_LEFT = 1;
	public static final int NO_INNOCENTS_LEFT = 2;
	public static final int NO_PLAYERS_LEFT = 3;

	/* Optionen */

	// Dauer eines Spiels in Millisekunden
	private int gameDuration;

	// Minimale Anzahl von Spielern
	private int minPlayerCount;

	/* Gamestate */

	private boolean stop = false;
	private boolean pause = true;
	private int timeLeft;
	private boolean running;

	/* Clients */

	private ArrayList<ClientHandler> clientHandler = new ArrayList<>();
	private ArrayList<ClientHandler> removeList = new ArrayList<>();
	private boolean[] blockedSpawnPositions = new boolean[5];

	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<DeadPlayer> deadplayers = new ArrayList<>();
	private Map map;

	private Thread acceptor;

	/**
	 * Initializes the server
	 */
	public void init() {
		loadProperties();

		timeLeft = gameDuration;
		running = false;
		map = new Map();
		for (int j = 0; j <= 4; j++) {
			blockedSpawnPositions[j] = false;
		}
		try {
			map.loadMap("room.map");
		} catch (IOException e1) {
			System.out.println("Karte konnte nicht geladen werden");
		}

		acceptor = new Thread(new ClientAcceptor(this));
		acceptor.start();
	}

	private void initGameStats() {

		for (Player p : players) {
			p.setPos(getFreeSpawnPosition());
			p.setRole(Role.INNOCENT);
		}

		Player[] pl = new Player[players.size()];
		players.toArray(pl);
		Random r = new Random();
		if (players.size() <= 4) {
			pl[r.nextInt(players.size())].setRole(Role.TRAITOR);
		} else {
			pl[r.nextInt(players.size())].setRole(Role.TRAITOR);
			pl[r.nextInt(players.size())].setRole(Role.TRAITOR);
			// Falls es 2 mal den gleichen Player treffen sollte, gibt es eben
			// nur 1 Traitor.
		}

		for (Player p : players) {
			Packet13Role packet13 = new Packet13Role();
			packet13.playerId = p.getId();
			packet13.role = p.getRole().ordinal();
			broadcastPacket(packet13);
		}

	}

	private void loadProperties() {
		Properties propertyFile = new Properties();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream("saboteur-server.properties");
			propertyFile.load(fis);
		} catch (IOException e) {
			// Do nothing, we have default fallbacks
		} finally {
			try {
				fis.close();
			} catch (IOException | NullPointerException e) {}
		}

		gameDuration = Integer.parseInt(propertyFile.getProperty("game_duration", "600000"));
		minPlayerCount = Integer.parseInt(propertyFile.getProperty("min_player_count", "2"));
	}

	/**
	 * Starts the server
	 */
	public void start() {
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

		if (running) {

			if (!pause) {
				map.update(delta);

				timeLeft -= delta;

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

			if (players.size() < minPlayerCount)
				return;

			boolean allReady = true;

			for (Player p : new ArrayList<>(players)) {
				if (!p.isReady())
					allReady = false;
			}

			if (allReady) {
				Packet03StartGame packet03 = new Packet03StartGame();
				broadcastPacket(packet03);
				running = true;
				unpause();
				initGameStats();
				System.out.println("Alle bereit");
			}

		}

	}

	/**
	 * Pauses the game
	 */
	public void pause() {
		pause = true;
		System.out.println("Paused.");
	}

	/**
	 * Unpauses the game
	 */
	public void unpause() {
		pause = false;
		System.out.println("Unpaused!");
	}

	/**
	 * Checks if win conditions are met and ends the game if so
	 */
	public void checkWinConditions() {

		if (timeLeft <= 0) {
			// Innocents gewinnen durch Zeit

			sendEndPacket(0);

		} else {

			boolean innoAlive = false;
			boolean traitorAlive = false;

			for (Player p : players) {
				if (!p.isDead() && p.getRole() == Role.INNOCENT) {
					innoAlive = true;
				} else if (!p.isDead() && p.getRole() == Role.TRAITOR) {
					traitorAlive = true;
				}
			}

			// TODO if debug...
			if (true) // To avoid dead code error
				return;

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

	/**
	 * Sends a packet to the clients that the game has ended
	 * 
	 * @param endCause
	 *            why the game has ended. Use one of {@link #TIME_RAN_OUT},
	 *            {@link #NO_TRAITORS_LEFT}, {@link #NO_INNOCENTS_LEFT},
	 *            {@value #NO_PLAYERS_LEFT}
	 */
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

	/**
	 * Resets the game, including time, the map, dead players and items.
	 */
	public void resetServer() {

		running = false;

		timeLeft = gameDuration;
		map = new MapServer();
		for (int j = 0; j <= 4; j++) {
			blockedSpawnPositions[j] = false;
		}
		try {
			map.loadMap("room.map");
		} catch (IOException e1) {
			System.out.println("Karte konnte nicht geladen werden");
		}

		for (Player p : (new ArrayList<>(players))) {

			p.setDead(false);
			p.setSprinting(false);
			p.resetLifepoints();
			p.setRole(Role.LOBBY);
			p.setReadyState(false);

		}
		deadplayers.clear();

		Packet14Reset packet14 = new Packet14Reset();
		broadcastPacket(packet14);
	}

	/**
	 * Sets the ready-state of a player with a certain id
	 * 
	 * @param id
	 *            the id of the player whose ready-state should be set
	 * @param ready
	 *            true if the player should be ready, false otherwise
	 */
	public void setReadyStateByPlayerId(int id, byte ready) {

		for (Player p : players) {
			if (p.getId() == id) {
				if (ready == 0) {
					p.setReadyState(false);
					// TODO LOG
					System.out.println("Player is now not Ready");
				} else {
					p.setReadyState(true);
					System.out.println("Player is now Ready");
				}
			}
		}

	}

	/**
	 * Adds a new player to the game
	 * 
	 * @param name
	 *            the name of the new player
	 * @return the new player
	 */
	public Player addNewPlayer(String name) {
		// TODO LOG
		System.out.println("New Player added: " + name);

		Player p = new Player(Player.getNextId(), Role.LOBBY, name, 100, new Vector2f(0, 0));
		p.addItem(new Gun("TestGun", Item.nextId()));
		players.add(p);

		Packet12PlayerSpawned packet12 = new Packet12PlayerSpawned();

		packet12.name = name;
		packet12.playerId = p.getId();
		packet12.role = Role.LOBBY.ordinal();
		packet12.x = 0;
		packet12.y = 0;
		packet12.ready = (byte) (p.isReady() ? 1 : 0);
		broadcastPacket(packet12);
		return p;
	}

	/**
	 * Adds a new client handler to the maintained list
	 * 
	 * @param client
	 *            the new client handler
	 */
	public void addClientHandler(ClientHandler client) {
		clientHandler.add(client);
	}

	/**
	 * Generates a snapshot of the current game
	 * 
	 * @return the generated snapshot
	 */
	public Snapshot generateSnapshot() {
		Snapshot snapshot = new Snapshot();
		Player[] pl = new Player[players.size()];
		players.toArray(pl);

		PlayerSnapshot[] ps = new PlayerSnapshot[pl.length];

		for (int i = 0; i < pl.length; i++) {
			ps[i] = pl[i].generateSnapshot();
		}

		snapshot.time = timeLeft;

		snapshot.player = ps;

		return snapshot;
	}

	/**
	 * Broadcasts a packet to all client handlers
	 * 
	 * @param packet
	 *            the packet to broadcast
	 */
	public void broadcastPacket(Packet packet) {
		for (ClientHandler c : new ArrayList<>(clientHandler)) {
			if (c.isLoggedIn()) {
				c.sendToClient(packet);
			}
		}
	}

	/**
	 * Removes a client handler from the maintained list at the next update
	 * 
	 * @param ch
	 *            the client handler to be removed
	 */
	public void removeClientHandler(ClientHandler ch) {
		removeList.add(ch);
	}

	/**
	 * Adds a new dead player to the game.
	 * 
	 * @param dp
	 *            the dead player to add
	 */
	public void addDeadPlayer(DeadPlayer dp) {
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
		packet11.killerId = dp.getMurderer();
		packet11.itemId = dp.getKillingItem();
		packet11.posX = dp.getPos().x;
		packet11.posY = dp.getPos().y;
		packet11.role = dp.getRole().ordinal();
		packet11.name = dp.getName();

		broadcastPacket(packet11);
	}

	/**
	 * Handles the logout of a player
	 * 
	 * @param player
	 *            the player is being logged out
	 */
	public void handlePlayerLogout(Player player) {
		System.out.println("Player " + player.getName() + " logged out.");
		if (players.contains(player)) {
			players.remove(player);
		}
		for (DeadPlayer dp : (new ArrayList<>(deadplayers))) {
			if (dp.getId() == player.getId()) {
				deadplayers.remove(dp);
			}
		}

		Packet05Logout packet05 = new Packet05Logout();
		packet05.playerId = player.getId();
		broadcastPacket(packet05);

	}

	/**
	 * @return the time left until the is ended
	 */
	public int getTimeLeft() {
		return timeLeft;
	}

	/**
	 * Searches for a free spawn position and returns it
	 * 
	 * @return a position where a player could be spawned or null if there is
	 *         none
	 */
	public Vector2f getFreeSpawnPosition() {
		Vector2f v = null;
		boolean found = false;

		while (!found) {
			double i = Math.random();

			if (i <= 0.2 && !blockedSpawnPositions[0]) {
				v = new Vector2f(0, 0);
				blockedSpawnPositions[0] = true;
				found = true;
			} else if (i > 0.2 && i <= 0.4 && !blockedSpawnPositions[1]) {
				v = new Vector2f(1248, 0);
				blockedSpawnPositions[1] = true;
				found = true;
			} else if (i > 0.4 && i <= 0.6 && !blockedSpawnPositions[2]) {
				v = new Vector2f(0, 928);
				blockedSpawnPositions[2] = true;
				found = true;
			} else if (i > 0.6 && i <= 0.8 && !blockedSpawnPositions[3]) {
				v = new Vector2f(1248, 928);
				blockedSpawnPositions[3] = true;
				found = true;
			} else if (i > 0.8 && i <= 1.0 && !blockedSpawnPositions[4]) {
				v = new Vector2f(640, 512);
				blockedSpawnPositions[4] = true;
				found = true;
			}
		}

		return v;
	}

	/**
	 * @return a list of all players
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Creates SpawnPackets for all players and returns an array of them.
	 * 
	 * @return an array of Packet12PlayerSpawned packets for all players
	 */
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
			packet.ready = (byte) (p.isReady() ? 1 : 0);
			packets[i++] = packet;
		}

		return packets;
	}

	/**
	 * @return the current map
	 */
	public Map getMap() {
		return map;
	}

}
