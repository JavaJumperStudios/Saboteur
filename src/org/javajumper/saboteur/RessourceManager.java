package org.javajumper.saboteur;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Lässt neue Ressourcen dynamisch laden
 */
public class RessourceManager {
	private static HashMap<String, Image> images = new HashMap<>();
	private static HashMap<String, File> files = new HashMap<>();

	private static Image tiles[];

	/**
	 * Lädt ein Bild aus einer Datei oder dem Speicher
	 * 
	 * @param name
	 *            Dateiname der Datei im Ordner "res"
	 * @return das geladene Bild
	 */
	public static Image loadImage(String name) {
		if (images.containsKey(name)) {
			return images.get(name);
		}
		Image newImage;
		try {
			newImage = new Image("res/" + name);
		} catch (Exception e) {
			e.printStackTrace();
			if (!"missingTexture.png".equals(name))
				return loadImage("missingTexture.png");
			// Schutz vor Rekursion falls missingTexture.png fehlt
			return null;
		}

		images.put(name, newImage);

		return newImage;
	}

	/**
	 * Lädt eine XML-Datei aus dem Speicher
	 * 
	 * @param name
	 *            Dateiname der Datei im Ordner "data"
	 * @return das geladene Bild
	 */
	public static File loadXML(String filename) {
		if (files.containsKey(filename)) {
			return files.get(filename);
		}

		File newFile;
		try {
			newFile = new File("data/" + filename);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		files.put(filename, newFile);

		return newFile;
	}

	/**
	 * Lädt ein Bild aus einem Tilesheet aus einer Datei oder dem Speicher
	 * 
	 * @param tilesheetname
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return das geladene Bild
	 */
	public static Image loadImage(String tilesheetname, int x, int y,
			int width, int height) {
		Image tilesheet = loadImage(tilesheetname);
		return tilesheet.getSubImage(x, y, width, height);
	}

	/**
	 * @param filename
	 * @param twidth
	 * @param theight
	 * @throws SlickException
	 */
	public static void loadSprites(String filename, int twidth, int theight)
			throws SlickException {
		Image tileSheet = loadImage(filename);

		int numCol = tileSheet.getWidth() / twidth;
		int numRow = tileSheet.getHeight() / theight;

		tiles = new Image[numCol * numRow];

		int tile = 0;
		for (int row = 0; row < numRow; row++) {
			for (int column = 0; column < numCol; column++) {
				tiles[tile] = tileSheet.getSubImage(column * twidth, row
						* theight, twidth, theight);
				tile++;
			}
		}
	}

	/**
	 * @param index
	 * @return das gewählte Tile
	 */
	public static Image getTile(int index) {
		return tiles[index];
	}

	public static Animation[] loadPlayerFeet() {
		SpriteSheet sprites = new SpriteSheet(loadImage("feet.png"), 64, 64);

		Animation[] model = new Animation[8];

		Image[] frames = new Image[8];
		for (int angle = 0, j = 0; angle < 360; angle += 45, j++) {
			for (int i = 0; i < 8; i++) {
				Image temp = sprites.getSprite(0, i);
				temp.rotate(angle);
				frames[i] = temp;
			}
			model[j] = new Animation(frames, 100);
		}

		return model;
	}

}
