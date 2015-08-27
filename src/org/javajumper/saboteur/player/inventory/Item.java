package org.javajumper.saboteur.player.inventory;

import org.javajumper.saboteur.SaboteurServer;
import org.javajumper.saboteur.player.Player;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public abstract class Item {
    
    private static int currentId = 0;

    private Image image;
    private String name;
    private int id;
    private int typeId; // 0 = Knife, 1 = Gun
    protected SaboteurServer instance;

    public Item(SaboteurServer instance, String name, int id, int typeId) {
	this.instance = instance;
	//this.image = image;
	this.name = name;
	this.id = id;
	this.typeId = typeId;

    }

    public void use(Player p, SaboteurServer server) {

    }

    public int getTypeId() {
	return typeId;
    }
    
    public int getId() {
	return id;
    }

    public void draw(float x, float y) {
	image.draw(x, y);
    }
    
    public static int nextId() {
	return currentId++;
    }

}
