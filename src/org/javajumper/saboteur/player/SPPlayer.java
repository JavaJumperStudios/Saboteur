package org.javajumper.saboteur.player;

import java.awt.Image;

import org.newdawn.slick.geom.Vector2f;

public class SPPlayer extends Player {

    public SPPlayer(int id, Role role, String name, int livepoints,
	    Vector2f pos, Image image) {
	super(id, role, name, livepoints, pos);
	this.image = image;

    }

    private Image image;

    public void draw(int x, int y) {

    }

}
