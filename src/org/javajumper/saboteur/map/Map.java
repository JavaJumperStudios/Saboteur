package org.javajumper.saboteur.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

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

    /**
     * Lädt eine Karte aus dem "maps" Verzeichnis
     * 
     * @param filename
     *            Der Name der Karte
     */
    public void loadMap(String filename) throws IOException {

	try (FileReader fr = new FileReader("maps/" + filename); BufferedReader bf = new BufferedReader(fr);) {

	    String line;

	    for (int row = 0; row < 30; row++) {
		line = bf.readLine();

		String[] tokens = line.split(" ");

		assert tokens.length == width;

		for (int column = 0; column < 40; column++) {
		    byte type = Byte.parseByte(tokens[column]);
		    boolean solid = false;
		    if (type == 1)
			solid = true;

		    setTile(column, row, new Tile(type, solid));
		}
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
