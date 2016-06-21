package org.javajumper.saboteur;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Saboteur extends StateBasedGame {

	public Saboteur() {
		super("Saboteur");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		StateManager.init(this);
		addState(new SaboteurMenue());
		addState(new SaboteurGame());
	}

}
