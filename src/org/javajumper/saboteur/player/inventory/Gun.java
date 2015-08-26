package org.javajumper.saboteur.player.inventory;

import java.util.ArrayList;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.map.MapServer;
import org.javajumper.saboteur.player.Player;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Gun extends Item {

    public Gun(String name, int id, int typeId) {
	super(name, id, typeId);
    }

    @Override
    public void use(Player p, SaboteurServer server) {

	Rectangle rec = new Rectangle(p.getPos().x + 16, p.getPos().y + 16, 0.1f, 0.1f);
	Vector2f startLocation = p.getPos().copy();
	startLocation.add(new Vector2f(16, 16));
	Vector2f v = new Vector2f(p.getAngle()).normalise();

	boolean t = true;

	ArrayList<Shape> a = MapServer.getTileCollision();
	ArrayList<Player> players = server.getPlayers();

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
		if (rec.intersects(player.collision())) {
		    if (player.getId() != p.getId()) {
			t = false;
			System.out.println("Punkt kollidiert mit Spieler " + player.getId());
			player.damage(10, player, 0);
			break;
		    }
		}
	    }
	    
	    if(rec.getX() < 0 || rec.getX() > 1280 || rec.getY() < 0 || rec.getY() > 1024) t = false;

	}

    }

}
