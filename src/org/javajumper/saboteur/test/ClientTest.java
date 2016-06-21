package org.javajumper.saboteur.test;

import org.javajumper.saboteur.Saboteur;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class ClientTest {

	public static void main(String[] _args) {
		try {
			AppGameContainer container = new AppGameContainer(new Saboteur());
			container.setDisplayMode(1280, 1024, false);
			container.setAlwaysRender(true);
			container.setMinimumLogicUpdateInterval(25);
			container.start();
		} catch (SlickException ex) {
			ex.printStackTrace();
			System.out.println("Saboteur-Start gescheitert.");
		}
	}

}
