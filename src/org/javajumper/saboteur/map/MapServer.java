package org.javajumper.saboteur.map;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public class MapServer extends Map {

    private static ArrayList<Rectangle> TileCollision = new ArrayList();

    @Override
    public void loadMap() {

	boolean r = false;

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		if (Math.random() < 0.01) {
		    r = true;
		} else
		    r = false;

		tiles[i][j] = new Tile(1, r);

		if (r) {

		    TileCollision.add(new Rectangle(i * 32, j * 32, 32, 32));

		}

	    }

	}

    }

    public static ArrayList<Rectangle> getTileCollision() {
	return TileCollision;
    }

}
