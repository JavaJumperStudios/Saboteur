package org.javajumper.saboteur.player.inventory;

import java.awt.Image;

public class Item {

    private Image image;
    private String name;
    private int id;
    private int typeId;

    public Item(Image image, String name, int id, int typeId) {

	this.image = image;
	this.name = name;
	this.id = id;
	this.typeId = typeId;

    }

    public void use() {

    }

    public int getTypeId() {
	return typeId;
    }

}
