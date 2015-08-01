package org.javajumper.saboteur.map;

import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.SPPlayer;

public class Map {

    private Tile[][] tiles;
    private SPPlayer[] players;
    private DeadPlayer[] deadplayers;
    private int width;
    private int hight;

    public void loadMap() {

    }

    public void spawn() {

    }

    public int getWidth() {
	return width;
    }

    public int getHight() {
	return hight;
    }

    public Tile getTile(int x, int y) {
	return tiles[x][y];
    }

    public void setTile(int x, int y, Tile tile) {
	tiles[x][y] = tile;
    }

    private void draw() {

    }

}
