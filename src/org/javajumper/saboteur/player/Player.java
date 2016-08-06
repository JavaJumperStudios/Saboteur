package org.javajumper.saboteur.player;

import java.util.ArrayList;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.player.inventory.Item;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * A character on the map.
 */
public class Player {

	/* Static */
	private static int nextPlayerId = 0;

	/* General */
	private int id;
	private String name;

	protected Role role;
	protected boolean ready;

	/* Movement */

	protected Vector2f pos;
	/**
	 * The movement vector is sent to the server with each tick, while the
	 * server is the one who transforms this movement to a new position based on
	 * time passed on a tick. The new position of the player is then transferred
	 * with the next snapshot update.
	 */
	private Vector2f move;
	protected float lookAngle;
	private boolean sprinting;

	protected Rectangle collisionBox;

	/* Attributes */
	protected int lifepoints;
	private boolean dead;

	/* Inventory */
	private int currentWeapon;
	private Item[] inventory;

	/**
	 * Creates a new player
	 * 
	 * @param id
	 *            the id to assign to the new player
	 * @param role
	 *            the role of the new Player, see
	 *            {@link org.javajumper.saboteur.player.Role Role}
	 * @param name
	 *            the name to assign to the new player
	 * @param lifepoints
	 *            the initial lifepoints of the new player
	 * @param pos
	 *            the position to set the player to
	 */
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
		this.ready = false;

		collisionBox = new Rectangle(pos.x, pos.y, 32, 32);

	}

	/**
	 * @return the next free player id to assign
	 */
	public static int getNextId() {
		return nextPlayerId++;
	}

	/**
	 * Updates the logic of the player based on the time passed since the last
	 * update
	 * 
	 * @param delta
	 *            the time passed since the last update
	 */
	public void update(int delta) {

		ArrayList<Shape> t = SaboteurServer.instance.getMap().getCollisionShapes();
		ArrayList<Player> players = SaboteurServer.instance.getPlayers();

		pos.x = pos.x + move.x * delta / 5f;
		collisionBox.setLocation(pos);

		for (Shape r : t) {
			if (collisionBox.intersects(r)) {
				pos.x = pos.x - move.x * delta / 5f;
				collisionBox.setLocation(pos);
				break;
			}

		}

		for (Player pl : players) {
			if (collisionBox.intersects(pl.getCollisionBox())) {
				if (pl.getId() != this.getId()) {
					pos.x = pos.x - move.x * delta / 5f;
					collisionBox.setLocation(pos);
					break;
				}

			}
		}

		pos.y = pos.y + move.y * delta / 5f;
		collisionBox.setLocation(pos);

		for (Shape r : t) {

			if (collisionBox.intersects(r)) {
				pos.y = pos.y - move.y * delta / 5f;
				collisionBox.setLocation(pos);
				break;
			}

		}

		for (Player pl : players) {
			if (collisionBox.intersects(pl.getCollisionBox())) {
				if (pl.getId() != this.getId()) {
					pos.y = pos.y - move.y * delta / 5f;
					collisionBox.setLocation(pos);
					break;
				}
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

	/**
	 * Generates a new PlayerSnapshot for this player, including all snapshot
	 * fields
	 * 
	 * @return the generated snapshot
	 */
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

	/**
	 * Damages the player. If the players life total sinks to zero, he dies.
	 * 
	 * @param damage
	 *            the amount of damage to do to the player
	 * @param inflictor
	 *            the player who caused the damage
	 * @param damagingItemId
	 *            the id of the item which caused the damage
	 */
	public void damage(int damage, Player inflictor, int damagingItemId) {
		if (!dead) {
			lifepoints -= damage;
			if (lifepoints <= 0) {
				lifepoints = 0;
				die(inflictor, damagingItemId);
			}

		}
	}

	/**
	 * @param murderer
	 *            the player who killed this player
	 * @param itemId
	 *            the id of the item used to kill the player
	 */
	public void die(Player murderer, int itemId) {
		DeadPlayer dp =
				new DeadPlayer(id, name, role, SaboteurServer.instance.getTimeLeft(), murderer.getId(), itemId, pos);
		dead = true;
		SaboteurServer.instance.addDeadPlayer(dp);
	}

	/**
	 * Adds an item to the inventory of the player
	 * 
	 * @param item
	 *            the item to add to the inventory
	 */
	public void addItem(Item item) {

		if (item.getTypeId() == 0) {
			inventory[1] = item;
		} else if (item.getTypeId() == 1) {
			inventory[2] = item;
		}

	}

	/**
	 * Removes the currently selected item from the inventory
	 */
	public void removeCurrentItem() {
		inventory[currentWeapon] = null;
	}

	/**
	 * Resets the lifepoints of the player to 100
	 */
	public void resetLifepoints() {
		this.lifepoints = 100;
	}

	/**
	 * @return the current lifepoints of the player
	 */
	public int getLifepoints() {
		return lifepoints;
	}

	/**
	 * @return the slot id of the currently selected weapon
	 */
	public int getCurrentWeapon() {
		return currentWeapon;
	}

	/**
	 * Sets which weapon is currently selected/used.
	 * 
	 * @param currentWeapon
	 *            the slot id of the new currently selected weapon
	 */
	public void setCurrentWeapon(int currentWeapon) {
		this.currentWeapon = currentWeapon;
	}

	/**
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id of the player
	 */
	public int getId() {
		return id;
	}

	/**
	 * Assigns a new role to the player, see
	 * {@link org.javajumper.saboteur.player.Role Role}
	 * 
	 * @param role
	 *            the new role to assign to the player
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the role of the player
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * See {@link #move movement vector}
	 * 
	 * @return the current movement vector
	 */
	public Vector2f getMove() {
		return move;
	}

	/**
	 * See {@link #move movement vector}
	 * 
	 * @param m
	 *            the new movement vector for the player
	 */
	public void setMove(Vector2f m) {
		this.move = m;
	}

	/**
	 * @return the players current inventory
	 */
	public Item[] getInventory() {
		return inventory;
	}

	/**
	 * @return the collision box of the player
	 */
	public Rectangle getCollisionBox() {
		return collisionBox;
	}

	/**
	 * @param d
	 *            the new death state for this player
	 */
	public void setDead(boolean d) {
		dead = d;
	}

	/**
	 * @return if the player is dead
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * @param ready
	 *            true, if the player should be set into ready state and false
	 *            if he should be set to the state of not being ready
	 */
	public void setReadyState(boolean ready) {
		this.ready = ready;
	}

	/**
	 * @return if the player is ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @return the position of the player
	 */
	public Vector2f getPos() {
		return pos;
	}

	/**
	 * @param pos
	 *            the new position for the player
	 */
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	/**
	 * @return if the player is sprinting
	 */
	public boolean isSprinting() {
		return sprinting;
	}

	/**
	 * @param sprinting
	 *            the new sprinting state for the player
	 */
	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

	/**
	 * @return the looking direction of the player
	 */
	public float getLookAngle() {
		return lookAngle;
	}

	/**
	 * Sets the direction the player is looking
	 * 
	 * @param angle
	 *            the looking direction for the player to look to
	 */
	public void setLookAngle(float angle) {
		this.lookAngle = angle;
	}

}
