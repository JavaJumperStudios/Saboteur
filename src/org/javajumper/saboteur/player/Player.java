package org.javajumper.saboteur.player;

import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.player.inventory.Item;
import org.newdawn.slick.geom.Vector2f;

public class Player {

    private static int currentId = 0;
    
    private int id;
    private Role role;
    private String name;
    private int lifepoints;
    private int currentWeapon;
    private Item[] inventory;
    protected Vector2f pos;
    private Vector2f move;
    private float lookAngle;
    private boolean sprinting;
    private boolean dead;

    public Player(int id, Role role, String name, int lifepoints, Vector2f pos) {

	this.id = id;
	this.role = role;
	this.name = name;
	this.lifepoints = lifepoints;
	this.currentWeapon = 0;
	this.inventory = new Item[3];
	this.pos = pos;
	this.move = new Vector2f(0, 0);
	this.lookAngle = 0f;
	this.sprinting = false;
	this.dead = false;

    }

    public void update(int delta) {
	
	pos.x = pos.x + move.x * delta;
	pos.y = pos.y + move.y * delta;

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

    public void setLivepoints(int lifepoints) {
	this.lifepoints = lifepoints;
    }

    public int getLivepoints() {
	return lifepoints;
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

    public float getAngle() {
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

    public PlayerSnapshot generateSnapshot() {
	PlayerSnapshot ps = new PlayerSnapshot();
	ps.currentWeapon = currentWeapon;
	ps.lifepoints = lifepoints;
	ps.lookAngle = lookAngle;
	ps.x = pos.x;
	ps.y = pos.y;
	
	return ps;
    }

    public static int getNextId() {
	return currentId++;
    }

}
