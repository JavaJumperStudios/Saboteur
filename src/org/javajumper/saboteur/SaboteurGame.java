package org.javajumper.saboteur;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import org.javajumper.saboteur.gui.ToggleButton;
import org.javajumper.saboteur.map.Map;
import org.javajumper.saboteur.map.Tile;
import org.javajumper.saboteur.network.ServerListener;
import org.javajumper.saboteur.packet.Packet02Login;
import org.javajumper.saboteur.packet.Packet06UseItem;
import org.javajumper.saboteur.packet.Packet09PlayerUpdate;
import org.javajumper.saboteur.packet.Packet10Ready;
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
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SaboteurGame extends BasicGameState {

    private boolean paused;
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
    private boolean start;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
	map = new Map();

	time = 0;
	start = false;
	stringTimeInSec = "";

	try {
	    map.loadMap("room.map");
	} catch (IOException e) {
	    System.out.println("Karte konnte nicht geladen werden.");
	}

	gui = RessourceManager.loadImage("gui.png");
	background = RessourceManager.loadImage("background.png");
	Tile.initTileRendering();

	setUpConnection("localhost", 5000);

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

	if (start) {
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

	} else {

	    int x = 500;
	    int y = 250;
	    background.draw();
	    for (SPPlayer p : players) {
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

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	if (thePlayer == null)
	    return;

	Input input = container.getInput();

	if (start) {

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

    public void reset() {

    }

    public Map getMap() {
	return map;
    }

    @Override
    public int getID() {
	return 0;
    }

    public void exitGame() {
	System.out.println("I should end the Game now");
    }

    public void setPlayerReadyState(int id, byte ready) {
	System.out.println(ready);
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

    public void addPlayer(SPPlayer p) {
	players.add(p);
    }

    public ArrayList<SPPlayer> getPlayers() {
	return players;
    }

    public void setUpConnection(String server, int port) {
	serverListener = new ServerListener(this, server, port);
	Thread serverListenerThread = new Thread(serverListener);
	serverListenerThread.start();
    }

    public void spawn(SPPlayer p) {

	players.add(p);

    }

    public void spawnDeadPlayer(DeadPlayer dp) {

	deadplayers.add(dp);
    }

    public SPPlayer createPlayerFromLoginPacket(Packet02Login loginPacket) {

	SPPlayer p = new SPPlayer(loginPacket.playerId, Role.LOBBY, loginPacket.name, 100, new Vector2f(0, 0), "Fuzzi.png");

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
