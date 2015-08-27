package org.javajumper.saboteur;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class StateManager {

    private static StateBasedGame game;

    public static void init(StateBasedGame instance) {
	StateManager.game = instance;
    }

    public static void changeState(int id) {
	game.enterState(id);
    }

    public static void changeState(int id, FadeOutTransition fadeOutTransition, FadeInTransition fadeInTransition) {
	game.enterState(id, fadeOutTransition, fadeInTransition);
    }

}
