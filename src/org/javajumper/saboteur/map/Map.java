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
    private int hight;
    
    public Map() {
	
	width = 1280;
	hight = 1024;
	
    }
    
    public void update(int delta) {


    }

    public void loadMap() {

	for (int i = 0; i <= hight; i++) {
	    for (int j = 0; j <= width; j++) {

		tiles[i][j] = new Tile(1, false);
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

    public void draw() {

	for (int i = 0; i <= hight; i++) {
	    for (int j = 0; j <= width; j++) {
		tiles[i][j].draw(i, j);
	    }
	}

    }

    public void setSnapshot(Snapshot snapshot) {
	
    }

}
