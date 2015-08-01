package org.javajumper.saboteur.map;

import org.javajumper.saboteur.RessourceManager;
import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.SPPlayer;

import com.sun.beans.WeakCache;

public class Map {

    private Tile[][] tiles;
    private SPPlayer[] players;
    private DeadPlayer[] deadplayers;
    private int width;
    private int hight;

    public void loadMap() {

	for (int i = 0; i <= hight; i++) {
	    for (int j = 0; j <= width; j++) {

		tiles[i][j] = new Tile(
			RessourceManager.loadImage("Tile-sheet.png"), false);
		tiles[i][j].draw(i, j);

	    }

	}

    }

    public void spawn(int id, float x, float y) {

	for (int i = 0; i <= players.length; i++) {
	    if (players[i].getId() == id) {
		if (players[i].getDead() == false) {
		    players[i].draw(x, y);
		} else {
		    spawnDeadPlayer(id, x, y);
		}
	    }
	}

    }

    private void spawnDeadPlayer(int id, float x, float y) {

	for (int i = 0; i <= deadplayers.length; i++) {
	    if (deadplayers[i].getId() == id) {
		deadplayers[i].draw(x, y);
	    }
	}

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

	for (int i = 0; i <= players.length; i++) {
	    if (players[i].getDead() == false) {
		players[i].draw(players[i].getPos().x, players[i].getPos().y);
	    }
	}

	for (int i = 0; i <= deadplayers.length; i++) {
	    deadplayers[i].draw(deadplayers[i].getPos().x,
		    deadplayers[i].getPos().y);
	}

    }

}
