package org.javajumper.saboteur.player.inventory;

import org.javajumper.saboteurengine.item.Item;
import org.javajumper.saboteurengine.player.Player;

/**
 * A knife ingame item
 */
public class Knife extends Item {

	/**
	 * Create a new knife
	 * 
	 * @param name
	 *            the name of the knife item
	 * @param id
	 *            the item id
	 */
	public Knife(String name, int id) {
		super(name, id, 0);
	}

	@Override
	public void use(Player p) {

	}

}
