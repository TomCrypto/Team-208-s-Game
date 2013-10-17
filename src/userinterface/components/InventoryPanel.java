package userinterface.components;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.util.FontUtils;

import userinterface.GameStateManager;
import ecs.components.Inventory;
import ecs.components.Type.EntityType;
import ecs.entity.Entity;

/**
 * The InventoryPanel displays the player's inventory in inventory slots which
 * can be clicked to interact with inventory items (e.g. use, drop).
 *
 * Author: Nick McNeil - 300222967
 */
public class InventoryPanel {
	private final RoundedRectangle inventoryPanelBottom = new RoundedRectangle(
			5, 442, 88, 154, 2);
	private final RoundedRectangle inventoryPanelTop = new RoundedRectangle(5,
			442, 88, 25, 2);

	private final ArrayList<InventorySlot> inventorySlots = new ArrayList<InventorySlot>();

	private final Color panelTopColor = new Color((float) 0.5, 0, 0,
			(float) 0.3);
	private final Color panelBottomColor = new Color((float) 0.5, 0, 0,
			(float) 0.7);

	public InventoryPanel() {
		// create a 2x3 group of inventory slots
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				inventorySlots.add(new InventorySlot(8 + (j * 42),
						469 + (i * 42), 39, 39));
			}
		}
	}

	/**
	 * Displays the inventory panel on the current graphics object.
	 *
	 * @param g
	 *            graphics object to display on
	 */
	public void draw(final Graphics g) {
		// draw inventory panel
		g.setColor(panelBottomColor);
		g.fill(inventoryPanelBottom);
		g.setColor(panelTopColor);
		g.fill(inventoryPanelTop);

		// draw inventory slots
		for (final InventorySlot slot : inventorySlots) {
			slot.draw(g);
		}

		// if an item is currently highlighted, draw its name in the top panel.
		for (final InventorySlot slot : inventorySlots) {
			// if a slot is highlighted and contains an item
			if (slot.getHighlighted() && slot.getItem() != null) {
				String itemName = "";
				if (slot.getItem().getType() == EntityType.WEAPON) {
					itemName = "Weapon";
				} else {
					itemName = slot.getItem().getName();
				}
				FontUtils.drawCenter(g.getFont(), itemName, 4, 446, 88,
						Color.white);
				return;
			}
		}
		// else if an item isn't highlighted, draw the inventory capacity
		final int inventoryWeight = GameStateManager.getPlayer()
				.getComponent(Inventory.class).getCurrent();
		final int inventoryCapacity = GameStateManager.getPlayer()
				.getComponent(Inventory.class).getMaxSize();
		FontUtils.drawCenter(g.getFont(), inventoryWeight + " / "
				+ inventoryCapacity, 4, 446, 88, Color.white);
	}

	/**
	 * Updates the inventory panel, responding user input actions (e.g.
	 * selecting inventory slots).
	 *
	 * @param input
	 *            the input hardware used
	 */
	public void update(final Input input) {
		// if the player does not exist, do not update
		if (GameStateManager.getPlayer() == null) {
			return;
		}

		final boolean leftClicked = input
				.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		final boolean rightClicked = input
				.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
		final List<Entity> playerInventory = (GameStateManager.getPlayer()
				.getComponent(Inventory.class)).getInventory();

		int i = 0;
		for (final InventorySlot slot : inventorySlots) {
			// add an item from the player's inventory to the slot
			if (i < playerInventory.size()) {
				slot.setItem(playerInventory.get(i));
				i++;
			} else {
				slot.setItem(null);
			}

			// if the mouse is over the slot, highlight it
			final boolean inBounds = slot.mouseInBounds(input.getMouseX(),
					input.getMouseY());
			if (inBounds) {
				slot.setHighlighted(true);
			} else {
				slot.setHighlighted(false);
			}

			// if the mouse is left clicked
			if (leftClicked) {
				// and the slot is already selected
				if (slot.getLeftSelected()) {
					// checks if a slot action is selected, then deselect the
					// slot
					slot.update(input);
					slot.setLeftSelected(false);
				} else {
					// if the mouse is in bounds, select the slot
					if (inBounds) {
						slot.setLeftSelected(true);
					}
					slot.setRightSelected(false);
				}
			}
			// if the mouse is right clicked
			else if (rightClicked) {
				// and the slot is already selected
				if (slot.getRightSelected()) {
					// deselects the slot
					slot.setRightSelected(false);
				} else {
					// if the mouse is in bounds, select the slot
					if (inBounds) {
						slot.setRightSelected(true);
					}
					slot.setLeftSelected(false);
				}
			}
		}
	}

	/**
	 * Gets the inventory slots stored in the panel.
	 *
	 * @return inventory slots
	 */
	public ArrayList<InventorySlot> getInventorySlots() {
		return inventorySlots;
	}
}