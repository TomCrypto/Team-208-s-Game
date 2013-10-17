package network.client;

import java.io.*;

import ecs.entity.*;
import ecs.world.*;
import ecs.components.*;
import ecs.components.Type.*;

import network.*;
import network.packet.*;
import network.packet.message.*;
import network.packet.message.LoginMessage.*;

import log.Log;
import naga.*;

/**
 * Implements  a  game client,  which  lets
 * users interact with the game server in a
 * controlled way.
 *
 * @author Thomas Beneteau (300250968)
 */
public class GameClient
{
	private static final String COMPONENT = "Game Client";

	private final ClientObserver clientObserver;
	private final PlayerObserver observer;
	private final NIOService service;
	private NIOSocket socket;
	private NetClient client;

	public GameClient(ClientObserver clientObserver) throws IOException
	{
		service = new NIOService(ClientDefaults.BUFFER_SIZE);
		PlayerObserver observer = new PlayerObserver();
		service.setExceptionObserver(observer);
		ServiceLoop.execute(service, observer);
		this.clientObserver = clientObserver;
		this.observer = observer;
	}

	/**
	 * Connects the game client to a server.
	 */
	public void connect(String host, int port) throws IOException
	{
		if ((socket != null) && (socket.isOpen())) throw new IllegalStateException("Client already connected.");
		socket = service.openSocket(host, port);
		socket.setMaxQueueSize(service.getBufferSize());
		client = new NetClient(socket);
		socket.listen(observer);
	}

	/**
	 * Terminates the game client.
	 */
	public void close()
	{
		service.close();
	}

	/**
	 * Returns the NetClient instance associated
	 * with this game client.
	 */
	public NetClient getClient()
	{
		return client;
	}

	/**
	 * Sends a login request to the server.
	 */
	public void sendLogin(LoginMessage login)
	{
		client.sendPacket(login);
	}

	/**
	 * Sends a player action to the server.
	 */
	public void sendAction(ActionPacket packet)
	{
		client.sendPacket(packet);
	}

	/**
	 * Sends a generic packet to the server.
	 */
	public void sendPacket(NetPacket packet)
	{
		client.sendPacket(packet);
	}

	/**
	 * Returns the player's location. If returns
	 * null, then the server has not yet sent the
	 * location to us (print "Loading" or something).
	 */
	public Location getCurrentLocation()
	{
		synchronized (client)
		{
			return client.getLocation();
		}
	}

	/**
	 * Returns the player entity, or null if the
	 * location is null.
	 */
	public Entity getPlayerEntity()
	{
		synchronized (client)
		{
			return client.getPlayer();
		}
	}

	private class PlayerObserver implements SocketObserver, ExceptionObserver
	{
		@Override
		public void connectionOpened(NIOSocket socket)
		{
			return;
		}

		@Override
		public void connectionBroken(NIOSocket socket, Exception e)
		{
			clientObserver.general.connectionLost(e);
		}

		@Override
		public void packetReceived(NIOSocket socket, byte[] data)
		{
			NetPacket packet = NetClient.decodePacket(data);

			if ((packet != null) && (packet.getType() != null))
			{
				switch (packet.getType())
				{
				case MESSAGE:
					dispatchMessage((MessagePacket)packet);
					break;

				case UPDATE:
					handleUpdate((UpdatePacket)packet);
					break;

				default:
					/* Ignore any other packets from the server. */
					break;
				}
			}
		}

		@Override
		public void packetSent(NIOSocket socket, Object data)
		{
			return;
		}

		@Override
		public void notifyExceptionThrown(Throwable error)
		{
			Log.warning(error, COMPONENT, "Exception caught.");
		}
	}

	/* Various message handlers below which parse messages into relevant game events. */

	private void dispatchMessage(MessagePacket packet)
	{
		switch (packet.getChannel())
		{
			case SERVER:
				if (packet instanceof LoginMessage) handleLoginMessage((LoginMessage)packet);
				if (packet instanceof LocationChangeMessage) handleLocationChangeMessage((LocationChangeMessage)packet);
				break;

			case PUBLIC:
				handlePublicMessage((PublicMessage)packet);
				break;

			case PRIVATE:
				handlePrivateMessage((PrivateMessage)packet);
				break;
		}
	}

	private void handleLoginMessage(LoginMessage message)
	{
		if (message.getLoginStatus() == LoginStatus.LOGGED_IN)
		{
			client.setName(message.getPlayerName());
			clientObserver.login.loginSucceeded(client);
		}
		else clientObserver.login.loginFailed(client, message.getLoginStatus());
	}

	private void handleLocationChangeMessage(LocationChangeMessage message)
	{
		synchronized(client)
		{
			for (Entity entity : message.newLocation.getEntities())
				if ((entity.getType() == EntityType.PLAYER) && (entity.getName().equals(client.getName())))
				{
					client.setPlayerEntity(entity);
					System.out.println(entity);
					break;
				}

			client.changeLocation(message.newLocation);
			client.updateLocation();
		}
	}

	private void handlePublicMessage(PublicMessage message)
	{
		clientObserver.chat.publicMessageReceived(message.getSource(), message.getMessage());
	}

	private void handlePrivateMessage(PrivateMessage message)
	{
		clientObserver.chat.privateMessageReceived(message.getSource(), message.getMessage());
	}

	private void handleUpdate(UpdatePacket packet)
	{
		Location location = client.getLocation();

		if (location == null)
		{
			System.out.println("Got update packet, but no location to update!");
			return;
		}

		synchronized (location)
		{
			switch (packet.kind)
			{
				case SUBMIT:
					/* If the entity doesn't exist, add it now. */
					if (location.getEntity(packet.entityID) == null)
					{
						location.addEntity(new Entity(packet.entityID));
					}

					Entity entity = location.getEntity(packet.entityID);

					if (entity.getID() == client.getPlayer().getID())
						client.setPlayerEntity(entity);

					if (packet.modified != null)
					{
						for (Component component : packet.modified)
							entity.addComponent(component);
					}

					if (packet.removed != null)
					{
						for (Class<? extends Component> componentClass : packet.removed)
							entity.removeComponent(componentClass);
					}

					break;

				case DELETE:
					location.removeEntity(packet.entityID);
					break;
			}
		}
	}
}
