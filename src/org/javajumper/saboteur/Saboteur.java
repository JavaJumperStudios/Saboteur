package org.javajumper.saboteur;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Saboteur extends StateBasedGame {

    public Saboteur() {
	super("Saboteur");
    }
    
    public static void main(String[] args) {
	try {
	    AppGameContainer container = new AppGameContainer(new Saboteur());
	    container.setDisplayMode(1280, 1024, false);
	    container.setMinimumLogicUpdateInterval(25);
	    container.start();
	} catch (SlickException ex) {
	    ex.printStackTrace();
	    System.out.println("Saboteur-Start gescheitert.");
	}
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
	addState(new SaboteurGame());
    }

}
