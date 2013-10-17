package userinterface.states;

import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

/**
 * The splash screen which introduces the player to the game when it first
 * loads.
 *
 * Author: Nick McNeil - 300222967
 */
public class SplashScreenState extends BasicGameState {
	private final int state;
	private final int controlPanelLeft = 400;

	private static TrueTypeFont panelFont;
	private static TrueTypeFont gameNameFont;

	private static Color panelColor = new Color(1, 1, 1, (float) 0.9);

	public SplashScreenState(final int state) {
		this.state = state;
	}

	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {
		panelFont = new TrueTypeFont(new Font("Dialog", Font.PLAIN, 18), true);
		gameNameFont = new TrueTypeFont(new Font("impact", Font.PLAIN, 42),
				true);
	}

	@Override
	public void render(final GameContainer gc, final StateBasedGame sbg,
			final Graphics g) throws SlickException {
		// draw panel background
		g.setColor(Color.pink);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight());

		g.setColor(panelColor);
		g.fillRoundRect(controlPanelLeft - 10, 228, 352, 253, 5);
		g.fillRoundRect(30, 228, 255, 110, 5);

		g.setColor(Color.black);

		FontUtils.drawCenter(gameNameFont, "The Game of Love and Affection", 0,
				30, Display.getWidth(), Color.black);

		// load the heart image
		Image heart = null;
		try {
			heart = new Image("assets/sprites/heart.png");
		} catch (final SlickException e) {
			e.printStackTrace();
		}
		if (heart != null) {
			heart.draw(330, 100, (float) 0.3);
		}
		g.drawString("You are an astronaught", 33, 235);
		g.drawString("named Peter. Your goal is", 33, 255);
		g.drawString("to kill all the zombies", 33, 275);
		g.drawString("because you have no purpose", 33, 295);
		g.drawString("in life.", 33, 315);

		g.drawString("Controls", controlPanelLeft + 5, 235);
		g.drawString("---------------------------------------",
				controlPanelLeft - 10, 253);
		g.drawString("W, A, S, D   :   Move Player", controlPanelLeft, 275);
		g.drawString("Arrow Keys   :   Move Camera", controlPanelLeft, 300);
		g.drawString("E            :   Action", controlPanelLeft, 325);
		g.drawString("ESC          :   Menu/Escape", controlPanelLeft, 350);
		g.drawString("F1           :   Open Chatbox", controlPanelLeft, 375);
		g.drawString("F2           :   Open/Close Map", controlPanelLeft, 400);
		g.drawString("LMB          :   Shoot/Select", controlPanelLeft, 425);
		g.drawString("RMB          :   Query Inventory", controlPanelLeft, 450);

		FontUtils.drawCenter(panelFont,
				"Press Space/Enter to begin a new game", 0, 510,
				Display.getWidth(), Color.black);
	}

	@Override
	public void update(final GameContainer gc, final StateBasedGame sbg,
			final int delta) throws SlickException {
		final Input input = gc.getInput();

		// check if the space/enter key or left mouse button is pressed
		if (input.isKeyPressed(Input.KEY_SPACE)
				|| input.isKeyPressed(Input.KEY_ENTER)
				|| input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			// join a new game
			sbg.enterState(3);
		}
	}

	@Override
	public int getID() {
		return state;
	}
}
