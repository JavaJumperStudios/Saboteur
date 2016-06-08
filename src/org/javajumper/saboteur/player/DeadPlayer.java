package org.javajumper.saboteur.player;

import org.javajumper.saboteur.RessourceManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class DeadPlayer {

	private int id;
	private String name;
	private Image image;
	private Role role;
	private int timeOfDeath;
	private int playerOfImpact;
	private int itemOfImplact;
	private Vector2f pos;

	public DeadPlayer(int id, String name, Role role, int timeOfDeath, int playerOfImpact, int itemOfImpact,
			Vector2f pos) {

		this.id = id;
		this.name = name;
		this.role = role;
		this.timeOfDeath = timeOfDeath;
		this.playerOfImpact = playerOfImpact;
		this.itemOfImplact = itemOfImpact;
		this.pos = pos;

	}

	public int getId() {
		return id;
	}

	public int getTimeOfDeath() {
		return timeOfDeath;
	}

	public String getName() {
		return name;
	}

	public int getItemOfImpact() {
		return itemOfImplact;
	}

	public int getPlayerOfImpact() {
		return playerOfImpact;
	}

	public Role getRole() {
		return role;
	}

	public void draw(float x, float y) {

		if (image == null)
			image = RessourceManager.loadImage("Fuzzitot.png");

		image.draw(x, y);

	}

	public Vector2f getPos() {
		return pos;
	}

}
