package userinterface.components;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class UpgradeMeter {
	private final int BAR_GAP = 5;
	private final int BAR_WIDTH = 14;
	private final int BAR_HEIGHT = 29;

	// meter bar colors
	private final ArrayList<Color> barColors = new ArrayList<Color>(Arrays.asList(
			new Color((float) 1, 0, 0), new Color(1, (float) 0.25, 0),
			new Color(1, (float) 0.5, 0), new Color(1, (float) 0.75, 0),
			new Color(1, 1, (float) 0), new Color((float) 0.8, 1, 0),
			new Color((float) 0.6, 1, 0), new Color((float) 0.4, 1, 0),
			new Color((float) 0.2, 1, 0), new Color(0, (float) 1, 0)));

	private final int x;
	private final int y;
	private int bars;

	public UpgradeMeter(final int xPos, final int yPos) {
		this.x = xPos;
		this.y = yPos;
	}

	/**
	 * Draws the upgrade meter onto the graphics components where the number of
	 * bars displayed depends on how many times the upgrade has been purchased.
	 *
	 * @param g
	 *            the graphics component to draw on
	 */
	public void draw(final Graphics g) {
		// draw bar outline
		g.setColor(Color.white);
		g.drawRoundRect(x, y, 197, 40, 3);
		// draw the meter bars
		for (int i = 0; i < bars; i++) {
			g.setColor(barColors.get(i));
			g.fillRoundRect(x + BAR_GAP + ((BAR_GAP + BAR_WIDTH) * i), y + 6,
					BAR_WIDTH, BAR_HEIGHT, 2);
		}
	}

	/**
	 * Adds a bar to the meter. This will be called whenever the associated
	 * upgrade has been purchased.
	 */
	public void addBar() {
		bars++;
	}

	/**
	 * Gets the number of bars the meter will display.
	 *
	 * @return number of bars in the meter
	 */
	public int getBars() {
		return bars;
	}
}
