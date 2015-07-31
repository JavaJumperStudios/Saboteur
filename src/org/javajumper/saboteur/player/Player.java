package org.javajumper.saboteur.player;

import org.javajumper.saboteur.player.inventory.Item;
import org.newdawn.slick.geom.Vector2f;

public class Player {

	private int id;
	private Role role;
	private String name;
	private int livepoints;
	private int currentWeapon;
	private Item[] inventory;
	protected Vector2f pos;
	private Vector2f move;
	private Float lookAngle;
	private boolean sprinting;
	private boolean dead;

	public Player() {

	}

	public void update() {

	}

	public void setName() {

	}

	public String getName() {
		return name;
	}

	public void setRole() {

	}

	public Role getRole() {
		return role;
	}

	public void setLivepoints() {

	}

	public int getLivepoints() {
		return livepoints;
	}

	public void setCurrentWeapon() {

	}

	public int getCurrentWeapon() {
		return currentWeapon;
	}

	public void addItem() {

	}

	public void removeItem() {

	}

	public void setPos() {

	}

	public Vector2f getPos() {
		return pos;
	}

	public void setMove() {

	}

	public Vector2f getMove() {
		return move;
	}

	public void setSprint() {

	}

	public Float getAngle() {
		return lookAngle;
	}

	public void setAngle() {

	}

}
