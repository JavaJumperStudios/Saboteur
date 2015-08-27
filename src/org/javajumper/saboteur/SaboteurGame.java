package org.javajumper.saboteur;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.javajumper.saboteur.gui.ToggleButton;
import org.javajumper.saboteur.map.Map;
import org.javajumper.saboteur.map.Tile;
import org.javajumper.saboteur.network.ServerListener;
import org.javajumper.saboteur.packet.Packet02Login;
import org.javajumper.saboteur.packet.Packet06UseItem;
import org.javajumper.saboteur.packet.Packet09PlayerUpdate;
import org.javajumper.saboteur.packet.Packet10Ready;
import org.javajumper.saboteur.packet.Packet14Reset;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.packet.Snapshot;
import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.Player;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.SPPlayer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SaboteurGame extends BasicGameState {

    public static SaboteurGame instance;

    private boolean paused;
    private boolean start;
    private boolean stop;
    private Map map;
    private SPPlayer thePlayer;
    private Image gui;

    private ArrayList<SPPlayer> players = new ArrayList<>();
    private ArrayList<DeadPlayer> deadplayers = new ArrayList<>();

    private boolean ready = false;

    private ToggleButton readyButton;
    private Image background;
    private ServerListener serverListener;
    private int time;
    private String stringTimeInSec;
    private int endCause;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
	instance = this;

	map = new Map();

	time = 0;
	start = false;
	stringTimeInSec = "";
	endCause = 0;
	stop = false;

	try {
	    map.loadMap("room.map");
	} catch (IOException e) {
	    System.out.println("Karte konnte nicht geladen werden.");
	}

	gui = RessourceManager.loadImage("gui.png");
	background = RessourceManager.loadImage("background.png");
	Tile.initTileRendering();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

	if (start && !stop) {
	    map.draw();

	    for (SPPlayer p : players) {
		if (!p.getDead())
		    p.draw(p.getPos().x, p.getPos().y, g);
	    }

	    for (DeadPlayer p : deadplayers) {
		p.draw(p.getPos().x, p.getPos().y);
	    }

	    gui.draw();

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
	    for (SPPlayer p : (ArrayList<SPPlayer>) players.clone()) {
		if (p.ready()) {
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

	    }

	    if (thePlayer.getDead()) {
		for (DeadPlayer dp : deadplayers) {
		    if (dp.getId() == thePlayer.getId()) {
			g.drawString("Du warst ein " + dp.getRole(), 500, 400);
		    }
		}
	    } else {
		g.drawString("Du warst ein " + thePlayer.getRole(), 500, 400);
	    }

	}

	Polygon shadowPoly = new Polygon();
	ArrayList<Vector2f> shadowPolyPoints = new ArrayList<>();
	Vector2f[] pointsOfCollision = new Vector2f[3];
	shadowPoly.addPoint(thePlayer.getPos().x + 16, thePlayer.getPos().y + 16);

	for (Shape s : map.getCollisionShapes()) {

	    float[] shapePoints = s.getPoints();
	    float[] mapCorners = { 0, 0, 0, 960, 1280, 0, 1280, 960 };

	    float[] points = new float[shapePoints.length + mapCorners.length];
	    System.arraycopy(shapePoints, 0, points, 0, shapePoints.length);
	    System.arraycopy(mapCorners, 0, points, shapePoints.length, mapCorners.length);

	    for (int i = 0; i < points.length; i += 2) {

		pointsOfCollision = getCollisionPoint(new Vector2f(points[i], points[i + 1]), g);

//		for (int j = 0; j < 3; j++) {
		    if (pointsOfCollision[0] != null)
			shadowPolyPoints.add(pointsOfCollision[0]);
		    else System.out.println(points[i] + "  " + points[i+1]);
//		}

	    }
	}

	for (int j = shadowPolyPoints.size(); j > 1; j = j - 1) {
	    for (int i = 0; i < j - 1; i++) {
		Vector2f vPlayer = thePlayer.getPos().copy();
		vPlayer = vPlayer.negate();
		Vector2f vRichtung1 = shadowPolyPoints.get(i).copy();
		Vector2f vRichtung2 = shadowPolyPoints.get(i + 1).copy();

		vRichtung1 = vRichtung1.add(vPlayer.copy());
		vRichtung2 = vRichtung2.add(vPlayer.copy());

		if (vRichtung1.getTheta() < vRichtung2.getTheta()) {
		    Vector2f hilfe = shadowPolyPoints.get(i + 1);
		    shadowPolyPoints.remove(hilfe);
		    shadowPolyPoints.add(i, hilfe);
		}
	    }
	}
	int a = 0;
	for (Vector2f v : shadowPolyPoints) {
	    a++;
	    System.out.println("VectorPosition:  " + a + "Winkel:  " + v.getTheta());
	    shadowPoly.addPoint(v.x, v.y);
	}

	g.setColor(new Color(0, 0, 0, 0.5f));
	g.fill(shadowPoly);
	g.setColor(Color.red);
	for (Line l : map.getCollisionLines()) {
	    g.draw(l);
	}

    }

    public Vector2f[] getCollisionPoint(Vector2f v, Graphics g) {

	Line line = null;
	g.setColor(Color.green);

	ArrayList<Vector2f> collisionPoints = new ArrayList<>();
	Vector2f[] thePoint = new Vector2f[3];
	Vector2f vPoint = new Vector2f(v.x, v.y);
//	for (int i = 0; i < 3; i++) {

//	    if (i == 0) {
		line = new Line(new Vector2f(thePlayer.getPos().x + 16, thePlayer.getPos().y + 16), vPoint.copy());
		g.setColor(Color.green);
		thePoint[0] = vPoint;

//	    } else {
//
//		g.setColor(Color.blue);
//		Vector2f hilfsVector = thePlayer.getPos().copy();
//		hilfsVector = hilfsVector.negate();
//		hilfsVector = hilfsVector.add(vPoint.copy());
//		hilfsVector = hilfsVector.normalise();
//		hilfsVector = hilfsVector.scale(2000);
//		if (i == 1)
//		    hilfsVector = hilfsVector.add(1);
//		if (i == 2)
//		    hilfsVector = hilfsVector.add(-1);
//
//		line = new Line(new Vector2f(thePlayer.getPos().x + 16, thePlayer.getPos().y + 16), hilfsVector.copy());
		
		thePoint[0] = null;
//
//	    }
	    g.draw(line);

	    for (Line l : map.getCollisionLines()) {
		Vector2f collisionPoint = l.intersect(line, true);

		if (collisionPoint != null) {
		    collisionPoints.add(collisionPoint);
		    g.setColor(Color.pink);
		    g.draw(new Circle(collisionPoint.x, collisionPoint.y, 5));
		}
	    }

	    
	    for (Vector2f pointOfCollision : collisionPoints) {

		if (thePoint[0] == null || thePlayer.getPos().distance(thePoint[0]) > thePlayer.getPos().distance(pointOfCollision))
		    thePoint[0] = pointOfCollision;

	    }
//	}

	return thePoint;

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	if (thePlayer == null)
	    return;

	Input input = container.getInput();

	if (start && !stop) {

	    int timeInSec = 0;
	    timeInSec = time / 1000;
	    stringTimeInSec = Integer.toString(timeInSec);

	    if (thePlayer.getDead()) {
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
	    thePlayer.setAngle((float) mouse.getTheta());

	    if (input.isKeyPressed(Input.KEY_F9)) {
		Packet10Ready packet10 = new Packet10Ready();
		packet10.playerId = thePlayer.getId();
		packet10.ready = (byte) (thePlayer.ready() ? 0 : 1);
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
	    packet09.lookAngle = thePlayer.getAngle();
	    packet09.moveX = thePlayer.getMove().x;
	    packet09.moveY = thePlayer.getMove().y;
	    packet09.sprinting = (byte) (thePlayer.getSprint() ? 1 : 0);

	    serverListener.sendToServer(packet09);

	} else {

	    if (input.isKeyPressed(Input.KEY_F9)) {
		Packet10Ready packet10 = new Packet10Ready();
		packet10.playerId = thePlayer.getId();
		packet10.ready = (byte) (thePlayer.ready() ? 0 : 1);
		ready = !ready;
		serverListener.sendToServer(packet10);
		System.out.println("I changed ready state to: " + ready);
	    }

	}
    }

    public void start() {
	start = true;
    }

    public Map getMap() {
	return map;
    }

    public void setRole(int playerId, Role role) {
	for (SPPlayer p : players) {
	    if (p.getId() == playerId) {
		p.setRole(role);
	    }
	}
    }

    @Override
    public int getID() {
	return 1;
    }

    public void exitGame() {
	System.out.println("I should end the Game now");
    }

    public void setPlayerReadyState(int id, byte ready) {
	for (SPPlayer p : players) {
	    if (p.getId() == id) {
		if (ready == 0) {
		    p.setReadyState(false);
		} else {
		    p.setReadyState(true);
		}
	    }
	}

    }

    public void setMainPlayer(SPPlayer p) {
	thePlayer = p;
    }

    public void setEndCause(int e) {
	this.endCause = e;
	stop = true;
    }

    public void addPlayer(SPPlayer p) {
	players.add(p);
    }

    public ArrayList<SPPlayer> getPlayers() {
	return players;
    }

    public void setUpConnection(String server, int port) throws UnknownHostException, IOException {
	serverListener = new ServerListener(this, server, port);
	Thread serverListenerThread = new Thread(serverListener);
	serverListenerThread.start();
    }

    public void spawn(SPPlayer p) {

	players.add(p);

    }

    public void handlePlayerLogout(int id) {

	for (SPPlayer p : (ArrayList<SPPlayer>) players.clone()) {
	    if (p.getId() == id) {
		players.remove(p);
	    }
	}

	for (DeadPlayer dp : (ArrayList<DeadPlayer>) deadplayers.clone()) {
	    if (dp.getId() == id) {
		deadplayers.remove(dp);
	    }
	}
    }

    public void spawnDeadPlayer(DeadPlayer dp) {

	deadplayers.add(dp);
    }

    public void reset() {

	start = false;
	stop = false;
	endCause = 0;

	for (SPPlayer p : (ArrayList<SPPlayer>) players.clone()) {

	    p.setDead(false);
	    p.setSprint(false);
	    p.setLivepoints(100);
	    p.setRole(Role.LOBBY);
	    p.setReadyState(false);

	}
	deadplayers.clear();
	System.out.println("ResetClient wurde ausgeführt");
    }

    public SPPlayer createPlayerFromLoginPacket(Packet02Login loginPacket) {

	SPPlayer p = new SPPlayer(loginPacket.playerId, Role.LOBBY, loginPacket.name, 100, new Vector2f(0, 0), "Fuzzi_Neutral.png");

	return p;
    }

    public void setTime(int t) {
	time = t;
    }

    public void setSnapshot(Snapshot snapshot) {
	snapshots: for (PlayerSnapshot ps : snapshot.player) {
	    for (Player p : players) {
		if (ps.playerId == p.getId()) {
		    p.getPos().set(ps.x, ps.y);
		    if (p != thePlayer)
			p.setAngle(ps.lookAngle);
		    p.setLivepoints(ps.lifepoints);
		    p.setCurrentWeapon(ps.currentWeapon);

		    continue snapshots;
		}
	    }
	}
    }

}
