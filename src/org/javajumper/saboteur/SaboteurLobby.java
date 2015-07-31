package org.javajumper.saboteur;

import java.awt.Image;

import org.javajumper.saboteur.gui.ToggleButton;
import org.javajumper.saboteur.player.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SaboteurLobby extends BasicGameState {

    private Player[] player;
    private boolean[] readyPlayers;
    private ToggleButton ready;
    private Image background;

    @Override
    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	// TODO Auto-generated method stub

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	// TODO Auto-generated method stub

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
	// TODO Auto-generated method stub

    }

    public void start() {

    }

    @Override
    public int getID() {
	// TODO Auto-generated method stub
	return 0;
    }

}
