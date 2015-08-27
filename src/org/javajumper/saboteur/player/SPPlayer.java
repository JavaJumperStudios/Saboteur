package org.javajumper.saboteur.player;

import org.javajumper.saboteur.RessourceManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class SPPlayer extends Player {

    private Image image;
    private String texture;

    public SPPlayer(int id, Role role, String name, int livepoints, Vector2f pos, String texture) {
	super(id, role, name, livepoints, pos);

	this.texture = texture;

    }

    @Override
    public void update(int delta) {

    }

    public void draw(float x, float y, Graphics g) {
	
	
	if (image == null || texture == "Fuzzi_Traitor.png" || texture == "Fuzzi_Innocent.png") {
	    image = RessourceManager.loadImage(texture);
	    texture = "Fuzzi_Neutral.png";
	}
	    

	image.rotate(lookAngle - image.getRotation() + 90);
	image.draw(x, y);

	if (this.getInventory()[this.getCurrentWeapon()] != null)
	    this.getInventory()[this.getCurrentWeapon()].draw(x, y);

	g.setColor(Color.black);
	g.drawRect(x, y - 12, 32, 8);
	g.setColor(Color.red);
	g.fillRect(x + 1, y - 11, lifepoints * 0.31f, 7);

	Vector2f v = new Vector2f(x + 16, y + 16);
	Vector2f v1 = new Vector2f(getAngle()).scale(130f);
	Vector2f v2 = v.copy().add(v1);

	g.drawLine(v.x, v.y, v2.x, v2.y);
    }
    
    @Override
    public void setRole(Role role) {
	this.role = role;
	if(role == Role.TRAITOR) this.texture = "Fuzzi_Traitor.png";
	if(role == Role.INNOCENT) this.texture = "Fuzzi_Innocent.png";
	if(role == Role.LOBBY) this.texture = "Fuzzi_Neutral.png";
	System.out.println(this.getId() + "  " + texture);
    }

}
