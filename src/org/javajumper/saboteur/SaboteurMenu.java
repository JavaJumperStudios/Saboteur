package org.javajumper.saboteur;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

/**
 * The menu gamestate
 */
public class SaboteurMenu extends BasicGameState {

	private Button connectButton;
	private Button exitButton;
	private TextField ipTextField;
	private TextField pwTextField;
	private TextField nameTextField;
	private Image background;
	Music openingMenuMusic;

	// True if music shall be played
	private boolean playMusic;

	// True if the menu shall be skipped (not implemented)
	private boolean skipMenu;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		loadProperties();

		// background = RessourceManager.loadImage("night.jpg");

		connectButton = new Button(this, container, RessourceManager.loadImage("button.png"), 64, 160, () -> {
			try {
				SaboteurGame.instance.setUpConnection(ipTextField.getText(), 5000, pwTextField.getText(),
						nameTextField.getText());
				if (playMusic)
					openingMenuMusic.fade(5000, 0, true);
				StateManager.changeState(1, new FadeOutTransition(Color.black, 1000),
						new FadeInTransition(Color.black, 1000));
			} catch (Exception e) {
				System.out.println("Verbindung zum Server konnte nicht hergestellt werden.");
				e.printStackTrace();
			}
		});

		connectButton.setText("Verbinden");
		connectButton.setMouseDownImage(RessourceManager.loadImage("buttonShadow.png"));

		exitButton =
				new Button(this, container, RessourceManager.loadImage("button.png"), 1024, 512, () -> System.exit(0));
		exitButton.setText("Spiel beenden");
		exitButton.setSound(RessourceManager.loadSound("button.wav"));

		ipTextField =
				new TextField(container, new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), false), 64, 64, 300, 25);
		ipTextField.setTextColor(Color.black);
		ipTextField.setBackgroundColor(Color.white);
		ipTextField.setText("localhost");

		pwTextField =
				new TextField(container, new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), false), 64, 96, 300, 25);
		pwTextField.setTextColor(Color.black);
		pwTextField.setBackgroundColor(Color.white);
		pwTextField.setText("Passwort (noch nicht genutzt)"); // Ungenutzt
		pwTextField.deactivate();

		nameTextField =
				new TextField(container, new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), false), 64, 128, 300, 25);
		nameTextField.setTextColor(Color.black);
		nameTextField.setBackgroundColor(Color.white);
		nameTextField.setText("");
		nameTextField.setMaxLength(10);

		if (playMusic) {
			openingMenuMusic = new Music("res/music/Kool Kats.ogg");
			openingMenuMusic.loop();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// background.draw();
		connectButton.render(container, g);
		exitButton.render(container, g);
		g.setColor(Color.white);
		ipTextField.render(container, g);
		pwTextField.render(container, g);
		nameTextField.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {}

	@Override
	public int getID() {
		return 0;
	}

	private void loadProperties() {
		Properties propertyFile = new Properties();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream("saboteur.properties");
			propertyFile.load(fis);
		} catch (IOException e) {
			// Do nothing, we have default fallbacks
		} finally {
			try {
				fis.close();
			} catch (IOException | NullPointerException e) {

			}
		}

		playMusic = Boolean.parseBoolean(propertyFile.getProperty("play_music", "true"));
		skipMenu = Boolean.parseBoolean(propertyFile.getProperty("debug_skip_menue", "false"));
	}

}
