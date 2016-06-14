package org.javajumper.saboteur.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public class Map {

	private ArrayList<Shape> collisionShapes = new ArrayList<>();
	private ArrayList<Line> collisionLines = new ArrayList<>();

	protected Tile[][] tiles = new Tile[40][30];
	protected int width;
	protected int height;
	protected String name;

	public Map() {

		width = 40;
		height = 30;

	}

	public ArrayList<Shape> getCollisionShapes() {
		return collisionShapes;
	}

	public ArrayList<Line> getCollisionLines() {
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

			name = filename;
			String line;

			String init = bf.readLine();
			String[] size = init.split(" ");
			width = Integer.parseInt(size[0]);
			height = Integer.parseInt(size[1]);

			for (int row = 0; row < height; row++) {
				line = bf.readLine();

				String[] tokens = line.split(" ");

				assert tokens.length == width;

				for (int column = 0; column < width; column++) {
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

				collisionShapes.add(collisionElement);

			}
			Line l;
			for (Shape s : collisionShapes) {
				for (int i = 1; i < s.getPointCount(); i++) {

					l = new Line(s.getPoint(i - 1)[0], s.getPoint(i - 1)[1], s.getPoint(i)[0], s.getPoint(i)[1]);
					collisionLines.add(l);
				}

				collisionLines.add(new Line(s.getPoint(0)[0], s.getPoint(0)[1], s.getPoint(s.getPointCount() - 1)[0],
						s.getPoint(s.getPointCount() - 1)[1]));
				collisionLines.add(new Line(0, 0, 1280, 0));
				collisionLines.add(new Line(1280, 0, 1280, 960));
				collisionLines.add(new Line(0, 0, 0, 960));
				collisionLines.add(new Line(0, 960, 1280, 960));
			}

		}
	}

	public void saveMap(String mapName, Integer[][] map, int width, int height) throws IOException {

		FileWriter fw = new FileWriter("maps/" + mapName);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(Integer.toString(width));
		bw.write(' ');
		bw.write(Integer.toString(height));

		for (int i = 0; i < height; i++) {
			bw.newLine();
			for (int j = 0; j < width; j++) {
				bw.write(Integer.toString(map[j][i]));
				bw.write(' ');
			}
		}

		bw.close();

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
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
