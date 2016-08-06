package org.javajumper.saboteur.test;

import static org.junit.Assert.assertEquals;

import org.javajumper.saboteur.SaboteurGame;
import org.javajumper.saboteur.player.Role;
import org.javajumper.saboteur.player.SPPlayer;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

/**
 * Holds tests for the Saboteur game
 */
public class SaboteurGameTest {

	private SaboteurGame sg;
	private SPPlayer sp;

	/**
	 * Initialize the test
	 */
	@Before
	public void init() {
		sg = new SaboteurGame();
		sp = new SPPlayer(0, Role.INNOCENT, "TestPlayer", 100, new Vector2f(100, 100), "");
		sg.setMainPlayer(sp);
	}

	/**
	 * Tests the getAngleToPlayer() method
	 */
	@Test
	public void testAngleToPlayer() {
		Vector2f p1 = new Vector2f(200, 200);
		Vector2f p2 = new Vector2f(0, 0);
		assertEquals(45, sg.getAngleToPlayer(p1), 0.0001);
		assertEquals(255, sg.getAngleToPlayer(p2), 0.0001);
	}

}
