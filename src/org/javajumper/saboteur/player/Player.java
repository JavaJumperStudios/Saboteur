package org.javajumper.saboteur.player;

import java.util.ArrayList;

import org.javajumper.saboteur.map.MapServer;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.player.inventory.Item;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
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
    protected float lookAngle;
    private boolean sprinting;
    private boolean dead;
    private Rectangle collisionBox;

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

	collisionBox = new Rectangle(pos.x, pos.y, 32, 32);

    }

    public void update(int delta) {

	boolean c = false;

	ArrayList<Rectangle> t = MapServer.getTileCollision();

	pos.x = pos.x + move.x * delta / 5f;
	collisionBox.setLocation(pos);

	for (Rectangle r : t) {

	    if (collision(r)) {
		pos.x = pos.x - move.x * delta / 5f;
		collisionBox.setLocation(pos);
		break;
	    }

	}

	pos.y = pos.y + move.y * delta / 5f;
	collisionBox.setLocation(pos);

	for (Rectangle r : t) {

	    if (collision(r)) {
		pos.y = pos.y - move.y * delta / 5f;
		collisionBox.setLocation(pos);
		break;
	    }

	}

	if (pos.x < 0)
	    pos.x = 0;
	if (pos.x > 1248)
	    pos.x = 1248;
	if (pos.y < 0)
	    pos.y = 0;
	if (pos.y > 928)
	    pos.y = 928;

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

    public void setAngle(float angle) {
	this.lookAngle = angle;
    }

    public Item[] getInventory() {
	return inventory;
    }

    public Boolean getDead() {
	return dead;
    }

    public boolean collision(Shape shape) {
	return collisionBox.intersects(shape);
    }

    public PlayerSnapshot generateSnapshot() {
	PlayerSnapshot ps = new PlayerSnapshot();
	ps.playerId = id;
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
