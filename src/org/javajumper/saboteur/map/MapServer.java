package org.javajumper.saboteur.map;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public class MapServer extends Map {

    private static ArrayList<Rectangle> tileCollision = new ArrayList();

    public static ArrayList<Rectangle> getTileCollision() {
	return tileCollision;
    }

    @Override
    public void setTile(int x, int y, Tile tile) {
	super.setTile(x, y, tile);
	
	if(tile.isSolid()) {
	    tileCollision.add(new Rectangle(x * 32, y * 32, 32, 32));
	}
    }
}
