package userinterface.states;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import userinterface.Controller;
import userinterface.components.MenuButton;

/**
 * The pause menu for the game where a player can start/save/load/resume a game,
 * change game settings, and exit.
 *
 * Author: Nick McNeil - 300222967
 */
public class MenuState extends BasicGameState {
	private final int state;

	// component location constants
	private static final int BUTTON_LEFT = 298;
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_TOP = 175;
	private static final int BUTTON_HEIGHT = 40;
	private static final int BUTTON_GAP = 91;

	// panel color
	private static Color panelBackColor = new Color((float) 1.0, (float) 0.2,
			(float) 0.2, (float) 0.8);

	// menu components
	private static final ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
	private static final ArrayList<String> buttonNames = new ArrayList<String>(
			Arrays.asList("Resume", "Upgrades", "Exit Game"));

	public MenuState(final int state) {
		this.state = state;
	}

	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {

		// set up menu buttons
		for (int i = 0; i < buttonNames.size(); i++) {
			buttons.add(new MenuButton(BUTTON_LEFT, BUTTON_TOP
					+ (BUTTON_GAP * i), BUTTON_WIDTH, BUTTON_HEIGHT,
					buttonNames.get(i)));
		}
	}

	@Override
	public void render(final GameContainer gc, final StateBasedGame sbg,
			final Graphics g) throws SlickException {
		g.setAntiAlias(true);

		g.setColor(panelBackColor);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight());

		// draw the letter box display
		g.setColor(Color.black);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight() / 4);
		g.fillRect(0, Display.getHeight() * 3 / 4, Display.getWidth(),
				Display.getHeight() / 4);

		// draw each of the menu buttons
		for (final MenuButton button : buttons) {
			button.draw(g);
		}
	}

	@Override
	public void update(final GameContainer gc, final StateBasedGame sbg,
			final int delta) throws SlickException {
		final Input input = gc.getInput();
		final boolean leftClicked = input
				.isMousePressed(Input.MOUSE_LEFT_BUTTON);

		// check keyboard input
		if (input.isKeyPressed(Controller.menu)) {
			sbg.enterState(0);
		}

		for (final MenuButton button : buttons) {
			// if the mouse is over the button, highlight it
			if (button.mouseInBounds(input.getMouseX(), input.getMouseY())) {
				button.setHighlighted(true);
				if (leftClicked || input.isKeyPressed(Input.KEY_ENTER)) {
					// ENTER
					if (button.getText().equals("Resume")) {
						sbg.enterState(0);
					}
					else if (button.getText().equals("New Game")) {
						sbg.enterState(3);
					}
					else if (button.getText().equals("Upgrades")) {
						sbg.enterState(2);
					}
					else if (button.getText().equals("Exit Game"))
						sbg.enterState(5);
				}
			} else {
				button.setHighlighted(false);
			}
		}
	}

	@Override
	public int getID() {
		return state;
	}
}
