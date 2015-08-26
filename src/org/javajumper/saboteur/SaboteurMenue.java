package org.javajumper.saboteur;

import java.awt.Font;

import org.javajumper.saboteur.gui.Button;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class SaboteurMenue extends BasicGameState {

    private Button connectButton;
    private Button exitButton;
    private TextField ipTextField;
    private TextField pwTextField;
    private Image background;
    Music openingMenuMusic;
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
	background = RessourceManager.loadImage("night.jpg");

	connectButton = new Button(this, container, RessourceManager.loadImage("button.png"), 64, 128, action -> {
	    try {
		SaboteurGame.instance.setUpConnection(ipTextField.getText(), 5000);
		openingMenuMusic.fade(5000, 0, true);
		StateManager.changeState(1, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.black, 1000));
		//action.disable();
	    } catch (Exception e) {
		System.out.println("Verbindung zum Server konnte nicht hergestellt werden.");
		e.printStackTrace();
	    }
	});
	
	connectButton.setText("Verbinden");
	connectButton.setMouseDownImage(RessourceManager.loadImage("buttonShadow.png"));

	exitButton = new Button(this, container, RessourceManager.loadImage("button.png"), 1024, 512, action -> System.exit(0));
	exitButton.setText("Spiel beenden");
	exitButton.setSound(RessourceManager.loadSound("button.wav"));
	connectButton.setMouseDownImage(RessourceManager.loadImage("buttonShadow.png"));

	ipTextField = new TextField(container, new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), false), 64, 64, 300, 25);
	ipTextField.setTextColor(Color.black);
	ipTextField.setBackgroundColor(Color.white);
	ipTextField.setText("localhost");

	pwTextField = new TextField(container, new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), false), 64, 96, 300, 25);
	pwTextField.setTextColor(Color.black);
	pwTextField.setBackgroundColor(Color.white);
	pwTextField.setText("Passwort (noch nicht genutzt)");
	
	openingMenuMusic = new Music("res/music/Kool Kats.ogg");
	openingMenuMusic.loop();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
	background.draw();
	connectButton.render(container, g);
	exitButton.render(container, g);

	g.setColor(Color.white);
	ipTextField.render(container, g);
	pwTextField.render(container, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    }

    @Override
    public int getID() {
	return 0;
    }

}
