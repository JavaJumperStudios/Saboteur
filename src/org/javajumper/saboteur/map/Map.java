package org.javajumper.saboteur.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public class Map {
    
    private static ArrayList<Shape> tileCollision = new ArrayList<>();
    private static ArrayList<Line> collisionLines = new ArrayList<>();

    protected Tile[][] tiles = new Tile[40][30];
    protected int width;
    protected int height;

    public Map() {

	width = 40;
	height = 30;

    }
    
    public static ArrayList<Shape> getTileCollision() {
	return tileCollision;
    }
    
    public static ArrayList<Line> getCollisionLines() {
	return collisionLines;
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

	    while ((line = bf.readLine()) != null) {
		Polygon collisionElement = new Polygon();
		String[] elements = line.split(" ");

		for (String element : elements) {
		    String[] coords = element.split(";");
		    assert coords.length == 2;
		    int x = Integer.parseInt(coords[0]);
		    int y = Integer.parseInt(coords[1]);

		    collisionElement.addPoint(x, y);
		}
		
		tileCollision.add(collisionElement);

	    }
	    Line l;
	    for (Shape s : tileCollision) {
		for (int i = 1; i < s.getPointCount(); i++) {

		    l = new Line(s.getPoint(i - 1)[0], s.getPoint(i - 1)[1], s.getPoint(i)[0], s.getPoint(i)[1]);
		    collisionLines.add(l);
		}

		collisionLines.add(new Line(s.getPoint(0)[0], s.getPoint(0)[1], s.getPoint(s.getPointCount() - 1)[0], s.getPoint(s.getPointCount() - 1)[1]));
		collisionLines.add(new Line(0, 0, 1280, 0));
		collisionLines.add(new Line(1280, 0, 1280, 928));
		collisionLines.add(new Line(0, 0, 0, 928));
		collisionLines.add(new Line(0, 928, 1280, 928));
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
