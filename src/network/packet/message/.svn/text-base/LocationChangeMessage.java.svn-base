package network.packet.message;

import ecs.world.*;

import network.packet.MessagePacket;

/**
 * Sent when a player has changed location.
 */
public class LocationChangeMessage implements MessagePacket
{
	private static final long serialVersionUID = 1L;

	@Override
	public Type getType()
	{
		return Type.MESSAGE;
	}

	@Override
	public Channel getChannel()
	{
		return Channel.SERVER;
	}

	public final Location newLocation;

	public LocationChangeMessage(Location newLocation)
	{
		this.newLocation = newLocation;
	}
}
