package org.javajumper.saboteur.player.inventory;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.player.Player;

public class Knife extends Item {

	public Knife(SaboteurServer instance, String name, int id, int typeId) {
		super(instance, name, id, typeId);
	}

	@Override
	public void use(Player p, SaboteurServer server) {

	}

}
