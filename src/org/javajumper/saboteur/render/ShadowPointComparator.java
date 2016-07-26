package org.javajumper.saboteur.render;

import java.util.Comparator;

import org.javajumper.saboteur.SaboteurGame;
import org.newdawn.slick.geom.Vector2f;

/**
 * Compares the angle of the lines between the player and two given points.
 */
public class ShadowPointComparator implements Comparator<Vector2f> {

	@Override
	public int compare(Vector2f v1, Vector2f v2) {
		SaboteurGame instance = SaboteurGame.instance;

		return ((int) ((instance.getAngleToPlayer(v1) - instance.getAngleToPlayer(v2)) * 100d));
	}

}