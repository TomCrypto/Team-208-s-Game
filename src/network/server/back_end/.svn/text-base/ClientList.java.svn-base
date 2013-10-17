package network.server.back_end;

import java.util.*;

import ecs.world.*;

import network.*;
import network.packet.*;
import network.packet.message.*;
import network.packet.message.LoginMessage.*;

import log.Log;
import naga.*;

/**
 * This  class   keeps  track  of   who  is
 * connected  to the  server, and  provides
 * methods to  add, remove, or  search over
 * connected clients.
 * <p>
 * This  class  is   also  responsible  for
 * handling player logins and notifying the
 * server.
 *
 * @author Thomas Beneteau (300250968)
 */
public class ClientList
{
	private final static String COMPONENT = "Client List";

	/**
	 * This maps a network  socket to  a {@code
	 * NetClient}   instance,  to   efficiently
	 * associate  network  packets to  a  given
	 * client.
	 */
	private final Map<NIOSocket, NetClient> clientMap;

	/**
	 * This  login  observer will  be  notified
	 * whenever a client  sends a login request
	 * to  the server  (whether it  succeeds or
	 * not).
	 */
	private final LoginObserver loginObserver;

	/**
	 * Creates  a client  list  with a  maximum
	 * number of connected clients.
	 *
	 * @param  maxClients  The  maximum  number
	 * of clients  which can  be simultaneously
	 * connected to the server.
	 * @param loginObserver  The login observer
	 * to  notify  when  players log  into  the
	 * server.
	 */
	public ClientList(int maxClients, LoginObserver loginObserver)
	{
		Log.info(COMPONENT, "Ready (%d clients maximum).", maxClients);
		clientMap = new HashMap<NIOSocket, NetClient>(maxClients);
		this.loginObserver = loginObserver;
	}

	/**
	 * This method  returns a read-only  map of
	 * the  currently  connected  players  with
	 * their  respective network  sockets. This
	 * map reflects the active server state.
	 */
	public synchronized Map<NIOSocket, NetClient> getClients()
	{
		return Collections.unmodifiableMap(clientMap);
	}

	/**
	 * This method adds a  client to the client
	 * list.
	 *
	 * @param socket The client socket.
	 * @param client The network client.
	 */
	public synchronized void addClient(NIOSocket socket, NetClient client)
	{
		clientMap.put(socket, client);
	}

	/**
	 * This  method removes  a client  from the
	 * client list.
	 * <p>
	 * Note   this   method   does   <b>not</b>
	 * disconnect the client (in fact, the game
	 * server  should  call  this  method  when
	 * it detects  a client  has disconnected).
	 * To   disconnect   a   client,   call   a
	 * {@code NetClient}'s {@code disconnect()}
	 * method.
	 *
	 * @param socket The client socket.
	 *
	 * @return Returns the network client which
	 * was just removed.
	 */
	public synchronized NetClient removeClient(NIOSocket socket)
	{
		return clientMap.remove(socket);
	}

	/**
	 * This method looks up a network client.
	 *
	 * @param socket A client socket.
	 *
	 * @return   Returns  the   network  client
	 * associated with this client socket.
	 */
	public synchronized NetClient getClient(NIOSocket socket)
	{
		return clientMap.get(socket);
	}

	/**
	 * This  method   returns  the   number  of
	 * currently connected clients.
	 *
	 * @return Returns the  number of connected
	 * clients.
	 */
	public synchronized int getClientCount()
	{
		return clientMap.size();
	}

	/**
	 * This interface  is used to  iterate over
	 * currently connected  clients and execute
	 * custom  tasks   (such  as  disconnecting
	 * clients with a given IP, etc...).
	 */
	public interface ClientTask
	{
		/**
		 * This  method is  called  once for  every
		 * network client connected to the server.
		 * <p>
		 * To   disconnect  a   client,  call   its
		 * {@code disconnect()}  method, not {@code
		 * removeClient()}.
		 * <p>
		 * If this method returns {@code false}, no
		 * further calls  will be  made. Otherwise,
		 * this method  will be executed  for every
		 * client - this is to help in implementing
		 * efficient decisional tasks.
		 *
		 * @param client The next network client.
		 *
		 * @return Whether to continue iterating.
		 */
		public boolean nextClient(NetClient client);
	}

	/**
	 * This  method  iterates  a  given  {@code
	 * ClientTask}  over  the set of  currently
	 * connected clients.
	 *
	 * @param task The task to run.
	 *
	 * @return  If  the task  was  interrupted,
	 * returns  the  {@code  NetClient}  during
	 * the  processing of  which  the task  was
	 * interrupted.  Otherwise, returns  {@code
	 * null}.
	 */
	public synchronized NetClient filter(ClientTask task)
	{
		if (!clientMap.isEmpty())
			for (Map.Entry<NIOSocket, NetClient> entry : clientMap.entrySet())
				if (!task.nextClient(entry.getValue())) return entry.getValue();

		return null;
	}

	/**
	 * This method broadcasts  a network packet
	 * to every client currently connected, and
	 * in a given world location.
	 * <p>
	 * If {@code location} is {@code null} then
	 * broadcasts to every connected client.
	 *
	 * @param location The world location.
	 * @param packet The network packet.
	 */
	public synchronized void broadcastPacket(final Location location, final NetPacket packet)
	{
		filter(new ClientTask()
		{
			@Override
			public boolean nextClient(NetClient client)
			{
				if ((client.isLoggedIn()) && (location == null) || (client.getLocation() == location)) client.sendPacket(packet);

				return true;
			}
		});
	}

	/**
	 * This  method  is  called when  the  game
	 * server receives a  Message packet from a
	 * given player.  The method  should decode
	 * the  message and  perform the  necessary
	 * actions.
	 *
	 * @param client The source client.
	 * @param message The message packet.
	 */
	public synchronized void processMessage(NetClient client, MessagePacket message)
	{
		switch (message.getChannel())
		{
			case SERVER:
				handleServerMessage(client, message);
				break;
			case PRIVATE:
				handlePrivateMessage(client, (PrivateMessage)message);
				break;
			case PUBLIC:
				handlePublicMessage(client, (PublicMessage)message);
				break;
			default:
				Log.warning(COMPONENT, "Received invalid message packet from %s (%s).", client, message.getChannel());
		}
	}

	private void handleServerMessage(NetClient client, MessagePacket packet)
	{
		if (packet instanceof LoginMessage) handleLoginMessage(client, (LoginMessage)packet);
	}

	private void handleLoginMessage(NetClient client, LoginMessage message)
	{
		if (client.isLoggedIn()) message.setLoginStatus(LoginStatus.ALREADY_LOGGED_IN);
		else
		{
			if (!NetClient.isNameValid(message.getPlayerName())) message.setLoginStatus(LoginStatus.INVALID_NAME);
			else if (!isNameAvailable(message.getPlayerName()))  message.setLoginStatus(LoginStatus.NAME_TAKEN);
			else
			{
				/* Name is valid and available, login accepted. */
				message.setLoginStatus(LoginStatus.LOGGED_IN);
				client.setName(message.getPlayerName());
			}
		}

		client.sendPacket(message);
		loginObserver.loginProcessed(client, message.getLoginStatus());
	}

	private void handlePrivateMessage(NetClient client, PrivateMessage message)
	{
		NetClient destination = findClient(message.getRecipient());
		message.setSource(client.getName());

		if (destination == null) client.sendPacket(message);
		else destination.sendPacket(message);
	}

	private void handlePublicMessage(final NetClient client, final PublicMessage message)
	{
		message.setSource(client.getName());
		broadcastPacket(client.getLocation(), message);
	}

	/**
	 * This utility  method returns  the {@code
	 * NetClient}  instance  associated with  a
	 * given player name.
	 *
	 * @param name The player name to search.
	 *
	 * @return  Returns the  appropriate {@code
	 * NetClient} instance, or  {@code null} if
	 * no client has this name.
	 */
	private synchronized NetClient findClient(final String name)
	{
		return filter(new ClientTask()
		{
			@Override
			public boolean nextClient(NetClient client)
			{
				if ((client.isLoggedIn()) && (client.getName().equals(name)))
					return false; /* Stop here, we found the client. */
				else
					return true; /* Keep going. */
			}
		});
	}

	/**
	 * This method  checks if a player  name is
	 * already taken, or if it is available.
	 *
	 * @param name The player name to check.
	 *
	 * @return Returns {@code true} if the name
	 * is available.
	 */
	private synchronized boolean isNameAvailable(final String name)
	{
		return (findClient(name) == null);
	}
}
