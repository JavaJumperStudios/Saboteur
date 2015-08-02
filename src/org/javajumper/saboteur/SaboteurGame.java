package org.javajumper.saboteur;

import org.javajumper.saboteur.map.Map;
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

    @Override
    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	map = new Map();
	map.loadMap();

	gui = RessourceManager.loadImage("gui.png");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	map.draw();
	gui.draw();
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
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
    }

    public void start() {

    }

    public void reset() {

    }

    @Override
    public int getID() {
	return 0;
    }

}
