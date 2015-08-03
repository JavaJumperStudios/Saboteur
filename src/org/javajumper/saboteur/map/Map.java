package org.javajumper.saboteur.map;

import java.util.ArrayList;

import org.javajumper.saboteur.RessourceManager;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.packet.Snapshot;
import org.javajumper.saboteur.player.DeadPlayer;
import org.javajumper.saboteur.player.Player;
import org.javajumper.saboteur.player.SPPlayer;

public class Map {

    private Tile[][] tiles = new Tile[40][30];
    private ArrayList<SPPlayer> players = new ArrayList<SPPlayer>();
    private ArrayList<DeadPlayer> deadplayers = new ArrayList<DeadPlayer>();
    private int width;
    private int hight;
    
    public void update() {

	for (SPPlayer p : players) {
	    p.update();
	}

	for (DeadPlayer p : deadplayers) {
	    p.update();
	}

    }

    public void loadMap() {

	for (int i = 0; i <= hight; i++) {
	    for (int j = 0; j <= width; j++) {

		tiles[i][j] = new Tile(1, false);
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

    public void draw() {

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

    public Snapshot generateSnapshot() {
	Snapshot snapshot = new Snapshot();
	Player[] pl = (Player[]) players.toArray();
	PlayerSnapshot[] ps = new PlayerSnapshot[pl.length];

	for (int i = 0; i < pl.length; i++) {
	    ps[i] = pl[i].generateSnapshot();
	}
	
	snapshot.player = ps;

	return snapshot;
    }

}
