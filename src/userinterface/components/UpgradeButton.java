package userinterface.components;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

/**
 * An upgrade button that when clicked on will select an upgrade that can be
 * purchased.
 *
 * Author: Nick McNeil - 300222967
 */
public class UpgradeButton extends Button {
	private final int MAX_BARS = 10;
	private final int upgradeCost;
	private final String upgradeName;
	private final UpgradeMeter meter;
	private final Image coin;

	private boolean enabled = true;

	// font types
	TrueTypeFont normalFont = new TrueTypeFont(new Font("Dialog", Font.PLAIN,
			20), true);
	TrueTypeFont highlightedFont = new TrueTypeFont(new Font("Dialog",
			Font.BOLD, 20), true);
	TrueTypeFont nameFont = new TrueTypeFont(new Font("Dialog", Font.BOLD, 18),
			true);

	// font colors
	private static Color normalColor = new Color(1, 1, 1, (float) 0.8);
	private static Color highlightedColor = Color.white;

	public UpgradeButton(final int buttonX, final int buttonY,
			final int buttonWidth, final int buttonHeight,
			final String buttonName, final int coinsRequired) {
		super(buttonX, buttonY, buttonWidth, buttonHeight);
		this.upgradeName = buttonName;
		this.upgradeCost = coinsRequired;

		// create the associated upgrade meter
		this.meter = new UpgradeMeter(x - 230, y - 9);

		// load the coin image
		Image coinRes = null;
		try {
			coinRes = new Image("assets/sprites/coin.png");
		} catch (final SlickException e) {
			e.printStackTrace();
		}
		coin = coinRes;
	}

	/**
	 * Draws the upgrade button onto the graphics component, altering it's look
	 * depending on whether the upgrade has been maxed out or highlighted.
	 *
	 * @param g
	 *            the graphics component to draw onto
	 */
	public void draw(final Graphics g) {
		// draw the upgrade name
		g.setColor(Color.white);
		g.setFont(nameFont);
		g.drawString(upgradeName, x - 228, y - 32);

		// if the button is no longer enabled, set it to display upgrade maxed
		if (enabled == false) {
			g.setColor(Color.red);
			g.drawRect(x, y, width + 28, height);
			g.drawString("Maxed", x + 5, y + 4);
		} else {
			// draw upgrade cost
			g.drawString("" + upgradeCost, x + 67, y + 4);

			// if the mouse is within the button bounds, highlight it and
			// increase the text size
			if (highlighted) {
				g.setFont(highlightedFont);
				g.setColor(highlightedColor);
			} else {
				g.setFont(normalFont);
				g.setColor(normalColor);
			}
			g.drawRect(x, y, width, height);
			g.drawString("Buy", x + 2, y + 4);

			// draw the coin image
			coin.draw(x + 49, y + 6, (float) 0.28);
		}
		// draw the upgrade meter
		meter.draw(g);
	}

	/**
	 * Performs an upgrade and disables the button if the upgrade has been maxed
	 * out.
	 */
	public void upgrade() {
		// add the upgrade
		meter.addBar();
		// if the upgrade bar has been maxed out, disable the button
		if (meter.getBars() == MAX_BARS) {
			enabled = false;
		}
	}

	/**
	 * Gets the button's enabled status (a button will be disabled if all the
	 * upgrades for that slots has been purchased already).
	 *
	 * @return whether the button is enabled
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * Gets the cost of the associated upgrade.
	 *
	 * @return upgrade cost
	 */
	public int getUpgradeCost() {
		return upgradeCost;
	}

	/**
	 * Gets the name of the associated upgrade.
	 *
	 * @return upgrade name
	 */
	public String getUpgradeName() {
		return upgradeName;
	}
}