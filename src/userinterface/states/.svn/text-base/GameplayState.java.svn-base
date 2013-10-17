package userinterface.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import network.packet.action.InteractAction;
import network.packet.action.MovementAction;
import network.packet.action.PlayerShootAction;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import renderer.Model;
import renderer.ModelLoader;
import renderer.ModelType;
import renderer.Render3D;
import userinterface.Controller;
import userinterface.GameStateManager;
import userinterface.components.HUD;
import userinterface.components.InventoryPanel;
import userinterface.components.InventorySlot;
import ecs.components.ModelData;
import ecs.components.Position;
import ecs.components.Type.EntityType;
import ecs.components.Volume;
import ecs.entity.Entity;
import ecs.world.Location;

/**
 * Controls gameplay, responding to user input and rendering to the display.
 */
public class GameplayState extends BasicGameState {
	private final int state;
	private boolean mapVisible = false;

	private HUD hud;
	private InventoryPanel inventory;

	private static class MessageRecord {
		public final String message;
		public final Long time;

		public MessageRecord(final String message) {
			this.message = message;
			time = System.currentTimeMillis();
		}
	}

	private static List<MessageRecord> messages = new ArrayList<MessageRecord>();

	private Render3D r3d;
	private ModelLoader ml;

	private HashMap<ModelType, Model> modelMap;

	private Vector2f playerPos;
	private float playerRotation;

	private boolean disconnected;

	public void setDisconnected() {
		disconnected = true;
	}

	public GameplayState(final int state) {
		this.state = state;
	}

	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {

		if (modelMap == null) {
			modelMap = new HashMap<ModelType, Model>();
		}
		// initialise hud components
		hud = new HUD();
		inventory = new InventoryPanel();

		// Setup the 3D renderer
		r3d = new Render3D(Display.getWidth(), Display.getHeight());
		ml = new ModelLoader();

		// Locations of shaders
		final String fragShader = "assets/shaders/fragment.fs";
		final String vertShader = "assets/shaders/vertex.vs";

		// Load all of the models into the modelmap
		if (!modelMap.containsKey(ModelType.BOX)) {
			modelMap.put(ModelType.BOX, ml.load("assets/models/box.obj",
					"assets/textures/cube-tex.png", vertShader, fragShader));
		}

		if (!modelMap.containsKey(ModelType.ROOM)) {
			modelMap.put(ModelType.ROOM, ml.load("assets/models/room.obj",
					"assets/textures/room-tex.png", vertShader, fragShader));
		}

		if (!modelMap.containsKey(ModelType.PLAYER)) {
			modelMap.put(ModelType.PLAYER, ml.load(""
					+ "assets/models/player.obj",
					"assets/textures/man-tex.png", vertShader, fragShader));
		}

		if (!modelMap.containsKey(ModelType.OUTSIDE)) {
			modelMap.put(ModelType.OUTSIDE, ml.load(
					"assets/models/outside.obj",
					"assets/textures/outside-tex.png", vertShader, fragShader));
		}

		if (!modelMap.containsKey(ModelType.BULLET)) {
			modelMap.put(ModelType.BULLET, ml.load("assets/models/bullet.obj",
					"assets/textures/bullet-tex.png", vertShader, fragShader));
		}
		playerPos = new Vector2f(0.0f, 0.0f);
	}

	/**
	 * The drawing part of the render loop. Redraws the Display object.
	 */
	@Override
	public void render(final GameContainer gc, final StateBasedGame sbg,
			final Graphics g) throws SlickException {

		if (GameStateManager.gameClient.getClient() == null)
			return;

		// Update the r3d object
		r3d.loopCycle();

		// Update the camera based on the player's position
		if (GameStateManager.getPlayer() != null) {
			final double playerX = GameStateManager.getPlayer()
					.getComponent(Position.class).getX();
			final double playerY = GameStateManager.getPlayer()
					.getComponent(Position.class).getY();
			playerPos.x = (float) playerX;
			playerPos.y = (float) playerY;
			r3d.setCameraPos(new Vector3f(0 - (float) playerX, (float) playerY,
					-1.0f));
		}

		if (disconnected) {
			g.drawString("THE CONNECTION TO THE SERVER WAS LOST!",
					gc.getWidth() / 2 - 250 / 2, gc.getHeight() / 2 - 50 / 2);
			return;
		}

		final Location location = GameStateManager.gameClient
				.getCurrentLocation();

		if (location == null) {
			g.drawString("LOCATION LOADING...", gc.getWidth() / 2 - 250 / 2,
					gc.getHeight() / 2 - 50 / 2);
			return;
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// Draw the room
		final Vector3f rPos = new Vector3f(0.5f, -0.5f, 0.0f);
		final Vector3f rRot = new Vector3f(0.0f, 0.0f, 0.0f);
		final Vector3f rSca = new Vector3f(0.02f, 0.02f, 0.02f);

		final ModelType worldModel = location.getModelType();

		if (worldModel != null) {
			modelMap.get(worldModel).drawAs(rPos, rRot, rSca,
					r3d.getProjectionMatrix(), r3d.getViewMatrix());
		}

		synchronized (location) {
			final Matrix4f pm = r3d.getProjectionMatrix();
			final Matrix4f vm = r3d.getViewMatrix();

			for (final Entity ent : location.getEntities()) {
				if (ent.hasAll(Position.class, Volume.class, ModelData.class)) {

					final Position pos = ent.getComponent(Position.class);
					final Vector3f vPos = new Vector3f((float) (pos.getX()),
							0.0f - (float) (pos.getY()), 0.0f);

					Vector3f vRot;

					if (ent.getType() == EntityType.PLAYER
							&& ent.getName().equals(
									GameStateManager.gameClient.getClient()
											.getName())) {
						vRot = new Vector3f(0.0f, 0.0f, playerRotation);
					} else {
						vRot = new Vector3f(0.0f, 0.0f, 0.0f);
					}

					final Vector3f vSca = new Vector3f(0.02f, 0.02f, 0.02f);

					ModelType mType = ent.getComponent(ModelData.class)
							.getType();
					if (mType == ModelType.ZOMBIE)
						mType = ModelType.PLAYER;
					if (mType == ModelType.NPC)
						mType = ModelType.PLAYER;

					modelMap.get(mType).drawAs(vPos, vRot, vSca, pm, vm);
				}
			}
		}

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		// display the hud and inventory
		hud.display(g);
		inventory.draw(g);

		// load the map image
		if (mapVisible) {
			Image mapRes = null;
			try {
				mapRes = new Image("assets/sprites/map.jpg");
			} catch (final SlickException e) {
				e.printStackTrace();
			}
			mapRes.draw(80, 80, (float) 0.4);
		}

		final Iterator<MessageRecord> iter = messages.iterator();

		while (iter.hasNext()) {
			final MessageRecord record = iter.next();

			if (System.currentTimeMillis() - record.time > 15000)
				iter.remove();
		}

		// draw messages
		int height = 550 - 20 * (messages.size() - 1);
		for (final MessageRecord record : messages) {
			g.drawString(record.message, 120, height);
			height += 20;
		}
	}

	/**
	 * This is the 'logic' step of the rendering loop. Processes user input, etc
	 */
	@Override
	public void update(final GameContainer gc, final StateBasedGame sbg,
			final int delta) throws SlickException {

		if (GameStateManager.gameClient.getClient() == null) {
			return;
		}

		gc.setTargetFrameRate(60);

		final Input input = gc.getInput();
		final int mouseX = input.getMouseX();
		final int mouseY = input.getMouseY();

		// Update player rotation
		final double mX = ((mouseX / ((double) Display.getWidth())) - 0.5);
		final double mY = ((mouseY / ((double) Display.getHeight())) - 0.5);
		playerRotation = (float) Math.toDegrees(Math.atan2(mX, mY));
		if (playerRotation < 0) {
			playerRotation += 360;
		}

		// check keyboard input
		if (input.isKeyPressed(Controller.menu)) {
			sbg.enterState(1);
		} else if (input.isKeyPressed(Controller.chat)) {
			sbg.enterState(4);
		} else if (input.isKeyDown(Controller.up)) {
			GameStateManager.gameClient.sendPacket(new MovementAction(
					MovementAction.Direction.UP));
		} else if (input.isKeyDown(Controller.down)) {
			GameStateManager.gameClient.sendPacket(new MovementAction(
					MovementAction.Direction.DOWN));
		} else if (input.isKeyPressed(Controller.map)) {
			mapVisible = !mapVisible;
		} else if (input.isKeyDown(Controller.left)) {
			GameStateManager.gameClient.sendPacket(new MovementAction(
					MovementAction.Direction.LEFT));
		} else if (input.isKeyDown(Controller.right)) {
			GameStateManager.gameClient.sendPacket(new MovementAction(
					MovementAction.Direction.RIGHT));
		} else if (input.isKeyPressed(Controller.action)) {
			GameStateManager.gameClient.sendPacket(new InteractAction());
		} else if (input.isKeyDown(Controller.cameraAddPitch)) {
			r3d.incrementCameraPitch();
		} else if (input.isKeyDown(Controller.cameraRemPitch)) {
			r3d.decrementCameraPitch();
		} else if (input.isKeyDown(Controller.cameraAddHeading)) {
			r3d.incrementCameraHeading();
		} else if (input.isKeyDown(Controller.cameraRemHeading)) {
			r3d.decrementCameraHeading();
		}

		// check for inventory item selected
		inventory.update(input);

		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			// Translate the screen-coords into player relative co-ords
			final float xPos = (float) ((mouseX / (double) Display.getWidth()) - 0.5)
					+ playerPos.x;
			final float yPos = (float) ((mouseY / (double) Display.getHeight()) - 0.5)
					+ playerPos.y;

			for (final InventorySlot slot : inventory.getInventorySlots()) {
				if (slot.mouseInBounds(mouseX, mouseY)) {
					return;
				}
			}

			GameStateManager.gameClient.sendPacket(new PlayerShootAction(xPos,
					yPos));
		}
	}

	/**
	 * Sets a gameplay message to be displayed on the hud.
	 *
	 * @param message
	 *            the message to display
	 */
	public static void sendMessage(final String message) {
		if (!message.trim().equals("")) {
			messages.add(new MessageRecord(message));
			if (messages.size() > 15)
				messages.remove(0); // history up to 15 messages
		}
	}

	@Override
	public int getID() {
		return state;
	}
}
