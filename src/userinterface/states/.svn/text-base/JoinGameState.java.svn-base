package userinterface.states;

import java.awt.Font;

import network.NetClient;
import network.client.observers.LoginObserver;
import network.packet.message.LoginMessage;
import network.packet.message.LoginMessage.LoginStatus;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import userinterface.GameStateManager;
import userinterface.components.MenuButton;

/**
 * The pause menu for the game where a player can start/save/load/resume a game,
 * change game settings, and exit.
 *
 * Author: Nick McNeil - 300222967
 */
public class JoinGameState extends BasicGameState implements LoginObserver {
	private final int state;
	private TextField nameTextField;
	private TextField portTextField;
	private TextField serverNameTextField;
	private StateBasedGame sbg;

	// panel color
	private static Color panelBackColor = new Color((float) 1.0, (float) 0.2,
			(float) 0.2);

	// menu components
	private static RoundedRectangle panelBack;
	private static TrueTypeFont font;
	private static MenuButton joinGameButton;
	private boolean loginFailed;

	public boolean failedToConnect, badPort;

	public JoinGameState(final int state) {
		this.state = state;
	}

	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {
		this.sbg = sbg;
		// set up join game button
		joinGameButton = new MenuButton(305, 370, 180, 35, "Join Game");

		// initialise button font
		font = new TrueTypeFont(new Font("Dialog", Font.BOLD, 18), true);

		// set up text fields
		nameTextField = new TextField(gc, gc.getDefaultFont(), 283, 230, 240,
				20);
		nameTextField.setMaxLength(20);
		nameTextField.setText("");

		portTextField = new TextField(gc, gc.getDefaultFont(), 420, 280, 80, 20);
		portTextField.setMaxLength(8);
		portTextField.setText("");

		serverNameTextField = new TextField(gc, gc.getDefaultFont(), 420, 320,
				120, 20);
		serverNameTextField.setMaxLength(12);
		serverNameTextField.setText("");

		// set the initial text field focus
		nameTextField.setFocus(true);
	}

	@Override
	public void render(final GameContainer gc, final StateBasedGame sbg,
			final Graphics g) throws SlickException {
		if (loginFailed) {
			g.drawString("Could not login", 10, 10);
		}

		// draw the back panel
		g.setColor(panelBackColor);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight());

		// draw the letter box display
		g.setColor(Color.black);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight() / 4);
		g.fillRect(0, Display.getHeight() * 3 / 4, Display.getWidth(),
				Display.getHeight() / 4);

		// draw the text fields
		g.setColor(Color.black);
		nameTextField.render(gc, g);
		portTextField.render(gc, g);
		serverNameTextField.render(gc, g);

		// draw continue button if play name and avatar has been selected
		if (!nameTextField.getText().equals("")
				&& !portTextField.getText().equals("")
				&& !serverNameTextField.getText().equals("")
				&& nameTextField.getText().length() > 2) {
			joinGameButton.draw(g);
		}

		if (failedToConnect) {
			g.setColor(Color.blue);
			g.drawString("Failed to connect to the server", 25, 435);
		}

		if (badPort) {
			g.setColor(Color.blue);
			g.drawString("Choose a port between 1 and 65535 :3", 25, 400);
		}

		// prompt the user for their login name
		g.setColor(Color.white);
		FontUtils.drawCenter(font, "Please enter your name", 0, 200,
				Display.getWidth());
		g.setFont(font);
		g.drawString("Port number :", 284, 278);
		g.drawString("Server name :", 284, 318);
	}

	@Override
	public void update(final GameContainer gc, final StateBasedGame sbg,
			final int delta) throws SlickException {
		final Input input = gc.getInput();
		final int mouseX = input.getMouseX();
		final int mouseY = input.getMouseY();

		// if the mouse is over the button, highlight it
		if (joinGameButton.mouseInBounds(input.getMouseX(), input.getMouseY())) {
			joinGameButton.setHighlighted(true);
		} else {
			joinGameButton.setHighlighted(false);
		}

		// check if the left mouse button or enter key was pressed
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)
				|| input.isKeyDown(Input.KEY_ENTER)) {

			// check for text field selection
			if (mouseInField(nameTextField, mouseX, mouseY)) {
				nameTextField.setFocus(true);
			} else if (mouseInField(portTextField, mouseX, mouseY)) {
				portTextField.setFocus(true);
			} else if (mouseInField(serverNameTextField, mouseX, mouseY)) {
				serverNameTextField.setFocus(true);
			}

			// if the continue button is clicked (and visible), begin gameplay
			if (joinGameButton.mouseInBounds(input.getMouseX(),
					input.getMouseY())
					|| input.isKeyPressed(Input.KEY_ENTER)
					&& !nameTextField.getText().equals("")
					&& !portTextField.getText().equals("")
					&& !serverNameTextField.getText().equals("")
					&& nameTextField.getText().length() > 2) {
				loginFailed = false;

				final LoginMessage login = new LoginMessage(
						nameTextField.getText());

				final String hostname = serverNameTextField.getText();
				int port = 0;

				try {
					port = Integer.parseInt(portTextField.getText());
				} catch (final NumberFormatException e) {
					badPort = true;
					return;
				}

				try {
					GameStateManager.gameClient.connect(hostname, port);
					failedToConnect = false;
					badPort = false;
				} catch (final Exception e) {
					// pray this never fails
				}

				GameStateManager.gameClient.sendLogin(login);
			}
		}
	}

	@Override
	public int getID() {
		return state;
	}

	@Override
	public void loginSucceeded(final NetClient client) {
		System.out.println("logged in");
		sbg.enterState(0);
	}

	@Override
	public void loginFailed(final NetClient client, final LoginStatus status) {
		loginFailed = true;
	}

	public boolean mouseInField(final TextField field, final int mouseX,
			final int mouseY) {
		if (mouseX < field.getX() + field.getWidth() && mouseX > field.getX()
				&& mouseY < field.getY() + field.getHeight()
				&& mouseY > field.getY()) {
			return true;
		} else {
			return false;
		}
	}
}
