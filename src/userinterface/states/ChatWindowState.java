package userinterface.states;

import network.client.observers.ChatObserver;
import network.packet.message.PublicMessage;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import userinterface.Controller;
import userinterface.GameStateManager;

/**
 * The ChatWindow allows players to send a message (entered in the text field)
 * to the other connected players.
 *
 * Author: Nick McNeil - 300222967
 */
public class ChatWindowState extends BasicGameState implements ChatObserver {
	private final int state;
	private TextField chatWindow;
	private boolean chatVisible = false;

	public ChatWindowState(final int state) {
		this.state = state;
	}

	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {
		chatWindow = new TextField(gc, gc.getDefaultFont(), 120, 575, 650, 20);
		chatWindow.setMaxLength(70);
	}

	@Override
	public void render(final GameContainer gc, final StateBasedGame sbg, final Graphics g)
			throws SlickException {
		g.setAntiAlias(true);

		chatWindow.setFocus(true);
		sbg.getState(0).render(gc, sbg, g);
		chatWindow.render(gc, g);
	}

	@Override
	public void update(final GameContainer gc, final StateBasedGame sbg, final int delta)
			throws SlickException {
		final Input input = gc.getInput();
		// if the chat window has just been called up, reset the text (prevents
		// the chat key from displaying in the text field)
		if (chatVisible == false) {
			chatWindow.setText("");
			chatVisible = true;
		}

		// if the menu button is pressed, return to gameplay
		if (input.isKeyPressed(Controller.menu)) {
			chatVisible = false;
			sbg.enterState(0);
			// if the enter key is pressed
		} else if (input.isKeyPressed(Input.KEY_ENTER)) {
			// if text was entered, send message
			if (!chatWindow.getText().equals("")) {
				GameStateManager.gameClient.sendPacket(new PublicMessage(chatWindow.getText()));
				//GameplayState.sendMessage(chatWindow.getText());
			}
			// close the chat window and return to gameplay
			chatVisible = false;
			sbg.enterState(0);
		}
	}

	@Override
	public int getID() {
		return state;
	}

	@Override
	public void noSuchRecipient(final String recipient, final String message) {
		// the recipient of a message did not exist
	}

	@Override
	public void privateMessageReceived(final String source, final String message) {
		// a private message from a player was received
		// (only you and him can see this message)
		GameplayState.sendMessage(message);
	}

	@Override
	public void publicMessageReceived(final String source, final String message) {
		// a public message from a player was received
		// (everyone in the same location as you received it too)
		GameplayState.sendMessage(String.format("%s: %s", source, message));
	}
}
