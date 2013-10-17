package network.client.observers;

import ecs.world.*;

import network.packet.*;

/**
 * This observer listens to game events.
 *
 * @author Thomas Beneteau (300250968)
 */
public interface GameObserver
{
	public void updatePacket(UpdatePacket packet);

	public void locationChangePacket(Location location);
}
