package org.javajumper.saboteurengine.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.GameState;

/**
 * A button which can be swiched on and off
 */
public class ToggleButton extends Button {

	/**
	 * Create a new ToggleButton
	 * 
	 * @param instance
	 *            an instance of the current GameState
	 * @param container
	 *            the GUIContext container
	 * @param image
	 *            the texture of the button
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param action
	 *            what to do on click
	 */
	public ToggleButton(GameState instance, GUIContext container, Image image, int x, int y, Runnable action) {
		super(instance, container, image, x, y, action);
	}

}
