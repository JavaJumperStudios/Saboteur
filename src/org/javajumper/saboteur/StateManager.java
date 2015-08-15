package org.javajumper.saboteur;

import org.newdawn.slick.state.StateBasedGame;

public class StateManager {

    private static StateBasedGame game;

    public static void init(StateBasedGame game) {
	StateManager.game = game;
    }

    public void changeState(int id) {
	game.enterState(id);
    }

}
