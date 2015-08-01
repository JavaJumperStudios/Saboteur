package org.javajumper.saboteur.player;

import org.newdawn.slick.geom.Vector2f;

public class DeadPlayer {

    private int id;
    private String name;
    private Role role;
    private int timeOfDeath;
    private int playerOfImpact;
    private int itemOfImplact;
    private Vector2f pos;

    public DeadPlayer(int id, String name, Role role, int timeOfDeath, int playerOfImpact, int itemOfImpact, Vector2f pos) {

	this.id = id;
	this.name = name;
	this.role = role;
	this.timeOfDeath = timeOfDeath;
	this.playerOfImpact = playerOfImpact;
	this.itemOfImplact = itemOfImpact;
	this.pos = pos;
	
    }

}
