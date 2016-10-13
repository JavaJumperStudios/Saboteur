package org.javajumper.saboteurengine;

import java.util.Comparator;

import org.javajumper.saboteur.SaboteurGame;
import org.newdawn.slick.geom.Vector2f;

/**
 * Compares the angle of the lines between the player and two given points.
 */
public class ShadowPointComparator implements Comparator<Vector2f> {

	// The origin of the "light" (the player)
	private Vector2f origin;
	
	/**
	 * @param origin the origin of the "light" (the center of the player), will be copied and not changed
	 */
	public ShadowPointComparator(Vector2f origin) {
		// Copying prevents violating the comparison contract
		this.origin = origin.copy();
	}
	
	@Override
	public int compare(Vector2f v1, Vector2f v2) {
		SaboteurGame instance = SaboteurGame.instance;

		return ((int) ((instance.getAngle(origin, v1) - instance.getAngle(origin, v2)) * 100d));
	}

}