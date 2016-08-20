package org.javajumper.saboteurengine.player;

import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteurengine.RessourceManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * A dead player
 */
public class DeadPlayer {

	private int id;
	private String name;
	private Image image;
	private Role role;
	private int timeOfDeath;
	private int murderer;
	private int killingItem;
	private Vector2f pos;

	/**
	 * Create a new dead player
	 * 
	 * @param id
	 *            the id of the player who died
	 * @param name
	 *            the name of the player who died
	 * @param role
	 *            the role of the player who died
	 * @param timeOfDeath
	 *            when the player died
	 * @param murderer
	 *            the murderer
	 * @param killingItem
	 *            the item that inflicted the killing damage
	 * @param pos
	 *            where the player died
	 */
	public DeadPlayer(int id, String name, Role role, int timeOfDeath, int murderer, int killingItem, Vector2f pos) {

		this.id = id;
		this.name = name;
		this.role = role;
		this.timeOfDeath = timeOfDeath;
		this.murderer = murderer;
		this.killingItem = killingItem;
		this.pos = pos;

	}

	/**
	 * Renders the body to the screen
	 */
	public void draw() {
		draw(pos.x, pos.y);
	}

	/**
	 * Renders the body to the screen
	 * 
	 * @param x
	 *            the x coordinate of this dead player
	 * @param y
	 *            the y coordinate of this dead player
	 */
	public void draw(float x, float y) {

		if (image == null)
			image = RessourceManager.loadImage("Fuzzitot.png");

		image.draw(x, y);

	}

	/**
	 * @return the id of the player who died
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return when this player died
	 */
	public int getTimeOfDeath() {
		return timeOfDeath;
	}

	/**
	 * @return the name of the player who died
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the item which inflicted the killing damage on this player
	 */
	public int getKillingItem() {
		return killingItem;
	}

	/**
	 * @return the murderer of this player
	 */
	public int getMurderer() {
		return murderer;
	}

	/**
	 * @return the role of the player who died
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @return the position of this dead player
	 */
	public Vector2f getPos() {
		return pos;
	}

}
