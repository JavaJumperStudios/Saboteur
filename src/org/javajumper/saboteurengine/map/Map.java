package org.javajumper.saboteurengine.map;

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

	protected String name;
	protected Tile[][] tiles = new Tile[40][30];
	protected int width;
	protected int height;

	private ArrayList<Shape> collisionShapes = new ArrayList<>();
	private ArrayList<Line> collisionLines = new ArrayList<>();

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
	 * Updates the logic of the map based on the time that passed since the last
	 * update
	 * 
	 * @param delta
	 *            time since last update
	 */
	public void update(int delta) {

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
					float x = Float.parseFloat(coords[0]);
					float y = Float.parseFloat(coords[1]);

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

	/**
	 * Saves the map downloaded from the server to a local file for future use
	 * 
	 * @param mapName
	 *            the filename to save to
	 * @param map
	 *            a two-dimensional array of tile information. Each element of
	 *            the array represents the typeId of one tile, see
	 *            {@link Tile#typeId}
	 * @param width
	 *            the width of the map to save in tiles
	 * @param height
	 *            the height of the map to save in tiles
	 * @throws IOException
	 *             if an IOException occured while the file is being saved
	 */
	public void saveMap(String mapName, int[][] map, int width, int height, ArrayList<Shape> collisionShapes) throws IOException {

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
		
		for (Shape s : collisionShapes) {
			bw.newLine();
			for (int i = 0; i < s.getPointCount(); i++) {
				bw.write(Float.toString(s.getPoint(i)[0]));
				bw.write(';');
				bw.write(Float.toString(s.getPoint(i)[1]));
				bw.write(' ');
			}
		}

		bw.close();

	}

	/**
	 * @return the name of the map, that is the filename
	 */
	public String getName() {
		return name;
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
	 * Returns a tile at a certain position in tiles
	 * 
	 * @param x
	 *            the x coordinate in tiles
	 * @param y
	 *            the y coordinate in tiles
	 * @return the tile at the position or null if the coordinates are not
	 *         inside of the map
	 */
	public Tile getTile(int x, int y) {
		if (x < width && y < height && x >= 0 && y >= 0)
			return tiles[x][y];
		else {
			// TODO log warning
			return null;
		}
	}

	/**
	 * Sets a tile at a specified position
	 * 
	 * @param x
	 *            the x coordinate of the tile to set. Has to be between 0 and
	 *            width - 1 inclusively
	 * @param y
	 *            the x coordinate of the tile to set. Has to be between 0 and
	 *            height - 1 inclusively
	 * @param tile
	 *            the tile to set
	 */
	public void setTile(int x, int y, Tile tile) {
		if (x >= width || y >= height || x < 0 || y < 0 || tile == null) {
			// TODO log warning
			return;
		}

		tiles[x][y] = tile;
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

}
