package userinterface.states;

import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import userinterface.components.MenuButton;

/**
 * Prompts the player with an exit confirmation dialog, clicking yes will end
 * the game, clicking no will return to the menu state.
 *
 * Author: Nick McNeil - 300222967
 */
public class ExitConfirmationState extends BasicGameState {
	private final int state;

	// panel color
	private static Color panelBackColor = new Color(1, (float) 0.5, 0);

	// menu components
	private static RoundedRectangle panelBack;
	private static TrueTypeFont font;
	private static MenuButton yesButton;
	private static MenuButton noButton;

	public ExitConfirmationState(final int state) {
		this.state = state;
	}

	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {
		// initialise panel background
		panelBack = new RoundedRectangle(Display.getWidth() / 3,
				Display.getHeight() / 3, Display.getWidth() / 3,
				Display.getHeight() / 6, 3);

		// set up buttons
		yesButton = new MenuButton(280, 260, 100, 35, "Yes");
		noButton = new MenuButton(417, 260, 100, 35, "No");

		// initialise font
		font = new TrueTypeFont(new Font("Dialog", Font.PLAIN, 18), true);
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

		// draw yes/no buttons
		yesButton.draw(g);
		noButton.draw(g);

		g.setColor(Color.black);
		FontUtils.drawCenter(font, "Are you sure you want to exit?",
				Display.getWidth() / 3, 215, Display.getWidth() / 3);

	}

	@Override
	public void update(final GameContainer gc, final StateBasedGame sbg,
			final int delta) throws SlickException {
		final Input input = gc.getInput();

		if (yesButton.mouseInBounds(input.getMouseX(), input.getMouseY())) {
			yesButton.setHighlighted(true);
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				gc.exit();
			}
		} else {
			yesButton.setHighlighted(false);
		}

		if (noButton.mouseInBounds(input.getMouseX(), input.getMouseY())) {
			noButton.setHighlighted(true);
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(1);
			}
		} else {
			noButton.setHighlighted(false);
		}
	}

	@Override
	public int getID() {
		return state;
	}
}
