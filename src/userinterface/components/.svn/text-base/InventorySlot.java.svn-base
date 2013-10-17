package userinterface.components;

import java.awt.Font;
import java.util.ArrayList;

import network.packet.action.DropAction;
import network.packet.action.UseAction;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

import userinterface.GameStateManager;
import ecs.components.DamageFactor;
import ecs.components.EventTrigger;
import ecs.components.Text;
import ecs.components.Type.EntityType;
import ecs.entity.Entity;

/**
 * An inventory slot that holds one of the player's items. When clicked on, the
 * item will be selected and the player may choose an action to perform from the
 * item's current available interactions. (e.g. use, drop).
 *
 * Author: Nick McNeil - 300222967
 */
public class InventorySlot extends Button {
	private boolean leftSelected = false;
	private boolean rightSelected = false;

	private final ArrayList<InventoryButton> actionButtons = new ArrayList<InventoryButton>();
	private Entity itemHeld = null;

	private final Color slotColorNormal = new Color((float) 0.5, 0, 0, 1);
	private final Color slotColorHighlighted = new Color((float) 0.3, 0, 0,
			(float) 0.7);
	private static TrueTypeFont descriptionFont = new TrueTypeFont(new Font(
			"Dialog", Font.PLAIN, 12), true);

	public InventorySlot(final int slotX, final int slotY, final int slotWidth,
			final int slotHeight) {
		super(slotX, slotY, slotWidth, slotHeight);
		actionButtons.add(new InventoryButton(slotX - 1, slotY - 37, 40, 18,
				"Use"));
		actionButtons.add(new InventoryButton(slotX - 1, slotY - 18, 40, 18,
				"Drop"));
	}

	/**
	 * Draws the inventory slot, highlighting it if the mouse is currently
	 * within its bounds.
	 *
	 * @param g
	 *            the graphics object being drawn on
	 */
	public void draw(final Graphics g) {
		// draw the slot, changing the color if the mkouse is currently over it,
		// or it is holding an item
		if (getHighlighted()) {
			g.setColor(slotColorHighlighted);
		} else if (itemHeld != null) {
			g.setColor(new Color(0, 0, 1, (float) 0.5));
		} else {
			g.setColor(slotColorNormal);
		}
		g.fillRect(x, y, width, height);

		// if the slot is holding an item
		if (itemHeld != null) {
			// if the slot is left clicked, draw item action buttons
			if (leftSelected) {
				for (final InventoryButton button : actionButtons) {
					if (button.mouseInBounds(Mouse.getX(), Display.getHeight()
							- Mouse.getY())) {
						button.setHighlighted(true);
					} else {
						button.setHighlighted(false);
					}
					button.draw(g);
				}
			}

			// if the slot is right clicked, set the item description based on
			// the item type
			if (rightSelected) {
				String itemDescription = "";

				// if the item is a weapon, display damage and rate of fire
				if (itemHeld.getType() == EntityType.WEAPON) {
					itemDescription = itemHeld.getName();

					final int damage = itemHeld
							.getComponent(DamageFactor.class).getDamageFactor();
					final int rateOfFire = (int) itemHeld.getComponent(
							EventTrigger.class).getInterval();

					// draw damage/rate of fire back panel
					g.setColor(Color.blue);
					g.fillRect(94, y, 105, 25);

					// draw the damage
					descriptionFont.drawString(97, y, "Damage: " + damage,
							Color.white);
					// draw rate of fire
					descriptionFont.drawString(97, y + 10, "Rate of Fire: "
							+ rateOfFire, Color.white);

				} else if (itemHeld.hasAll(Text.class)) {
					itemDescription = itemHeld.getComponent(Text.class)
							.getText();
				} else {
					itemDescription = "No description available";
				}

				// draw description box
				g.setColor(new Color(62, 148, 209));
				g.fillRect(94, y - 20, itemDescription.length() * 7, 20);
				// draw description
				descriptionFont.drawString(97, y - 18, itemDescription,
						Color.white);
			}
		}
	}

	/**
	 * Updates the inventory slot's state, responding user input actions (e.g.
	 * highlighting, selecting).
	 *
	 * @param input
	 *            the input hardware used
	 */
	public void update(final Input input) {

		for (final InventoryButton button : actionButtons) {
			final boolean inBounds = button.mouseInBounds(input.getMouseX(),
					input.getMouseY());

			// if the button is left clicked, respond to the action selected
			if (leftSelected && inBounds) {
				if (button.getText().equals("Use") && itemHeld != null) {
					GameStateManager.gameClient.sendPacket(new UseAction(
							itemHeld.getID()));
				} else if (button.getText().equals("Drop") && itemHeld != null) {
					GameStateManager.gameClient.sendPacket(new DropAction(
							itemHeld.getID()));
				}
			}
		}
	}

	/**
	 * Sets the left click selection status of the button.
	 *
	 * @param isSelected
	 *            whether the slot is left clicked
	 */
	public void setLeftSelected(final boolean isSelected) {
		this.leftSelected = isSelected;
	}

	/**
	 * Gets the left click selection status of the button.
	 *
	 * @return whether the button has been left clicked
	 */
	public boolean getLeftSelected() {
		return leftSelected;
	}

	/**
	 * Sets the right click selection status of the button.
	 *
	 * @param isSelected
	 *            whether the slot is right clicked
	 */
	public void setRightSelected(final boolean isSelected) {
		this.rightSelected = isSelected;
	}

	/**
	 * Gets the right click selection status of the button.
	 *
	 * @return whether the button has been right clicked
	 */
	public boolean getRightSelected() {
		return rightSelected;
	}

	/**
	 * Sets the item to be held in the inventory slot.
	 *
	 * @param itemHeld
	 *            item to be stored
	 */
	public void setItem(final Entity item) {
		this.itemHeld = item;
	}

	/**
	 * Gets the item currently held in the inventory slot.
	 *
	 * @return item stored
	 */
	public Entity getItem() {
		return itemHeld;
	}
}