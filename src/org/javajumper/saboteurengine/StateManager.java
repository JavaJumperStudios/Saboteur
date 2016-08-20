package org.javajumper.saboteurengine;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * Manages the game states of a StateBasedGame
 */
public class StateManager {

	private static StateBasedGame game;

	/**
	 * Initializes the StateManager
	 * 
	 * @param instance
	 *            an instance of the StateBasedGame
	 */
	public static void init(StateBasedGame instance) {
		StateManager.game = instance;
	}

	/**
	 * changes the state of the game
	 * 
	 * @param id
	 *            the id of the state to change to
	 */
	public static void changeState(int id) {
		game.enterState(id);
	}

	/**
	 * Changes the state of the game with a transition
	 * 
	 * @param id
	 *            the id of the state to change to
	 * @param fadeOutTransition
	 *            the transition to use to fade out of the current state
	 * @param fadeInTransition
	 *            the transition to use to fade into the next state
	 */
	public static void changeState(int id, FadeOutTransition fadeOutTransition, FadeInTransition fadeInTransition) {
		game.enterState(id, fadeOutTransition, fadeInTransition);
	}

}
