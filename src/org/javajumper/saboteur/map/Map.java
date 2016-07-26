package org.javajumper.saboteur.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

/**
 * Represents a map of tiles
 */
public class Map {

	private ArrayList<Shape> collisionShapes = new ArrayList<>();
	private ArrayList<Line> collisionLines = new ArrayList<>();

	/**
	 * A two-dimensional array of tiles of the map
	 */
	protected Tile[][] tiles = new Tile[40][30];

	protected int width, height;
	protected String name;

	/**
	 * Creates a new map with a width of 40 and a height of 30
	 */
	public Map() {
		this(40, 30);
	}

	/**
	 * Creates a new map
	 * 
	 * @param width
	 *            the width of the map in tiles
	 * @param height
	 *            the height of the map in tiles
	 */
	public Map(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns all collision shapes not including the map borders as a shape
	 * 
	 * @return a list of all collision shapes of the map
	 */
	public ArrayList<Shape> getCollisionShapes() {
		return collisionShapes;
	}

	/**
	 * @return a list of all lines of the collision-shapes of the map and the
	 *         borders of the map
	 */
	public ArrayList<Line> getCollisionLines() {
		return collisionLines;
	}

	/**
	 * <<<<<<< HEAD Updates the logic of the map based on the time that passed
	 * since the last update ======= Updates the map >>>>>>> branch 'cleanup' of
	 * https://github.com/JavaJumperStudios/Saboteur.git
	 * 
	 * @param delta
	 *            time since last update
	 */
	public void update(int delta) {

	}

	/**
	 * Load a map from the "maps" directory
	 * 
	 * @param filename
	 *            the filename of the map, including the suffix, e.g. room1.map
	 * @throws IOException
	 *             if the file could not be found or an other IOException
	 *             occured
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
			}

			collisionLines.add(new Line(0, 0, 1280, 0));
			collisionLines.add(new Line(1280, 0, 1280, 1024));
			collisionLines.add(new Line(0, 0, 0, 1024));
			collisionLines.add(new Line(0, 1024, 1280, 1024));

		}
	}

	// TODO Further explanation of the "map" array
	/**
	 * Saves the map downloaded from the server to a local file for future use
	 * 
	 * @param mapName//TODO
	 *            further explanation the filename to save to
	 * @param map
	 *            a two-dimensional array of tile information
	 * @param width
	 *            the width of the map to save in tiles
	 * @param height
	 *            the height of the map to save in tiles
	 * @throws IOException
	 *             if an IOException occured while the file is being saved
	 */
	public void saveMap(String mapName, int[][] map, int width, int height) throws IOException {

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

	/**
	 * @return the width of the map
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height of the map
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the name of the map, that is the filename
	 */
	public String getName() {
		return name;
	}

	// TODO add null check
	/**
	 * Returns a tile at a certain position in tiles
	 * 
	 * @param x
	 *            the x coordinate in tiles
	 * @param y
	 *            the y coordinate in tiles
	 * @return the tile at the position
	 */
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	// TODO Add bound check
	/**
	 * Sets a tile at a specified position
	 * 
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x, int y, Tile tile) {
		tiles[x][y] = tile;
	}

	/**
	 * Renders the map
	 * 
	 * @param g
	 *            the Graphics context to render to
	 */
	public void draw(Graphics g) {

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j].draw(g, i * 32, j * 32);
			}
		}

	}

}
