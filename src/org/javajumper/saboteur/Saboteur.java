package org.javajumper.saboteur;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The whole saboteur game wrapping the different gamestates
 */
public class Saboteur extends StateBasedGame {

	/**
	 * Create the saboteur game with title "Saboteur"
	 */
	public Saboteur() {
		super("Saboteur");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		StateManager.init(this);
		addState(new SaboteurMenu());
		addState(new SaboteurGame());
	}

}
