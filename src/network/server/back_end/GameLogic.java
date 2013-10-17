package network.server.back_end;

import ecs.world.*;
import ecs.entity.*;
import ecs.helpers.*;
import ecs.components.*;

import network.*;
import network.packet.*;
import network.packet.action.*;
import network.server.back_end.logic_handlers.*;

import log.Log;

public class GameLogic
{
	public static final String COMPONENT = "Game Logic";

	/**
	 * Represents  an   action  from   a  given
	 * client. The game logic will handle these
	 * actions.
	 */
	public static class ClientAction
	{
		public final ActionPacket packet;
		public final NetClient client;

		public ClientAction(NetClient client, ActionPacket packet)
		{
			this.client = client;
			this.packet = packet;
		}
	}
	
	private static void spawnDefaultPlayer(NetClient client, World world)
	{
		Location location = world.getStartLocation();

		Entity player = EntityFactory.genPlayerEntity(client.getName());
		Entity shotgun = EntityFactory.genDefaultWeapon("Shotgun");

		location.addEntity(shotgun);  //Default gun
		// adds to location, but no position so won't display
		(player.getComponent(Equipped.class)).equip(shotgun);

		client.setPlayerEntity(player);
		client.changeLocation(location);
	}

	/**
	 * Called when a client logs into the game.
	 */
	public static void addClient(NetClient client, World world)
	{
		if (!world.loadPlayer(client)) spawnDefaultPlayer(client, world);
	}

	/**
	 * Called when a client disconnects from the server.
	 */
	public static void removeClient(NetClient client, World world)
	{
		world.savePlayer(client);
	}

	/**
	 * This "master  method" dispatches actions
	 * to the appropriate player action handler
	 * depending on the action type.
	 */
	public static void processAction(ClientAction action, World world)
	{
		// game logic goes here (literally)

		switch (action.packet.getActionType())
		{
			case MOVEMENT:
				MovementHandler.handleEvent(action.client, (MovementAction)action.packet, world);
				break;
			case INTERACT:
				InteractionHandler.handleEvent(action.client, (InteractAction)action.packet, world);
				break;
			case PLAYER_SHOOT:
				ShootingHandler.handleEvent(action.client, (PlayerShootAction)action.packet, world);
				break;
			case USE:
				InventoryHandler.handleEvent(action.client, action.packet, world);
				break;
			case DROP:
				InventoryHandler.handleEvent(action.client, action.packet, world);
				break;
			case UPGRADE:
				UpgradeHandler.handleEvent(action.client, (UpgradeAction)action.packet, world);
				break;
			default:
				Log.warning(COMPONENT, "Unknown action by %s (%s).", action.client, action.packet.getActionType());
		}
	}
}

