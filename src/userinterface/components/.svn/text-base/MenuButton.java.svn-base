package userinterface.components;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.FontUtils;

/**
 * A menu button that when clicked on will activate a change in game state (e.g.
 * exit the game).
 *
 * Author: Nick McNeil - 300222967
 */
public class MenuButton extends Button {
	private final String text;

	// font types
	TrueTypeFont normalFont = new TrueTypeFont(new Font("Dialog", Font.PLAIN,
			30), true);
	TrueTypeFont highlightedFont = new TrueTypeFont(new Font("Dialog",
			Font.BOLD, 32), true);

	// font colors
	private static Color normalColor = new Color(1, 1, 1, (float) 0.8);
	private static Color highlightedColor = Color.white;

	public MenuButton(final int buttonX, final int buttonY,
			final int buttonWidth, final int buttonHeight,
			final String buttonText) {
		super(buttonX, buttonY, buttonWidth, buttonHeight);
		this.text = buttonText;
	}

	/**
	 * Draws the button, highlighting it if the mouse is currently within its
	 * bounds.
	 *
	 * @param g
	 *            the graphics object being drawn on
	 */
	public void draw(final Graphics g) {
		// draw the button, highlighting it if the mouse is within its bounds
		if (highlighted) {
			FontUtils.drawCenter(highlightedFont, text, x, y + height / 8,
					width, highlightedColor);
		} else {
			FontUtils.drawCenter(normalFont, text, x, y + height / 6, width,
					normalColor);
		}
	}

	/**
	 * Gets the text to be displayed in the button.
	 *
	 * @return text button text
	 */
	public String getText() {
		return this.text;
	}
}