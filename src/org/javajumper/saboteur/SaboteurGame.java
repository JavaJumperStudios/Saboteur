package org.javajumper.saboteur;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.javajumper.saboteur.map.Map;
import org.javajumper.saboteur.map.Tile;
import org.javajumper.saboteur.network.ServerListener;
import org.javajumper.saboteur.packet.Packet06UseItem;
import org.javajumper.saboteur.packet.Packet09PlayerUpdate;
import org.javajumper.saboteur.packet.Packet10Ready;
import org.javajumper.saboteur.packet.Packet14Reset;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.packet.Snapshot;
import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.SPPlayer;
import org.javajumper.saboteur.render.ShadowPointComparator;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The gamestate when the player is in the ready-up, playing or game-over state
 */
public class SaboteurGame extends BasicGameState {

	/**
	 * A public instance of the game
	 */
	public static SaboteurGame instance;

	private boolean start;
	private boolean stop;
	private Map map;
	private SPPlayer thePlayer;
	private Image gui;

	private ArrayList<SPPlayer> players = new ArrayList<>();
	private ArrayList<DeadPlayer> deadplayers = new ArrayList<>();

	private boolean ready = false;

	private Image background;
	private ServerListener serverListener;
	private int timeLeft;
	private String stringTimeInSec;
	private int endCause;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		instance = this;

		map = new Map();

		timeLeft = 0;
		start = false;
		stringTimeInSec = "";
		endCause = 0;
		stop = false;

		gui = RessourceManager.loadImage("gui.png");

		background = RessourceManager.loadImage("background.png");
		Tile.initTileRendering();
	}

	/**
	 * Leaves the ready-up state and starts the game
	 */
	public void start() {
		start = true;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	
		if (thePlayer == null)
			return;
	
		Input input = container.getInput();
	
		if (start && !stop) {
	
			int timeInSec = 0;
			timeInSec = timeLeft / 1000;
			stringTimeInSec = Integer.toString(timeInSec);
	
			if (thePlayer.isDead()) {
				return;
			}
	
			Vector2f move = new Vector2f();
	
			if (input.isKeyDown(Input.KEY_UP)) {
				move.y = -1;
			} else if (input.isKeyDown(Input.KEY_DOWN)) {
				move.y = 1;
			}
	
			if (input.isKeyDown(Input.KEY_LEFT)) {
				move.x = -1;
			} else if (input.isKeyDown(Input.KEY_RIGHT)) {
				move.x = 1;
			}
	
			if (input.isKeyDown(Input.KEY_2)) {
				thePlayer.setCurrentWeapon(2);
			}
	
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				Packet06UseItem packet06 = new Packet06UseItem();
				packet06.itemId = 0;
				serverListener.sendToServer(packet06);
	
			}
	
			Vector2f mouse = new Vector2f(input.getMouseX(), input.getMouseY());
			mouse = mouse.negate();
			mouse.add(new Vector2f(16, 16).add(thePlayer.getPos()));
			mouse = mouse.negate();
			thePlayer.setLookAngle((float) mouse.getTheta());
	
			if (input.isKeyPressed(Input.KEY_R)) {
				Packet10Ready packet10 = new Packet10Ready();
				packet10.playerId = thePlayer.getId();
				packet10.ready = (byte) (thePlayer.isReady() ? 0 : 1);
				ready = !ready;
				serverListener.sendToServer(packet10);
				System.out.println("I changed ready state to: " + ready);
			}
	
			if (input.isKeyPressed(Input.KEY_F10)) {
				Packet14Reset packet14 = new Packet14Reset();
				serverListener.sendToServer(packet14);
			}
	
			move = move.normalise();
	
			thePlayer.setMove(move);
	
			map.update(delta);
	
			for (SPPlayer p : players) {
				p.update(delta);
			}
	
			Packet09PlayerUpdate packet09 = new Packet09PlayerUpdate();
			packet09.currentItem = thePlayer.getCurrentWeapon();
			packet09.lookAngle = thePlayer.getLookAngle();
			packet09.moveX = thePlayer.getMove().x;
			packet09.moveY = thePlayer.getMove().y;
			packet09.sprinting = (byte) (thePlayer.isSprinting() ? 1 : 0);
	
			serverListener.sendToServer(packet09);
	
		} else {
	
			if (input.isKeyPressed(Input.KEY_R)) {
				Packet10Ready packet10 = new Packet10Ready();
				packet10.playerId = thePlayer.getId();
				packet10.ready = (byte) (thePlayer.isReady() ? 0 : 1);
				ready = !ready;
				serverListener.sendToServer(packet10);
				System.out.println("I changed ready state to: " + ready);
			}
	
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		g.clearAlphaMap();
		g.setDrawMode(Graphics.MODE_ALPHA_MAP);
		g.setColor(Color.black);

		renderShadows(g);

		g.setDrawMode(Graphics.MODE_ALPHA_BLEND);

		if (start && !stop) {
			map.draw(g);

			for (SPPlayer p : players) {
				if (!p.isDead())
					p.draw(p.getPos().x, p.getPos().y, g, thePlayer.getRole());
			}

			for (DeadPlayer p : deadplayers) {
				p.draw();
			}

			// TODO draw the gui in Graphics.MODE_NORMAL after
			// gui.draw();

			g.drawString(stringTimeInSec, 1200, 996);

			if (thePlayer.getRole() == Role.TRAITOR)
				g.setColor(Color.red);
			if (thePlayer.getRole() == Role.INNOCENT)
				g.setColor(Color.green);

			g.drawString(thePlayer.getRole().toString(), 200, 985);

		}

		if (!start) {

			int x = 500;
			int y = 250;

			background.draw();

			for (SPPlayer p : (new ArrayList<>(players))) {
				if (p.isReady()) {
					g.setColor(Color.green);
				} else {
					g.setColor(Color.red);
				}
				g.drawString(p.getName() + "    ID:   " + p.getId(), x, y);
				g.fillRect(x + 300, y, 32, 32);
				y += 64;
			}

		}

		if (stop) {

			background.draw();

			switch (endCause) {

			case 0:
				g.setColor(Color.green);
				g.fillRect(300, 250, 700, 256);
				g.setColor(Color.blue);
				g.drawString("Innocent gewinnen, weil die Zeit abgelaufen ist.", 360, 350);
				break;
			case 1:
				g.setColor(Color.green);
				g.fillRect(300, 250, 700, 256);
				g.setColor(Color.blue);
				g.drawString("Innocent gewinnen, weil alle Traitor gestorben sind.", 360, 350);
				break;
			case 2:
				g.setColor(Color.red);
				g.fillRect(300, 250, 700, 256);
				g.setColor(Color.blue);
				g.drawString("Traitor gewinnen, weil alle Innocent gestorben sind.", 360, 350);
				break;
			case 3:
				g.setColor(Color.gray);
				g.fillRect(300, 250, 700, 256);
				g.setColor(Color.blue);
				g.drawString("Der Server wurde manuell RESETTET.", 360, 350);
				break;
			default:
				// TODO
				break;
			}

			if (thePlayer.isDead()) {
				for (DeadPlayer dp : deadplayers) {
					if (dp.getId() == thePlayer.getId()) {
						g.drawString("Du warst ein " + dp.getRole(), 500, 400);
					}
				}
			} else {
				g.drawString("Du warst ein " + thePlayer.getRole(), 500, 400);
			}

		}
	}

	/**
	 * Resets the state of all players and the state of the game and goes back
	 * to the lobby
	 */
	public void reset() {

		start = false;
		stop = false;
		endCause = 0;

		for (SPPlayer p : new ArrayList<>(players)) {

			p.setDead(false);
			p.setSprinting(false);
			p.resetLifepoints();
			p.setRole(Role.LOBBY);
			p.setReadyState(false);

		}
		deadplayers.clear();
		// TODO Log
		System.out.println("ResetClient wurde ausgeführt");
	}

	/**
	 * Exits the game
	 */
	public void exitGame() {
		// TODO implement
		System.out.println("I should end the Game now");
	}

	// TODO make the endcause an enum
	/**
	 * @param e
	 *            the endcause to set
	 */
	public void setEndCause(int e) {
		this.endCause = e;
		stop = true;
	}

	/**
	 * Sets up a connnection to a server
	 * 
	 * @param server
	 *            the server ip
	 * @param port
	 *            the server port
	 * @param password
	 *            the password for the server
	 * @param name
	 *            the name of the client
	 * @throws UnknownHostException
	 *             if the host could not be found
	 * @throws IOException
	 */
	public void setUpConnection(String server, int port, String password, String name)
			throws UnknownHostException, IOException {
		serverListener = new ServerListener(this, server, port, password, name);
		Thread serverListenerThread = new Thread(serverListener);
		serverListenerThread.start();
	}

	/**
	 * Adds a new player to the game
	 * 
	 * @param p
	 *            the player to add
	 */
	public void addPlayer(SPPlayer p) {
		players.add(p);
	}

	/**
	 * Adds a dead player
	 * 
	 * @param dp
	 *            the dead player to add
	 */
	public void spawnDeadPlayer(DeadPlayer dp) {
		deadplayers.add(dp);
	}

	/**
	 * Handles the logout of a player
	 * 
	 * @param id
	 *            the player id
	 */
	public void handlePlayerLogout(int id) {

		for (SPPlayer p : (new ArrayList<>(players))) {
			if (p.getId() == id) {
				players.remove(p);
			}
		}

		for (DeadPlayer dp : (new ArrayList<>(deadplayers))) {
			if (dp.getId() == id) {
				deadplayers.remove(dp);
			}
		}
	}

	/**
	 * Attempts to load a map specified by a name
	 * 
	 * @param mapName
	 *            the filename of the map, including the suffix, e.g. room1.map
	 */
	public void loadMap(String mapName) {
		try {
			map.loadMap(mapName);
		} catch (IOException e) {
			System.out.println("Karte konnte nicht geladen werden.");
		}
	}

	// TODO Further explanation of the "map" array
	/**
	 * Saves the map downloaded from the server to a local file for future use
	 * 
	 * @param mapName
	 *            the filename to save to
	 * @param mapInfo
	 *            a two-dimensional array of tile information
	 * @param width
	 *            the width of the map to save in tiles
	 * @param height
	 *            the height of the map to save in tiles
	 */
	public void saveMap(String mapName, int[][] mapInfo, int width, int height) {
		try {
			map.saveMap(mapName, mapInfo, width, height);
		} catch (IOException e) {
			// TODO log
			System.out.println("Karte konnte nicht gespeichert werden.");
		}
	}

	@Override
	public int getID() {
		return 1;
	}

	/**
	 * @return the currently played map
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * @return a list of all players, including the main player
	 */
	public ArrayList<SPPlayer> getPlayers() {
		return players;
	}

	/**
	 * Calculates the angle from the player-center to a given point
	 * 
	 * @param v
	 *            the given point
	 * @return the angle from the center of the player to the given point
	 */
	public double getAngleToPlayer(Vector2f v) {
		return v.copy().sub(thePlayer.getCenter()).getTheta();
	}

	/**
	 * Casts 3 rays from the player to the given point and returns the 3 points
	 * where the ray collides with an object or null
	 * 
	 * @param g
	 *            the Graphics context
	 * @param vPoint
	 * @return [0] is the ray in the center, [1] is the left ray and [2] is the
	 *         right ray
	 */
	public Vector2f[] getCollisionPoints(Graphics g, Vector2f vPoint) {

		Vector2f[] theCollisionPoints = new Vector2f[3];
		Vector2f playerCenter = thePlayer.getCenter();

		Vector2f destPoint1 = vPoint.copy().add(vPoint.copy().sub(playerCenter));
		Vector2f destPoint2 =
				playerCenter.copy().add(destPoint1.copy().sub(playerCenter).normalise().scale(1700f).add(0.2));
		Vector2f destPoint3 =
				playerCenter.copy().add(destPoint1.copy().sub(playerCenter).normalise().scale(1700f).add(-0.2));

		Line ray1 = new Line(playerCenter, destPoint1);
		Line ray2 = new Line(playerCenter, destPoint2);
		Line ray3 = new Line(playerCenter, destPoint3);

		theCollisionPoints[0] = getCollisionPoint(ray1);
		theCollisionPoints[1] = getCollisionPoint(ray2);
		theCollisionPoints[2] = getCollisionPoint(ray3);

		return theCollisionPoints;

	}

	/**
	 * @param p
	 *            the player who should be the main player
	 */
	public void setMainPlayer(SPPlayer p) {
		thePlayer = p;
	}

	/**
	 * Sets the ready-state of a player
	 * 
	 * @param playerId
	 *            the id of the player
	 * @param ready
	 *            the new ready-state
	 */
	public void setPlayerReadyState(int playerId, boolean ready) {
		for (SPPlayer p : players) {
			if (p.getId() == playerId) {
				p.setReadyState(ready);
			}
		}

	}

	/**
	 * Sets the role of a specified player
	 * 
	 * @param playerId
	 *            the id of the player to set the role
	 * @param role
	 *            the role that the player should get
	 */
	public void setRole(int playerId, Role role) {
		for (SPPlayer p : players) {
			if (p.getId() == playerId) {
				p.setRole(role);
			}
		}
	}

	/**
	 * Sets the game time to a specified value
	 * 
	 * @param time
	 *            the time to set
	 */
	public void setTime(int time) {
		timeLeft = Math.max(0, time);
	}

	/**
	 * Updates the current snapshot. The Snapshot will later be applied to
	 * update the game state.
	 * 
	 * @param snapshot
	 *            the snapshot to apply later
	 */
	public void setSnapshot(Snapshot snapshot) {
		snapshots: for (PlayerSnapshot ps : snapshot.player) {
			for (SPPlayer p : players) {
				if (ps.playerId == p.getId()) {
					p.getPos().set(ps.x, ps.y);
					if (p != thePlayer)
						p.setLookAngle(ps.lookAngle);
					p.updateLifepoints(ps.lifepoints);
					p.setCurrentWeapon(ps.currentWeapon);

					continue snapshots;
				}
			}
		}
	}

	private void renderShadows(Graphics g) {
		ArrayList<Vector2f> points = new ArrayList<>();
	
		ArrayList<Shape> shapes = new ArrayList<>(map.getCollisionShapes());
		shapes.add(new Rectangle(0, 0, background.getWidth(), background.getHeight()));
	
		for (Shape s : shapes) {
			float[] shapePoints = s.getPoints();
	
			for (int i = 0; i < shapePoints.length; i += 2) {
				Vector2f poi = new Vector2f(shapePoints[i], shapePoints[i + 1]);
				Vector2f[] cPoints = getCollisionPoints(g, poi);
	
				points.addAll(Arrays.asList(cPoints));
			}
		}
	
		ShadowPointComparator spComparator = new ShadowPointComparator();
	
		points.sort(spComparator);
	
		points.add(points.get(0));
	
		for (int i = 0; i < points.size() - 1; i++) {
			Polygon p = new Polygon();
			p.addPoint(points.get(i).x, points.get(i).y);
			p.addPoint(points.get(i + 1).x, points.get(i + 1).y);
			p.addPoint(thePlayer.getCenter().x, thePlayer.getCenter().y);
	
			g.fill(p);
		}
	}

	private Vector2f getCollisionPoint(Line ray) {
		ArrayList<Vector2f> collisionPoints = new ArrayList<>();

		for (Line l : map.getCollisionLines()) {
			Vector2f collisionPoint = l.intersect(ray, true);

			if (collisionPoint != null) {
				collisionPoints.add(collisionPoint);
			}
		}

		Vector2f point = ray.getEnd();
		for (Vector2f pointOfCollision : collisionPoints) {

			if (point == null || thePlayer.getPos().distance(point) > thePlayer.getPos().distance(pointOfCollision))
				point = pointOfCollision;

		}

		return point;
	}

}
