package org.javajumper.saboteur;

import java.util.ArrayList;

import org.javajumper.saboteur.gui.ToggleButton;
import org.javajumper.saboteur.map.Map;
import org.javajumper.saboteur.map.Tile;
import org.javajumper.saboteur.network.ServerListener;
import org.javajumper.saboteur.packet.Packet02Login;
import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.SPPlayer;
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
    private int time;
    private Map map;
    private SPPlayer thePlayer;
    private Image gui;

    private ArrayList<SPPlayer> players = new ArrayList<SPPlayer>();
    private ArrayList<DeadPlayer> deadplayers = new ArrayList<DeadPlayer>();

    private boolean[] readyPlayers;
    private ToggleButton ready;
    private Image background;
    private ServerListener serverListener;

    @Override
    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	map = new Map();
	map.loadMap();

	gui = RessourceManager.loadImage("gui.png");
	Tile.initTileRendering();

	setUpConnection("localhost", 5000);

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	map.draw();

	for (SPPlayer p : players) {
	    p.draw(p.getPos().x, p.getPos().y);
	}

	for (DeadPlayer p : deadplayers) {
	    p.draw(p.getPos().x, p.getPos().y);
	}

	gui.draw();
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {

	if (thePlayer == null)
	    return;

	Input input = container.getInput();

	Vector2f move = new Vector2f();

	if (input.isKeyDown(Input.KEY_UP)) {
	    move.x = 1;
	} else if (input.isKeyDown(Input.KEY_DOWN)) {
	    move.x = -1;
	}

	if (input.isKeyDown(Input.KEY_LEFT)) {
	    move.y = 1;
	} else if (input.isKeyDown(Input.KEY_RIGHT)) {
	    move.y = -1;
	}

	if (input.isKeyDown(Input.MOUSE_LEFT_BUTTON)) {
	    // TODO Waffe benutzen
	}

	move = move.normalise();

	thePlayer.setMove(move);

	map.update(delta);

	for (SPPlayer p : players) {
	    p.update(delta);
	}

    }

    public void start() {

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

    public void setMainPlayer(SPPlayer p) {
	thePlayer = p;
    }

    public void addPlayer(SPPlayer p) {
	players.add(p);
    }

    public void setUpConnection(String server, int port) {
	serverListener = new ServerListener(this, server, port);
	Thread serverListenerThread = new Thread(serverListener);
	serverListenerThread.start();
    }

    public void spawn(SPPlayer p) {

	players.add(p);

    }

    public void spawnDeadPlayer(DeadPlayer p) {

	deadplayers.add(p);

    }

    public SPPlayer createPlayerFromLoginPacket(Packet02Login loginPacket) {
	SPPlayer p = new SPPlayer(loginPacket.playerId, Role.LOBBY,
		loginPacket.name, 100, new Vector2f(0, 0), "missingTexture.png");
	return p;
    }
    
}
