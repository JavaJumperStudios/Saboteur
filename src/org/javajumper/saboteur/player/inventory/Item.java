package org.javajumper.saboteur.player.inventory;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.player.Player;
import org.newdawn.slick.Image;

/**
 * An abstract item class
 */
public abstract class Item {

	private static int currentId = 0;

	private Image image;
	private String name;
	private int id;
	private int typeId; // 0 = Knife, 1 = Gun TODO Enum?
	protected SaboteurServer instance;

	/**
	 * Creates a new item
	 * 
	 * @param name
	 *            the name of the item
	 * @param id
	 *            the item id
	 * @param typeId
	 *            the typeId of the item
	 */
	public Item(String name, int id, int typeId) {
		// this.image = image;
		this.name = name;
		this.id = id;
		this.typeId = typeId;

	}

	/**
	 * Uses the item
	 * 
	 * @param p
	 *            the player who uses the item
	 */
	public abstract void use(Player p);

	/**
	 * @return the typeId of the item (0 = knife, 1 = gun)
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @return the item id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Renders the item to the screen
	 * 
	 * @param x
	 *            the x coordinate to render the item to
	 * @param y
	 *            the y coordinate to render the item to
	 */
	public void draw(float x, float y) {
		image.draw(x, y);
	}

	/**
	 * @return the next free item id
	 */
	public static int nextId() {
		return currentId++;
	}

}
