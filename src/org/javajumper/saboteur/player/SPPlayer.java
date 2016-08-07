package org.javajumper.saboteur.player;

import org.javajumper.saboteur.RessourceManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * The player representation on the client, extending the server representation
 * by rendering
 */
public class SPPlayer extends Player {

	private Image image;
	private String texture;

	/**
	 * Creates a new player
	 * 
	 * @param id
	 *            the id to assign to the new player
	 * @param role
	 *            the role of the new Player, see
	 *            {@link org.javajumper.saboteur.player.Role Role}
	 * @param name
	 *            the name to assign to the new player
	 * @param lifepoints
	 *            the initial lifepoints of the new player
	 * @param pos
	 *            the position to set the player to
	 * @param texture
	 *            the texture for the new player on this client
	 */
	public SPPlayer(int id, Role role, String name, int lifepoints, Vector2f pos, String texture) {
		super(id, role, name, lifepoints, pos);

		this.texture = texture;

	}

	@Override
	public void update(int delta) {
		// The player logic is updated on the server
	}

	/**
	 * Renders the player to the screen
	 * 
	 * @param x
	 *            the x position to render the player to
	 * @param y
	 *            the y position to render the player to
	 * @param g
	 *            the graphics context
	 * @param viewerRole
	 *            the role of the one viewing this player
	 */
	public void draw(float x, float y, Graphics g, Role viewerRole) {

		if (Role.isRestriced(viewerRole) && texture == "Fuzzi_Traitor.png") {
			image = RessourceManager.loadImage("Fuzzi_Innocent.png");
		} else
			image = RessourceManager.loadImage(texture);

		image.rotate(lookAngle - image.getRotation() + 90);
		image.draw(x, y);

		if (this.getInventory()[this.getCurrentWeapon()] != null)
			this.getInventory()[this.getCurrentWeapon()].draw(x, y);

		g.setColor(Color.black);
		g.drawRect(x, y - 12, 32, 8);
		g.setColor(Color.red);
		g.fillRect(x + 1, y - 11, lifepoints * 0.31f, 7);

		Vector2f v = new Vector2f(x + 16, y + 16);
		Vector2f v1 = new Vector2f(getLookAngle()).scale(130f);
		Vector2f v2 = v.copy().add(v1);

		g.drawLine(v.x, v.y, v2.x, v2.y);
	}

	/**
	 * Updates the lifepoints of the player. Used to update the player
	 * lifepoints to the lifepoints in the snapshot.
	 * 
	 * @param lifepoints
	 *            the new value for the lifepoints of the player
	 */
	public void updateLifepoints(int lifepoints) {
		this.lifepoints = lifepoints;
	}

	/**
	 * @return the position of the center of the player, computed by adding 16
	 *         to the x and y coordinates
	 */
	public Vector2f getCenter() {
		return new Vector2f(pos.x + 16, pos.y + 16);
	}

	@Override
	public void setRole(Role role) {
		super.setRole(role);

		// TODO Filenames
		if (role == Role.TRAITOR)
			this.texture = "Fuzzi_Traitor.png";
		if (role == Role.INNOCENT)
			this.texture = "Fuzzi_Innocent.png";
		if (role == Role.LOBBY)
			this.texture = "Fuzzi_Neutral.png";
	}

}
