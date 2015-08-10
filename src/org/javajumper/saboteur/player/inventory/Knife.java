package org.javajumper.saboteur.player.inventory;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.player.Player;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Knife extends Item {

    public Knife(String name, int id, int typeId) {
	super(name, id, typeId);
    }

    @Override
    public void use(Player p, SaboteurServer server) {

    }

}
