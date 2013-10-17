package userinterface.states;

import java.util.ArrayList;
import java.util.Arrays;

import network.packet.action.UpgradeAction;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import userinterface.Controller;
import userinterface.GameStateManager;
import userinterface.components.MenuButton;
import userinterface.components.UpgradeButton;
import ecs.components.Upgrades;
import ecs.components.Worth;
import ecs.entity.Entity;

/**
 * The upgrade menu for the game where a player can upgrade their stats (e.g.
 * rate of fire, health).
 *
 * Author: Nick McNeil - 300222967
 */
public class UpgradesState extends BasicGameState {
	private final int state;
	private boolean updated = false;

	// component location constants
	private static final int BUTTON_LEFT = 440;
	private static final int BUTTON_WIDTH = 43;
	private static final int BUTTON_TOP = 190;
	private static final int BUTTON_HEIGHT = 30;
	private static final int BUTTON_GAP = 65;

	// panel color
	private static Color panelBackColor = new Color((float) 0.0, (float) 0.7,
			(float) 1.0, 1);

	private static MenuButton back;
	private static final ArrayList<UpgradeButton> upgradeButtons = new ArrayList<UpgradeButton>();
	private static final ArrayList<String> upgradeNames = new ArrayList<String>(
			Arrays.asList("Health", "Weapon Strength", "Rate of Fire",
					"Inventory Capacity"));
	private static final ArrayList<Integer> upgradeCosts = new ArrayList<Integer>(
			Arrays.asList(10, 20, 30, 40));

	public UpgradesState(final int state) {
		this.state = state;
	}

	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {
		// set up upgrade buttons
		for (int i = 0; i < upgradeNames.size(); i++) {
			upgradeButtons.add(new UpgradeButton(BUTTON_LEFT, BUTTON_TOP
					+ (BUTTON_GAP * i), BUTTON_WIDTH, BUTTON_HEIGHT,
					upgradeNames.get(i), upgradeCosts.get(i)));
		}
		// create a back button
		back = new MenuButton(0, 408, 80, 45, "Back");
	}

	@Override
	public void render(final GameContainer gc, final StateBasedGame sbg,
			final Graphics g) throws SlickException {
		// draw the back panel
		g.setColor(panelBackColor);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight());

		// draw the letter box display
		g.setColor(Color.black);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight() / 4);
		g.fillRect(0, Display.getHeight() * 3 / 4, Display.getWidth(),
				Display.getHeight() / 4);

		// draw each of the buttons and their corresponding meter
		for (final UpgradeButton button : upgradeButtons) {
			button.draw(g);
		}
		// draw the back button
		back.draw(g);

		if(!updated){
			final Entity player = GameStateManager.getPlayer();
			if(player != null){
				player.getComponent(Upgrades.class).doUpgrades();
			}
			updated = true;
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

		// if the back button is clicked, return to the menu state (highlight if
		// mouse over).
		if (back.mouseInBounds(input.getMouseX(), input.getMouseY())) {
			back.setHighlighted(true);
			if (leftClicked) {
				sbg.enterState(1);
			}
		} else {
			back.setHighlighted(false);
		}
		for (final UpgradeButton button : upgradeButtons) {

			// if the mouse is over the button, highlight it
			if (button.mouseInBounds(input.getMouseX(), input.getMouseY())
					&& button.getEnabled()) {
				button.setHighlighted(true);

				// if the button is left clicked and the player is holding coins
				if (leftClicked
						&& GameStateManager.getPlayer().getComponent(
								Worth.class) != null) {
					// get the player coins
					final int coins = GameStateManager.getPlayer()
							.getComponent(Worth.class).getWorth();
					// if the player has enough coins, purchase the upgrade
					if (coins >= button.getUpgradeCost() && button.getEnabled()) {
						GameStateManager.gameClient
								.sendAction(new UpgradeAction(button
										.getUpgradeCost(), button
										.getUpgradeName()));
						button.upgrade();
					}
				}
			} else {
				button.setHighlighted(false);
			}
		}
	}

	/**
	 * Gets the list of upgrade buttons.
	 *
	 * @return upgradeButtons
	 */
	public static ArrayList<UpgradeButton> getButtons() {
		return upgradeButtons;
	}

	@Override
	public int getID() {
		return state;
	}
}
