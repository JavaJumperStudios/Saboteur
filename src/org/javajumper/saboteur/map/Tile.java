package org.javajumper.saboteur.map;

import java.util.ArrayList;

import org.javajumper.saboteur.RessourceManager;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Represent one Tile on the Map
 */
public class Tile {

	private int typeId;
	private boolean solid;

	/**
	 * Creates a new tile
	 * 
	 * @param typeId
	 *            the type Id of the new tile TODO explanation
	 * @param solid
	 *            if the tile is solid or not
	 */
	public Tile(int typeId, boolean solid) {
		this.typeId = typeId;
		this.solid = solid;
	}

	/**
	 * Renders the tile to the screen
	 * 
	 * @param g
	 *            the Graphics context
	 * @param x
	 *            the x position to render the tile to
	 * @param y
	 *            the y position to render the tile to
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(getTexture(), x, y);
	}

	private Image getTexture() {
		return Tile.typeIdTextures.get(typeId);
	}

	/**
	 * @return the typeId of the tile TODO explain
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @return if the tile is solid or not
	 */
	public boolean isSolid() {
		return solid;
	}

	// Static

	private static ArrayList<Image> typeIdTextures = new ArrayList<>();

	/**
	 * Initializes the tile rendering and loads the tile's ressources from the
	 * RessourceManager
	 */
	public static void initTileRendering() {
		typeIdTextures.add(0, RessourceManager.loadImage("Tile-sheet.png", 0, 0, 32, 32));
		typeIdTextures.add(1, RessourceManager.loadImage("Tile-sheet.png", 64, 0, 32, 32));
	}

}
