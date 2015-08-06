package org.javajumper.saboteur.player;

import org.javajumper.saboteur.RessourceManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class SPPlayer extends Player {

    private Image image;
    private String texture;

    public SPPlayer(int id, Role role, String name, int livepoints,
	    Vector2f pos, String texture) {
	super(id, role, name, livepoints, pos);
	this.texture = texture;

    }

    @Override
    public void update(int delta) {

    }

    public void draw(float x, float y) {
	if (image == null)
	    image = RessourceManager.loadImage(texture);

	image.draw(x, y);

	if (this.getInventory()[this.getCurrentWeapon()] != null)
	    this.getInventory()[this.getCurrentWeapon()].draw(x, y);

    }

}
