package org.javajumper.saboteur.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.GameState;

/**
 * A custom button class.
 */
public class Button extends MouseOverArea {

	private String text = "";
	private boolean active = true;
	private Runnable action;
	private Sound sound;

	/**
	 * Creates a new Button
	 * 
	 * @param instance
	 *            an instance of the current GameState
	 * @param container
	 *            the GUIContext container
	 * @param image
	 *            the texture for the Button
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param action
	 *            what to do on click
	 */
	public Button(GameState instance, GUIContext container, Image image, int x, int y, Runnable action) {
		super(container, image, x, y);
		this.action = action;
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (active && isMouseOver()) {
			if (sound != null)
				sound.play();
			runAction();
		}
	}

	/**
	 * Runs the action normally performed on click
	 */
	public void runAction() {
		action.run();
	}

	@Override
	public void render(GUIContext gameContainer, Graphics g) {
		super.render(gameContainer, g);
		g.setColor(Color.black);
		g.drawString(text, getX() + 20, getY() + 20);
	}

	/**
	 * Deativates the button
	 */
	public void disable() {
		active = false;
	}

	/**
	 * @param string
	 *            the caption for the button
	 */
	public void setText(String string) {
		this.text = string;
	}

	/**
	 * Sets the sound to play on click
	 * 
	 * @param sound
	 *            the sound to play
	 */
	public void setSound(Sound sound) {
		this.sound = sound;
	}

}
