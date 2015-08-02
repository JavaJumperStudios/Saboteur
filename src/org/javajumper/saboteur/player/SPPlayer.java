package org.javajumper.saboteur.player;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class SPPlayer extends Player {

    private Image image;

    public SPPlayer(int id, Role role, String name, int livepoints,
	    Vector2f pos, Image image) {
	super(id, role, name, livepoints, pos);
	this.image = image;

    }

    public void draw(float x, float y) {

	image.draw(x, y);
	this.getInventory()[this.getCurrentWeapon()].draw(x, y);

    }

}
