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

    public Player(int id, Role role, String name, int livepoints, Vector2f pos) {

	this.id = id;
	this.role = role;
	this.name = name;
	this.livepoints = livepoints;
	this.currentWeapon = 0;
	this.pos = pos;
	this.move = new Vector2f(0, 0);
	this.lookAngle = 0F;
	this.sprinting = false;
	this.dead = false;

    }

    public void update() {

    }
    
    public int getId() {
	return id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public Role getRole() {
	return role;
    }

    public void setLivepoints(int livepoints) {
	this.livepoints = livepoints;
    }

    public int getLivepoints() {
	return livepoints;
    }

    public void setCurrentWeapon(int currentWeapon) {
	this.currentWeapon = currentWeapon;
    }

    public int getCurrentWeapon() {
	return currentWeapon;
    }

    public void addItem(Item item) {

	if (item.getTypeId() == 0) {
	    inventory[1] = item;
	} else if (item.getTypeId() == 1) {
	    inventory[2] = item;
	}

    }

    // Entfernt das ausgerüstete Item aus dem Inventar
    public void removeCurrentItem() {
	inventory[currentWeapon] = null;
    }

    public void setPos(Vector2f pos) {
	this.pos = pos;
    }

    public Vector2f getPos() {
	return pos;
    }

    public void setMove(Vector2f move) {
	this.move = move;
    }

    public Vector2f getMove() {
	return move;
    }

    public void setSprint(boolean sprint) {
	this.sprinting = sprint;
    }
    
    public boolean getSprint() {
	return sprinting;
    }

    public Float getAngle() {
	return lookAngle;
    }

    public void setAngle(Float angle) {
	this.lookAngle = angle;
    }

    public Item[] getInventory() {
	return inventory;
    }
    
    public Boolean getDead() {
	return dead;
    }

}
