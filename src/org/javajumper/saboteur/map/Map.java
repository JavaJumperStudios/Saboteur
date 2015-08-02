package org.javajumper.saboteur.map;

import java.util.ArrayList;

import org.javajumper.saboteur.RessourceManager;
import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.SPPlayer;

public class Map {

    private Tile[][] tiles;
    private ArrayList<SPPlayer> players = new ArrayList<SPPlayer>();
    private ArrayList<DeadPlayer> deadplayers = new ArrayList<DeadPlayer>();
    private int width;
    private int hight;

    public void loadMap() {

	for (int i = 0; i <= hight; i++) {
	    for (int j = 0; j <= width; j++) {

		tiles[i][j] = new Tile(RessourceManager.loadImage(
			"Tile-sheet.png", 0, 0, 32, 32), false);
	    }

	}

    }

    public void spawn(SPPlayer p) {

	players.add(p);

    }

    public void spawnDeadPlayer(DeadPlayer p) {

	deadplayers.add(p);

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

	for (int i = 0; i <= hight; i++) {
	    for (int j = 0; j <= width; j++) {
		tiles[i][j].draw(i, j);
	    }
	}

	for (SPPlayer p : players) {
	    p.draw(p.getPos().x, p.getPos().y);
	}

	for (DeadPlayer p : deadplayers) {
	    p.draw(p.getPos().x, p.getPos().y);
	}

    }

}
