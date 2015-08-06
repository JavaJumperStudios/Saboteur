package org.javajumper.saboteur.map;

import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

public class Map {

    protected Tile[][] tiles = new Tile[40][30];
    protected int width;
    protected int height;

    public Map() {

	width = 40;
	height = 30;

    }

    public void update(int delta) {

    }

    public void loadMap() {

	boolean r = false;

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		if (Math.random() < 0.2) {
		    r = true;
		} else
		    r = false;

		tiles[i][j] = new Tile(1, true);

	    }

	}

    }

    public int getWidth() {
	return width;
    }

    public int getHight() {
	return height;
    }

    public Tile getTile(int x, int y) {
	return tiles[x][y];
    }

    public void setTile(int x, int y, Tile tile) {
	tiles[x][y] = tile;
    }

    public void draw() {

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		tiles[i][j].draw(i * 32, j * 32);
	    }
	}

    }

}
