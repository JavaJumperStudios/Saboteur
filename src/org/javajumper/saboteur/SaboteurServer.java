package org.javajumper.saboteur;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

import org.javajumper.saboteur.network.packet.Packet13Role;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.inventory.Gun;
import org.javajumper.saboteurengine.item.Item;
import org.javajumper.saboteurengine.map.Map;
import org.javajumper.saboteurengine.map.MapServer;
import org.javajumper.saboteurengine.network.ClientAcceptor;
import org.javajumper.saboteurengine.network.ClientHandler;
import org.javajumper.saboteurengine.network.packet.Packet;
import org.javajumper.saboteurengine.network.packet.Packet03StartGame;
import org.javajumper.saboteurengine.network.packet.Packet04EndGame;
import org.javajumper.saboteurengine.network.packet.Packet05Logout;
import org.javajumper.saboteurengine.network.packet.Packet07Snapshot;
import org.javajumper.saboteurengine.network.packet.Packet11SpawnDead;
import org.javajumper.saboteurengine.network.packet.Packet12PlayerSpawned;
import org.javajumper.saboteurengine.network.packet.Packet14Reset;
import org.javajumper.saboteurengine.network.packet.PlayerSnapshot;
import org.javajumper.saboteurengine.network.packet.Snapshot;
import org.javajumper.saboteurengine.player.DeadPlayer;
import org.javajumper.saboteurengine.player.Player;
import org.newdawn.slick.geom.Vector2f;

/**
 * The server class for the saboteur game.
 */
public class SaboteurServer {

	/** The main instance of the server made public for convenience */
	public static SaboteurServer instance;

	/** The main Logger for the server */
	public final static Logger LOGGER = Logger.getLogger(SaboteurServer.class.getName());

	/* Enum for the reason why the game ended */
	/** Time ran out */
	public static final int TIME_RAN_OUT = 0;
	/** No Traitors left, innocent win */
	public static final int NO_TRAITORS_LEFT = 1;
	/** No innocent left, traitors win */
	public static final int NO_INNOCENTS_LEFT = 2;
	/** No players left, server resets */
	public static final int NO_PLAYERS_LEFT = 3;

	private Properties properties;

	/* Gamestate */

	private boolean stop = false;
	private boolean pause = true;
	private int timeLeft;
	private boolean running;
	private String password;

	/* Clients */

	private ArrayList<ClientHandler> clientHandler = new ArrayList<>();
	private ArrayList<ClientHandler> removeList = new ArrayList<>();
	private boolean[] blockedSpawnPositions = new boolean[5];

	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<DeadPlayer> deadplayers = new ArrayList<>();
	private Map map;

	private ClientAcceptor acceptor;

	/**
	 * Initializes the server
	 */
	public void init() {
		loadProperties();

		timeLeft = Integer.parseInt(properties.getProperty("game_duration"));
		password = deleteUnderscores(properties.getProperty("password"));
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

		acceptor = new ClientAcceptor(this);
		new Thread(acceptor).start();
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
		Properties defaults = new Properties();
		defaults.setProperty("game_duration", "600000");
		defaults.setProperty("min_player_count", "2");
		defaults.setProperty("debug_dont_end_game", "false");
		defaults.setProperty("password", "");

		properties = new Properties(defaults);
		FileInputStream fis = null;

		try {
			fis = new FileInputStream("saboteur-server.properties");
			properties.load(fis);
		} catch (IOException e) {
			// Do nothing, we have default fallbacks
		} finally {
			try {
				fis.close();
			} catch (IOException | NullPointerException e) {}
		}
	}

	/**
	 * Starts the server
	 */
	public void start() {
		int delta;
		long lastTimeMillis = System.currentTimeMillis();

		new Thread(new Runnable() {

			@Override
			public void run() {
				Scanner scan = new Scanner(System.in);
				String input;
				System.out.println("starting");
				while (true) {
					input = scan.nextLine();

					if ("stop".equals(input)) {
						break;
					}

					if ("printsettings".equals(input)) {
						System.out.println(instance.properties.toString());
					}

					// Commands ...
				}

				scan.close();
				SaboteurServer.instance.stop();

			}

		}).start();

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

		LOGGER.info("Server has been stopped. Shutting down...");
		acceptor.shutDown();
		for (ClientHandler ch : clientHandler) {
			ch.close();
		}
	}

	private void stop() {
		stop = true;
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

			checkReadyStates();

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
	 * Checks if all players are ready and starts the game if so
	 */
	public void checkReadyStates() {
		if (players.size() < getMinPlayerCount())
			return;

		boolean allReady = players.stream().allMatch((Player p) -> p.isReady());

		if (allReady) {
			Packet03StartGame packet03 = new Packet03StartGame();
			broadcastPacket(packet03);
			running = true;
			unpause();
			initGameStats();
			System.out.println("Alle bereit");
		}
	}

	/**
	 * Checks if win conditions are met and ends the game if so
	 */
	public void checkWinConditions() {

		if (!running | Boolean.parseBoolean(properties.getProperty("debug_dont_end_game")))
			return;

		if (timeLeft <= 0) {
			// Innocents gewinnen durch Zeit

			sendEndPacket(SaboteurServer.TIME_RAN_OUT);

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

			if (!innoAlive && !traitorAlive) {
				// Unentschieden
				sendEndPacket(SaboteurServer.NO_PLAYERS_LEFT);
			} else if (innoAlive && !traitorAlive) {
				// Inno gewonnen
				sendEndPacket(SaboteurServer.NO_TRAITORS_LEFT);
			} else if (!innoAlive && traitorAlive) {
				// Traitor gewonnen
				sendEndPacket(SaboteurServer.NO_INNOCENTS_LEFT);
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

		timeLeft = getGameDuration();
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

		checkReadyStates();

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
	 * @param playerId
	 *            the id of the player who is being logged out
	 */
	public void handlePlayerLogout(int playerId) {
		handlePlayerLogout(getPlayerById(playerId));
	}

	/**
	 * Handles the logout of a player and closes the connection to the client
	 * 
	 * @param player
	 *            the player who is being logged out
	 */
	public void handlePlayerLogout(Player player) {
		LOGGER.info("Player " + player.getName() + " logged out.");

		if (players.contains(player)) {
			players.remove(player);
		}
		for (DeadPlayer dp : (new ArrayList<>(deadplayers))) {
			if (dp.getId() == player.getId()) {
				deadplayers.remove(dp);
			}
		}

		for (ClientHandler ch : clientHandler) {
			if (ch.isLoggedIn() && player.equals(ch.getPlayer())) {
				ch.close();
				removeClientHandler(ch);
				break;
			}
		}

		Packet05Logout packet05 = new Packet05Logout();
		packet05.playerId = player.getId();
		broadcastPacket(packet05);

		checkWinConditions();
		checkReadyStates();
	}

	/**
	 * @return the minimum player count to start a round
	 */
	public int getMinPlayerCount() {
		return Integer.parseInt(properties.getProperty("min_player_count"));
	}

	/**
	 * @return the duration of one round
	 */
	public int getGameDuration() {
		return Integer.parseInt(properties.getProperty("game_duration"));
	}

	/**
	 * Return a player based on his id
	 * 
	 * @param playerId
	 *            the id of the player
	 * @return the player with the given id or null if no player has that id
	 */
	public Player getPlayerById(int playerId) {
		for (Player p : getPlayers()) {
			if (p.getId() == playerId)
				return p;
		}
		return null;
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

	/**
	 * checks if a new player is allowed to connect to the server
	 * 
	 * @return true if the player is allowed to connect, false otherwise
	 */
	public boolean confirmNewLogin(String password) {
		// TODO: Add other Conditions like Playerslots etc.

		if (!this.password.equals(deleteUnderscores(password)))
			return false;
		return true;
	}

	/**
	 * Deletes all underscores ('_') from a given text
	 * 
	 * @param text
	 * @return the given text without underscores
	 */
	public String deleteUnderscores(String text) {
		return text.replaceAll("_", "");
	}

}
