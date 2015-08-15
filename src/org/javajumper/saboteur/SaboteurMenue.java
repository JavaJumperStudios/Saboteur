package org.javajumper.saboteur;

import java.awt.Font;

import org.javajumper.saboteur.gui.Button;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SaboteurMenue extends BasicGameState {

    private Button connectButton;
    private Button exitButton;
    private TextField ipTextField;
    private TextField pwTextField;
    private Image background;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
	background = RessourceManager.loadImage("night.jpg");
	
	connectButton = new Button(this, container, RessourceManager.loadImage("button.png"), 64, 128);
	connectButton.setText("Verbinden");
	connectButton.setMouseDownImage(RessourceManager.loadImage("buttonShadow.png"));
	
	exitButton = new Button(this, container, RessourceManager.loadImage("button.png"), 1024, 512);
	exitButton.setText("Spiel beenden");
	connectButton.setMouseDownImage(RessourceManager.loadImage("buttonShadow.png"));
	
	ipTextField = new TextField(container, new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), false), 64, 64, 300, 25);
	ipTextField.setTextColor(Color.black);
	ipTextField.setBackgroundColor(Color.white);
	ipTextField.setText("localhost");
	
	pwTextField = new TextField(container, new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), false), 64, 96, 300, 25);
	pwTextField.setTextColor(Color.black);
	pwTextField.setBackgroundColor(Color.white);
	pwTextField.setText("");
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

    public void connect() {
	// TODO
    }

    @Override
    public int getID() {
	return 0;
    }

}
