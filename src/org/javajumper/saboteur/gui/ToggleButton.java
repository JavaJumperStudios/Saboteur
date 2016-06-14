package org.javajumper.saboteur.gui;

import java.util.function.Consumer;

import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.GameState;

public class ToggleButton extends Button {

	public ToggleButton(GameState instance, GUIContext container, Image image, int x, int y,
			Consumer<? super Button> action) {
		super(instance, container, image, x, y, action);
	}

}
