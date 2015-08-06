package org.javajumper.saboteur.map;

import java.util.ArrayList;

import org.javajumper.saboteur.RessourceManager;
import org.javajumper.saboteur.packet.Packet07Snapshot;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.packet.Snapshot;
import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.Player;
import org.javajumper.saboteur.player.SPPlayer;

public class Map {

    private Tile[][] tiles = new Tile[40][30];
    private int width;
    private int height;
    
    public Map() {
	
	width = 40;
	height = 30;
	
    }
    
    public void update(int delta) {


    }

    public void loadMap() {

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {

		tiles[i][j] = new Tile(1, false);
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
		tiles[i][j].draw(i*32, j*32);
	    }
	}

    }

}
