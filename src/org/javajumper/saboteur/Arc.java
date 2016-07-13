package org.javajumper.saboteur;

import org.newdawn.slick.geom.Vector2f;

public class Arc {
	public final boolean rightArc;
	public final Vector2f frontPoint;
	public final Vector2f backPoint;
	public final double angle;
	
	public Arc(boolean rightArc, Vector2f frontPoint, Vector2f backPoint, double d) {
		this.rightArc = rightArc;
		this.frontPoint = frontPoint;
		this.backPoint = backPoint;
		this.angle = d;
	}
}
