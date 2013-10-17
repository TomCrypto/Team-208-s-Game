package userinterface.components;

/**
 * A button that when clicked on will perform a game action.
 *
 * Author: Nick McNeil - 300222967
 */
public abstract class Button {
	protected final int x, y, width, height;

	protected boolean highlighted = false;
	protected boolean selected = false;

	public Button(final int buttonX, final int buttonY, final int buttonWidth,
			final int buttonHeight) {
		this.x = buttonX;
		this.y = buttonY;
		this.width = buttonWidth;
		this.height = buttonHeight;
	}

	/**
	 * Checks whether the mouse position is currently in the bounds of the
	 * button.
	 *
	 * @param mouseX
	 *            xPos of the mouse
	 * @param mouseY
	 *            yPos of the mouse
	 * @return whether the mouse is within the button bounds
	 */
	public boolean mouseInBounds(final int mouseX, final int mouseY) {
		if (mouseX > x && mouseX < x + width && mouseY > y
				&& mouseY < y + height) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets whether the button is to be highlighted.
	 *
	 * @param isHighlighted
	 *            whether the button should be highlighted or not
	 */
	public void setHighlighted(final boolean isHighlighted) {
		this.highlighted = isHighlighted;
	}

	/**
	 * Gets the current highlighted status of the button.
	 *
	 * @return whether the button is highlighted
	 */
	public boolean getHighlighted() {
		return highlighted;
	}

	/**
	 * Sets whether the button is to be selected.
	 *
	 * @param buttonSelected
	 *            whether the button should be selected or not
	 */
	public void setSelected(final boolean isSelected) {
		this.selected = isSelected;
	}

	/**
	 * Gets the current selection status of the button.
	 *
	 * @return whether the button is highlighted
	 */
	public boolean getSelected() {
		return selected;
	}

	/**
	 * Gets the x position of the top left corner of the button.
	 *
	 * @return button top left corner xPos
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y position of the top left corner of the button.
	 *
	 * @return button top left corner yPos
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the width of the button.
	 *
	 * @return button width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the button.
	 *
	 * @return button height
	 */
	public int getHeight() {
		return height;
	}
}