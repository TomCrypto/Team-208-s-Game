package userinterface;

import java.io.IOException;

import network.client.ClientObserver;
import network.client.GameClient;
import network.client.observers.GeneralObserver;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import userinterface.states.ChatWindowState;
import userinterface.states.ExitConfirmationState;
import userinterface.states.GameplayState;
import userinterface.states.JoinGameState;
import userinterface.states.MenuState;
import userinterface.states.SplashScreenState;
import userinterface.states.UpgradesState;
import ecs.entity.Entity;

/**
 * Manages the game states
 *
 * Author: Nick McNeil - 300222967
 */
public class GameStateManager extends StateBasedGame implements GeneralObserver {
	private final static String GAME_NAME = "The Game of Love and Affection";
	private final int gameplay = 0;
	private final int menu = 1;
	private final int upgrades = 2;
	private final int newGame = 3;
	private final int chatWindow = 4;
	private final int exitConfirmation = 5;
	private final int splashScreen = 6;

	public static GameClient gameClient;

	public GameStateManager(final String gamename) throws IOException {
		super(gamename);

		final JoinGameState loginState = new JoinGameState(newGame);
		final GameplayState gameplayState = new GameplayState(gameplay);
		final ChatWindowState chatWindowState = new ChatWindowState(chatWindow);

		final ClientObserver observer = new ClientObserver(this, loginState,
				null, chatWindowState);

		gameClient = new GameClient(observer);

		this.addState(gameplayState);
		this.addState(loginState);
		this.addState(chatWindowState);
		this.addState(new MenuState(menu));
		this.addState(new UpgradesState(upgrades));
		this.addState(new ExitConfirmationState(exitConfirmation));
		this.addState(new SplashScreenState(splashScreen));
	}

	@Override
	public void initStatesList(final GameContainer gc) throws SlickException {
		gc.setAlwaysRender(true);

		// enter the starting game state
		this.enterState(splashScreen);
	}

	public static void main(final String[] args) throws IOException {
		// create game container
		AppGameContainer appgc;
		try {
			// add a new game to the container
			appgc = new AppGameContainer(new GameStateManager(GAME_NAME));
			// set the display window size
			appgc.setDisplayMode(800, 600, false);
			// turn off displaying the FPS rate
			appgc.setShowFPS(false);
			// load the initial game state and begin the game
			appgc.start();
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(final Exception exception) {
		// something's gone wrong with the network connection - display an error
		// message and freeze/exit the game

	    final GameState state = getCurrentState();

	    if (state instanceof GameplayState) ((GameplayState)state).setDisconnected();
	    if (state instanceof JoinGameState) ((JoinGameState)state).failedToConnect = true;
	}

	/**
	 * Gets the client player entity.
	 *
	 * @return player the client player entity
	 */
	public static Entity getPlayer() {
		return gameClient.getPlayerEntity();
	}
}
