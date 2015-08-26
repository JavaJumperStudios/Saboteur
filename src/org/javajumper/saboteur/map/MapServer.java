package org.javajumper.saboteur.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class MapServer extends Map {

    private static ArrayList<Shape> tileCollision = new ArrayList<>();

    public static ArrayList<Shape> getTileCollision() {
	return tileCollision;
    }

    /**
     * Lädt eine Karte aus dem "maps" Verzeichnis
     * 
     * @param filename
     *            Der Name der Karte
     */
    @Override
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
	}
    }
}
