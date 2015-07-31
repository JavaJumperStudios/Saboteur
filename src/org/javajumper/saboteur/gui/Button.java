package org.javajumper.saboteur.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.GameState;

public class Button extends MouseOverArea {

    private String text = "";
    private GameState instance;
    private boolean active = true;

    public Button(GameState instance, GUIContext container, Image image, int x,
	    int y) {
	super(container, image, x, y);
	this.instance = instance;
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
	    disable();
	    // Was passieren soll, wenn man den Button drückt
	}
    }

    public void disable() {
	active = false;
    }

}
