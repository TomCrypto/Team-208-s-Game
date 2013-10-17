package network;

import java.io.*;
import java.util.zip.*;

import ecs.world.*;
import ecs.entity.*;

import network.packet.*;
import network.packet.transport.*;

import log.Log;
import naga.*;

/**
 * This represents a  player instance bound
 * to a network  socket. It does <b>not</b>
 * represent  a player entity, though every
 * client should have only one.
 *
 * @author Thomas Beneteau (300250968)
 */
public class NetClient
{
	private static final String COMPONENT = "Network Client";

	/**
	 * This  is  the  client's  transport-layer
	 * network packet reader (packet decoder).
	 */
	private final TransportPacketReader reader = new TransportPacketReader();

	/**
	 * This  is  the  client's  transport-layer
	 * network packet writer (packet encoder).
	 */
	private final TransportPacketWriter writer = new TransportPacketWriter();

	private final NIOSocket socket;

	private Entity playerEntity;

	private String name = null;
	private Location location = null;

	private boolean locationChanged = false;
	private Location newLocation;
	
	/**
	 * Dummy implementation of the NetClient for test purposes.
	 */
	public NetClient(String name, Location location)
	{
		this.socket = null;
		this.name = name;
		this.location = location;
	}

	public NetClient(NIOSocket socket)
	{
		socket.setPacketWriter(writer);
		socket.setPacketReader(reader);
		this.socket = socket;
	}

	/**
	 * Returns the client's IP address.
	 */
	public String getAddress()
	{
		return socket.getIp();
	}

	/**
	 * Disconnects the client from the server.
	 */
	public void disconnect()
	{
		socket.close();
	}

	/**
	 * Sets the client's login name.
	 */
	public void setName(String playerName)
	{
		if (playerName == null) throw new IllegalArgumentException("Player name cannot be null.");
		if (isLoggedIn()) throw new IllegalStateException("Client already logged in, cannot change name.");
		name = playerName;
	}

	/**
	 * Returns the client's login name, or
	 * throws an exception if the client is
	 * not logged in.
	 */
	public String getName()
	{
		if (!isLoggedIn()) throw new IllegalStateException("Client not logged in.");
		return name;
	}

	/**
	 * Associates a player entity to a client.
	 */
	public void setPlayerEntity(Entity entity)
	{
		playerEntity = entity;
	}

	/**
	 * Whether the client has an associated
	 * player entity.
	 */
	public boolean hasPlayerEntity()
	{
		return playerEntity != null;
	}

	/**
	 * Whether the player has changed location
	 * this server tick.
	 */
	public boolean locationChanged()
	{
		return locationChanged;
	}

	/**
	 * Moves the player to the location it has
	 * changed to, and discards its previous
	 * location.
	 */
	public void updateLocation()
	{
		newLocation.addEntity(playerEntity);
		this.location = newLocation;
		locationChanged = false;
		newLocation = null;
	}

	/**
	 * Indicates the player needs to move to
	 * another location on the next tick.
	 */
	public void changeLocation(Location location)
	{
		locationChanged = true;
		newLocation = location;
		if (this.location != null) playerEntity.setRemoved();
	}

	/**
	 * Returns the player's current location.
	 */
	public Location getLocation()
	{
		return location;
	}

	/**
	 * Retrieves  the player  entity associated
	 * with  this  client,  regardless  of  its
	 * location.
	 */
	public Entity getPlayer()
	{
		return playerEntity;
	}

	/**
	 * Whether the player is logged in.
	 */
	public boolean isLoggedIn()
	{
		return (name != null);
	}

	/**
	 * Returns the average network throughput
	 * from the client to the server.
	 */
	public double getToThroughput()
	{
		return writer.getThroughput();
	}

	/**
	 * Returns the average network throughput
	 * from the server to the client.
	 */
	public double getFromThroughput()
	{
		return reader.getThroughput();
	}

	/**
	 * Returns a formatted representation of the client.
	 */
	@Override
	public String toString()
	{
		if (isLoggedIn()) return String.format("[%s -- %s]", getName(), getAddress());
		else return String.format("[%s]", getAddress());
	}

	/**
	 * Utility  method which  encodes a  {@code
	 * NetPacket}  into  a (short)  byte  array
	 * using serialization,  and then  sends it
	 * over the  network to be received  by the
	 * remote host.
	 * <p>
	 * Compresses outgoing packets.
	 *
	 * @param packet The packet to send.
	 */
	public void sendPacket(NetPacket packet)
	{
		try
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			GZIPOutputStream zip = new GZIPOutputStream(bytes);
			ObjectOutputStream stream = new ObjectOutputStream(zip);
			stream.writeObject(packet);
			stream.close();
			zip.close();

			socket.write(bytes.toByteArray());
			bytes.close();
		}
		catch (Exception error)
		{
			Log.warning(error, COMPONENT, "Serialization error.");
		}
	}

	/**
	 * Utility  method  which   decodes  a  raw
	 * network   packet    into   a   NetPacket
	 * instance, to be parsed and dispatched by
	 * the client or server.
	 * <p>
	 * Decompresses incoming packets.
	 *
	 * @param data The raw network data.
	 *
	 * @return Returns a {@code NetPacket}.
	 */
	public static NetPacket decodePacket(byte[] data)
	{
		try
		{
			ByteArrayInputStream bytes = new ByteArrayInputStream(data);
			GZIPInputStream zip = new GZIPInputStream(bytes);
			ObjectInputStream stream = new ObjectInputStream(zip);
			NetPacket packet = (NetPacket)stream.readObject();
			stream.close();
			return packet;
		}
		catch (Exception error)
		{
			Log.warning(error, COMPONENT, "Serialization error.");
		}

		return null;
	}

	/**
	 * Returns whether  the passed  player name
	 * is valid and can be used to log in.
	 *
	 * @return  Returns  {@code  true}  if  the
	 * player name is valid.
	 */
	public static boolean isNameValid(String name)
	{
		return (name != null)
			&& (name.length() >= 3) && (name.length() <= 16)			// Length constraints
			&& (!name.contains("\n")) && (!name.contains(":"));			// Special characters
	}
}
