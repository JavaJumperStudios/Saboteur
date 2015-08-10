package org.javajumper.saboteur.map;

import java.util.HashMap;

import org.javajumper.saboteur.RessourceManager;
import org.newdawn.slick.Image;

public class Tile {

    public static HashMap<Integer, Image> typeIdTextures = new HashMap<>();

    private int typeId;
    private boolean solid;

    public Tile(int typeId, boolean solid) {
	this.typeId = typeId;
	this.solid = solid;
    }

    public void draw(int x, int y) {
	getTexture().draw(x, y);
    }

    private Image getTexture() {
	return Tile.typeIdTextures.get(typeId);
    }

    public boolean isSolid() {
	return solid;
    }

    // Static Methods

    public static void initTileRendering() {
	typeIdTextures.put(0, RessourceManager.loadImage("Tile-sheet.png", 0, 0, 32, 32));
	typeIdTextures.put(1, RessourceManager.loadImage("Tile-sheet.png", 64, 0, 32, 32));
    }

}
