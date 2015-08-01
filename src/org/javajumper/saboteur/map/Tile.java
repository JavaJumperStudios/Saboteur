package org.javajumper.saboteur.map;

import org.newdawn.slick.Image;

public class Tile {

    private Image image;
    private boolean solid;

    public Tile(Image image, boolean solid) {

	this.image = image;
	this.solid = solid;

    }

    public void draw() {

    }

}
