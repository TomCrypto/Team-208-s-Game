package network.server.back_end.logic_handlers;

import network.NetClient;
import network.packet.action.MovementAction;
import ecs.entity.Entity;
import ecs.world.Location;
import ecs.world.World;

public class MovementHandler {

	public static void handleEvent(NetClient client, MovementAction movement, World world) {
		int x = 0;
		int y = 0;
		switch (movement.getDir())
		{
		case UP:
			y = -1;
			break;
		case DOWN:
			y = 1;
			break;
		case LEFT:
			x = -1;
			break;
		case RIGHT:
			x = 1;
		}

		Entity player = client.getPlayer();

		movePlayer(world, client.getLocation(), x, y, player);
	}

	private static void movePlayer(World world, Location location, int x, int y, Entity player)
	{
		if (world.getPhysics().hasBody(location, player)) // are you dead? (improve this eventually)
		{
			world.getPhysics().displaceEntity(location, player, x * 0.001f, y * 0.001f);
		}
	}

}
