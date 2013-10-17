package network.server.back_end;

import java.io.*;
import java.net.*;
import java.util.*;

import ecs.world.*;
import ecs.entity.*;
import ecs.components.*;

import network.*;
import network.server.*;
import network.packet.*;
import network.packet.message.*;
import network.packet.message.LoginMessage.*;

import log.Log;
import naga.*;

/**
 * Server back-end, receiving messages from
 * clients and sending back game state.
 *
 * @author Thomas Beneteau (300250968)
 */
public class GameServer implements Configurable
{
	private static final String COMPONENT = "Network Back-end";

	/**
	 * The   network  service,   which  manages
	 * most  of the  low-level network  details
	 * involved in running the server.
	 */
	private final NIOService service;

	/**
	 * The main server network socket.
	 */
	private final NIOServerSocket serverSocket;

	/**
	 * The server configuration in use.
	 */
	private final Configuration config;

	/**
	 * The  client list,  which should  be used
	 * when  operations  relating to  currently
	 * connected clients  (network-wise) are to
	 * be carried out.
	 */
	private final ClientList clientList;

	/**
	 * The world  state, which holds  the state
	 * of the  entire game world  and processes
	 * client actions in order  for the game to
	 * move forward.
	 */
	private final WorldState worldState;

	@Override
	public void validate(Configuration config) throws IllegalArgumentException
	{
		if ((config.getBufferSize() < 1) || (config.getBufferSize() > 32768))
			throw new IllegalArgumentException("Invalid TCP buffer size.");

		if (config.getMaxPlayers() < 1)
			throw new IllegalArgumentException("Server requires at least one player");
	}

	public GameServer(Configuration config) throws IOException
	{
		this.config = config;
		validate(config);

		service = new NIOService(config.getBufferSize() * 1024);
		serverSocket = service.openServerSocket(config.getPort());

		ServerObserver observer = new ServerObserver();
		serverSocket.setConnectionAcceptor(observer);
		service.setExceptionObserver(observer);
		ServiceLoop.execute(service, observer);

		clientList = new ClientList(config.getMaxPlayers(), observer);
		worldState = new WorldState(new ConcreteWorldObserver(), config);

		/* Clients can now connect. */
		serverSocket.listen(observer);

		Log.info(COMPONENT, "Listening on port %d.", config.getPort());
	}

	/**
	 * This method  applies a client  filter to
	 * the currently connected clients.
	 *
	 * @param filter The filter to apply.
	 *
	 * @see {@link network.server.back_end.ClientList.ClientTask}
	 */
	public void clientFilter(ClientList.ClientTask filter)
	{
		clientList.filter(filter);
	}

	/**
	 * This  method  can  be used  to  get  the
	 * current number  of clients  connected to
	 * the game server.
	 *
	 * @return Returns the  number of connected
	 * clients.
	 */
	public int getClientCount()
	{
		return clientList.getClientCount();
	}

	/**
	 * This  method exposes  the game  server's
	 * configuration  (which  is immutable)  in
	 * order for  other components to  make use
	 * of it, such as  displaying it or logging
	 * it to a file.
	 *
	 * @return  Returns  the  currently  active
	 * server configuration.
	 */
	public Configuration getConfiguration()
	{
		return config;
	}
	
	/**
	 * This  method exposes  the current  world
	 * state,  useful  for the  server  control
	 * panel to  display some  statistics about
	 * the game world and its players.
	 * 
	 * @return Returns the world state.
	 */
	public WorldState getWorldState()
	{
		return worldState;
	}

	/**
	 * Terminates the game  server. This should
	 * disconnect  all  players and  cause  the
	 * data storage  package to save  the world
	 * back to disk (if it hasn't already).
	 *
	 * @param   failed   Whether   the   server
	 * terminated due to an error.
	 */
	public void close(boolean failed) throws InterruptedException
	{
		if (failed) Log.warning(COMPONENT, "Emergency shutdown.");
		else Log.info(COMPONENT, "Shutting down gracefully.");

		service.close();
		worldState.close(failed);
	}

	/**
	 * This   inner   class  is   an   observer
	 * for  the   server  socket   and  handles
	 * administrative  tasks such  as accepting
	 * client  connections  and logging  server
	 * errors.
	 */
	private class ServerObserver implements ConnectionAcceptor, ServerSocketObserver, ExceptionObserver, LoginObserver
	{
		/**
		 * Called  when  a connection  request  has
		 * been  received  from  a  player  and  is
		 * pending. This is where the server should
		 * either accept or deny the player access.
		 *
		 * @param address The player's address.
		 */
		@Override
		public boolean acceptConnection(InetSocketAddress address)
		{
			String host = address.getAddress().getHostAddress();

			if (getClientCount() < config.getMaxPlayers())
			{
				if (!config.getBlacklist().contains(host)) return true;
				Log.info("Client %s is on the blacklist.", host);
			}

			return false;
		}

		/**
		 * Called  when  a   player  has  attempted
		 * to  connect to  the  server. The  player
		 * isn't  added just  yet  to the  server's
		 * player list,  this will happen  when the
		 * connection is established.
		 *
		 * @param socket The network socket.
		 */
		@Override
		public void newConnection(NIOSocket socket)
		{
			socket.setMaxQueueSize(service.getBufferSize());
			socket.listen(new ClientObserver());
		}

		/**
		 * Called when a client tries to log in.
		 *
		 * @param client The client instance.
		 * @param status The login status.
		 */
		@Override
		public void loginProcessed(NetClient client, LoginStatus status)
		{
			switch (status)
			{
			case LOGGED_IN:
				Log.info(COMPONENT, "%s has logged in.", client);
				worldState.addPlayer(client);
				break;

			case ALREADY_LOGGED_IN:
				Log.info(COMPONENT, "%s tried to log in multiple times.", client);
				break;

			case INVALID_NAME:
				Log.info(COMPONENT, "%s tried to log in, name was invalid.", client);
				break;

			case NAME_TAKEN:
				Log.info(COMPONENT, "%s tried to log in, name was taken.", client);
				break;

			default:
				Log.warning(COMPONENT, "Unexpected login status %s for %s.", status, client);
			}
		}

		/**
		 * Called when the server socket was closed
		 * for some reason.
		 *
		 * @param error The cause exception.
		 */
		@Override
		public void serverSocketDied(Exception error)
		{
			if (error == null) Log.info(COMPONENT, "Network service closed.");
			else Log.severe(error, COMPONENT, "The server socket failed.");
		}

		/**
		 * Called   as  a   notification  when   an
		 * exception occurs.
		 *
		 * @param exception The exception.
		 */
		@Override
		public void notifyExceptionThrown(Throwable exception)
		{
			Log.warning(exception, COMPONENT, "Exception caught.");
		}

		@Override
		public void acceptFailed(IOException e)
		{
			return;
		}
	}

	/**
	 * This  inner  class handles  world  state
	 * updates by  providing a listener  to the
	 * world  state object  to  call back  when
	 * clients need to be updated.
	 */
	private class ConcreteWorldObserver implements WorldObserver
	{
		@Override
		public void commitWorldState(final World world)
		{
			for (Location location : world.getLocations())
			{
				for (Entity entity : location.clearRemovedEntities())
					clientList.broadcastPacket(location, UpdatePacket.entityDeleted(entity));

				for (Entity entity : location.getEntities())
				{
					/* Get its removed and modified components. */
					Set<Class<? extends Component>> removedComponents = entity.clearRemovedComponents();
					Set<Component> modifiedComponents = entity.clearModifiedComponents();

					if ((removedComponents.size() > 0) || (modifiedComponents.size() > 0))
						clientList.broadcastPacket(location, UpdatePacket.componentsUpdated(entity,
															 modifiedComponents, removedComponents));
				}
			}

			clientList.filter(new ClientList.ClientTask()
			{
				@Override
				public boolean nextClient(NetClient client)
				{
					if (client.locationChanged())
					{
						Location oldLocation = client.getLocation();
						client.updateLocation();

						client.sendPacket(new LocationChangeMessage(client.getLocation()));

						/* Remove player from his previous location. */
						if (oldLocation != null) clientList.broadcastPacket(oldLocation, UpdatePacket.entityDeleted(client.getPlayer()));
						
						/* And add player to his new location. */
						clientList.broadcastPacket(client.getLocation(), UpdatePacket.entityCreated(client.getPlayer()));
					}

					return true;
				}
			});
		}
	}

	/**
	 * This inner class handles a single client
	 * and is responsible for handling any game
	 * packets it sends to  the server, as well
	 * as  informing the  server of  any errors
	 * (such as unexpected disconnections).
	 */
	private class ClientObserver implements SocketObserver
	{
		/**
		 * Called when the connection with a player
		 * has   been  finalized.   The  (currently
		 * unnamed) player  should be added  to the
		 * server's player list at this point.
		 *
		 * @param  socket The  socket to  associate
		 * with the new player.
		 */
		@Override
		public void connectionOpened(NIOSocket socket)
		{
			NetClient client = new NetClient(socket);
			clientList.addClient(socket, client);
			Log.info(COMPONENT, "Client %s connected.", client);
		}

		/**
		 * Called when the connection with a player
		 * is lost.
		 *
		 * @param   socket   The  socket   of   the
		 * disconnected player.
		 * @param e The exception  which led to the
		 * disconnection (may be {@code null} if no
		 * error occurred,  e.g. the  server called
		 * the socket's {@code close()} method).
		 */
		@Override
		public void connectionBroken(NIOSocket socket, Exception e)
		{
			NetClient client = clientList.removeClient(socket);
			if (client.isLoggedIn()) worldState.removePlayer(client);

			if (e != null) Log.info(e, COMPONENT, "Lost connection with %s.", client);
			else Log.info(COMPONENT, "Disconnected %s.", client);
		}

		/**
		 * Called when  a raw network  packet (i.e.
		 * just  above  the   transport  layer)  is
		 * received  from  a  player.  This  packet
		 * needs  to be  decoded and  dispatched to
		 * the relevant method.
		 *
		 * @param socket The source socket.
		 * @param data The raw packet data.
		 */
		@Override
		public void packetReceived(NIOSocket socket, byte[] data)
		{
			NetPacket packet = NetClient.decodePacket(data);
			NetClient client = clientList.getClient(socket);

			if ((packet != null) && (packet.getType() != null) && (client != null))
			{
				switch (packet.getType())
				{
				case MESSAGE:
					clientList.processMessage(client, (MessagePacket)packet);
					break;

				case ACTION:
					if (client.isLoggedIn()) worldState.processAction(client, (ActionPacket)packet);
					else Log.warning(COMPONENT, "Client %s issued action before logging in.", client);
					break;

				default:
					Log.warning(COMPONENT, "Received invalid packet from %s (%s).", client, packet.getType());
				}
			}
		}

		@Override
		public void packetSent(NIOSocket socket, Object tag)
		{
			return;
		}
	}
}
