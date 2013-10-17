package userinterface.components;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.FontUtils;

/**
 * An inventory button that when clicked, performs an action upon an inventory
 * item (e.g. use, drop).
 *
 * Author: Nick McNeil - 300222967
 */
public class InventoryButton extends Button {
	private final String text;
	private final int lineWidth = 1;

	private final Color outlineColor = Color.darkGray;
	private final Color highlightColor = Color.blue;
	private final Color backgroundColor = Color.gray;

	private final static TrueTypeFont font = new TrueTypeFont(new Font(
			"Dialog", Font.BOLD, 12), true);

	public InventoryButton(final int buttonX, final int buttonY,
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
		// draw button background
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);

		// draw button outline
		if (highlighted) {
			g.setColor(highlightColor);
		} else {
			g.setColor(outlineColor);
		}
		g.setLineWidth(lineWidth);
		g.drawRect(x, y, width, height);

		// draw button text
		FontUtils.drawCenter(font, text, x, y + height / 6, width, Color.black);
	}

	/**
	 * Gets the text to be displayed in the button
	 *
	 * @return text button text
	 */
	public String getText() {
		return this.text;
	}
}