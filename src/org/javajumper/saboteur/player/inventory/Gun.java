package org.javajumper.saboteur.player.inventory;

import java.util.ArrayList;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.map.MapServer;
import org.javajumper.saboteur.player.Player;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Gun extends Item {

    public Gun(String name, int id, int typeId) {
	super(name, id, typeId);
    }

    @Override
    public void use(Player p, SaboteurServer server) {

	Point point = new Point(p.getPos().x + 16, p.getPos().y + 16);
	Vector2f startLocation = p.getPos();
	Vector2f v = new Vector2f(p.getAngle()).normalise();

	boolean t = true;

	ArrayList<Rectangle> a = MapServer.getTileCollision();
	ArrayList<Player> players = server.getPlayers();

	while (t) {

	    point.setLocation(startLocation.add(v));

	    for (Rectangle r : a) {
		if (point.intersects(r)) {
		    t = false;
		    System.out.println("Punkt kollidiert mit Block " + r.getX() + r.getY());
		    break;
		}
	    }

	    for (Player player : players) {
		if (point.intersects(player.collision())) {
		    if (player.getId() != p.getId()) {
			t = false;
			System.out.println("Punkt kollidiert mit Spieler " + player.getId());
			break;
		    }
		}
	    }

	}

    }

}
