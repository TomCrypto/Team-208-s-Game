package userinterface.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.FontUtils;

import userinterface.GameStateManager;
import ecs.components.Equipped;
import ecs.components.Health;
import ecs.components.Worth;
import ecs.entity.Entity;

/**
 * Displays information about the local player's name, health, coins, location
 * and currently equipped weapon.
 *
 * Author: Nick McNeil - 300222967
 */
public class HUD {
	private final Image coin;

	public HUD() {
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
	 * Draws the HUD components on the graphics object.
	 *
	 * @param g
	 *            graphics object to display on
	 */
	public void display(final Graphics g) {
		// check that the player has logged in
		if (GameStateManager.getPlayer() != null) {
			final Entity player = GameStateManager.getPlayer();

			// draw health bar
			g.setColor(Color.red);
			g.fillRoundRect(7, 28, player.getComponent(Health.class)
					.getCurrent(), 9, 3);
			g.setColor(Color.black);
			g.drawRoundRect(7, 28, player.getComponent(Health.class).getMax(),
					9, 3);

			// display credit value
			coin.draw(7, 42, (float) 0.225);
			g.setColor(Color.white);
			if (player.getComponent(Worth.class) == null) {
				g.drawString("0", 20, 42);
			} else {
				g.drawString("" + player.getComponent(Worth.class).getWorth(),
						20, 42);
			}

			// draw the currently equipped weapon
			if (player.getComponent(Equipped.class).hasEquipped()) {
				g.drawString(player.getComponent(Equipped.class).getEquipped()
						.getName(), 7, 60);
			}

			// draw location
			FontUtils.drawRight(g.getFont(), GameStateManager.gameClient
					.getCurrentLocation().getName(), 570, 5, 225);

			// draw player name
			FontUtils.drawLeft(g.getFont(), player.getName().toString(), 5, 5);
		}
	}
}