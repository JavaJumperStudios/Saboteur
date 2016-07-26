package org.javajumper.saboteur.player;

import java.util.ArrayList;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.packet.PlayerSnapshot;
import org.javajumper.saboteur.player.inventory.Item;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Player {

	private static SaboteurServer instance;
	private static int currentId = 0;
	private int id;
	protected Role role;
	private String name;
	protected int lifepoints;
	private int currentWeapon;
	private Item[] inventory;
	protected Vector2f pos;
	private Vector2f move;
	protected float lookAngle;
	private boolean sprinting;
	private boolean dead;
	protected Rectangle collisionBox;
	protected boolean ready;

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
			if (collisionBox.intersects(pl.collision())) {
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
			if (collisionBox.intersects(pl.collision())) {
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
		System.out.println(role);
	}

	public Role getRole() {
		return role;
	}

	public void setReadyState(boolean r) {
		ready = r;
	}

	public boolean isReady() {
		return ready;
	}

	public void setLivepoints(int lifepoints) {
		this.lifepoints = lifepoints;
		if (lifepoints <= 0) {
			this.lifepoints = 0;
		}
	}

	public void damage(int damage, Player playerOfImpact, int i) {
		if (!dead) {
			lifepoints -= damage;
			if (lifepoints <= 0) {
				lifepoints = 0;
				die(playerOfImpact, i);
			}

		}
	}

	public void die(Player p, int i) {
		DeadPlayer dp = new DeadPlayer(id, name, role, SaboteurServer.instance.getTimeLeft(), p.getId(), i, pos);
		dead = true;
		SaboteurServer.instance.deadPlayer(dp);
	}

	public int getLivepoints() {
		return lifepoints;
	}

	public void setCurrentWeapon(int currentWeapon) {
		this.currentWeapon = currentWeapon;
	}

	public void setDead(boolean d) {
		dead = d;
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

	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

	public boolean isSprinting() {
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

	public boolean isDead() {
		return dead;
	}

	public Rectangle collision() {
		return collisionBox;
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
