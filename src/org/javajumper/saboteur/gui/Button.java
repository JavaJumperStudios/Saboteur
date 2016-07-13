package org.javajumper.saboteur.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.GameState;

public class Button extends MouseOverArea {

	private String text = "";
	private boolean active = true;
	private Runnable action;
	private Sound sound;

	public Button(GameState instance, GUIContext container, Image image, int x, int y,
			Runnable action) {
		super(container, image, x, y);
		this.action = action;
	}

	@Override
	public void render(GUIContext gameContainer, Graphics g) {
		super.render(gameContainer, g);
		g.setColor(Color.black);
		g.drawString(text, getX() + 20, getY() + 20);
	}

	public void setText(String string) {
		this.text = string;
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (active && isMouseOver()) {
			if (sound != null)
				sound.play();
			runAction();
		}
	}

	public void disable() {
		active = false;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public void runAction() {
		action.run();
	}

}
