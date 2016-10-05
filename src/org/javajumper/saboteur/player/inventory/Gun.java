package org.javajumper.saboteur.player.inventory;

import java.util.ArrayList;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteurengine.item.Item;
import org.javajumper.saboteurengine.player.Player;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * A gun ingame item
 */
public class Gun extends Item {

	/**
	 * Creates a new gun
	 * 
	 * @param name
	 *            the name of the gun item
	 * @param id
	 *            the item id
	 */
	public Gun(String name, int id) {
		super(name, id, 1);
	}

	@Override
	public void use(Player p) {

		Rectangle rec = new Rectangle(p.getPos().x + 16, p.getPos().y + 16, 0.1f, 0.1f);
		Vector2f startLocation = p.getPos().copy();
		startLocation.add(new Vector2f(16, 16));
		Vector2f v = new Vector2f(p.getLookAngle()).normalise();

		boolean t = true;

		ArrayList<Shape> a = SaboteurServer.instance.getMap().getCollisionShapes();
		ArrayList<Player> players = SaboteurServer.instance.getPlayers();

		while (t) {

			rec.setLocation(startLocation.add(v));

			for (Shape r : a) {
				if (r.intersects(rec)) {
					t = false;
					System.out.println("Punkt kollidiert mit Block " + r.getX() + "  " + r.getY());
					break;
				}
			}

			for (Player player : players) {
				if (rec.intersects(player.getCollisionBox())) {
					if (player.getId() != p.getId()) {
						t = false;
						System.out.println("Punkt kollidiert mit Spieler " + player.getId());
						player.damage(10, player, 0);
						break;
					}
				}
			}

			if (rec.getX() < 0 || rec.getX() > 1280 || rec.getY() < 0 || rec.getY() > 1024)
				t = false;

		}

	}

}
