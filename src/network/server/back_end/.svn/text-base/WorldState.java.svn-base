package network.server.back_end;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import ecs.world.*;
import ecs.entity.*;
import ecs.components.Type.*;

import network.*;
import network.server.*;
import network.packet.*;
import network.server.back_end.GameLogic.*;

import dataStorage.*;
import log.*;

/**
 * This class holds  references to the game
 * states of  every location in  the world,
 * and is responsible for processing player
 * actions as well as issuing updates.
 */
public class WorldState implements Configurable
{
	public static final String COMPONENT = "World State";

	private class ServerTick implements Runnable
	{
		private final ConcurrentLinkedQueue<ClientAction> actions;
		private final ConcurrentLinkedQueue<NetClient> removed;
		private final ConcurrentLinkedQueue<NetClient> added;

		private final double updateRate;
		private final int networkTicks;
		private final long startTime;
		private long tick = 0;

		public ServerTick(double updateRate, int networkTicks)
		{
			actions = new ConcurrentLinkedQueue<ClientAction>();
			removed = new ConcurrentLinkedQueue<NetClient>();
			added = new ConcurrentLinkedQueue<NetClient>();

			startTime = System.currentTimeMillis();
			this.networkTicks = networkTicks;
			this.updateRate = updateRate;
		}

		/**
		 * Adds an action to the action queue.
		 * 
		 * @param action The action to submit.
		 */
		public void submitAction(ClientAction action)
		{
			actions.offer(action);
		}

		/**
		 * Adds a new client to the client queue.
		 * 
		 * @param client The client to add.
		 */
		public void addClient(NetClient client)
		{
			added.offer(client);
		}

		/**
		 * Adds a disconnected client to the client queue.
		 * 
		 * @param client The client to add.
		 */
		public void removeClient(NetClient client)
		{
			removed.offer(client);
		}

		/**
		 * Main loop, performs game logic tasks and maintains
		 * the game state and sends it to the clients.
		 */
		@Override
		public void run()
		{
			try
			{
				synchronized(world)
				{
					/* We need to handle new players a bit differently, as
					 * they need to be sent the state of the location they
					 * are currently in. This happens after the game logic
					 * has decided what to do with them. */
					Set<NetClient> newPlayers = new HashSet<NetClient>();

					while (!added.isEmpty())
					{
						NetClient newPlayer = added.poll();
						newPlayers.add(newPlayer);

						/* Here the game logic should add the
						 * player to some location, and maybe
						 * give him starting items and stuff. */
						GameLogic.addClient(newPlayer, world);
					}

					while (!removed.isEmpty()) GameLogic.removeClient(removed.poll(), world);

					/* This is because until the end of this tick, new players *have* no actual
					 * location, so they cannot issue any action yet (next tick they will). */
					while (!actions.isEmpty())
					{
						ClientAction action = actions.poll();
						if (!action.client.locationChanged())
						{
							GameLogic.processAction(action, world);
						}
					}

					for(Location location : world.getLocations())
						world.process(location, updateRate);

					/* Commit the world state to the clients every now and then. */
					if ((tick++) % networkTicks == 0) observer.commitWorldState(world);
				}
			}
			catch (Exception error)
			{
				Log.severe(error, COMPONENT, "Game logic loop encountered an error, attempting to recover.");
			}
		}
	}

	private final ScheduledExecutorService gameLogicScheduler;
	private final WorldObserver observer;
	private final ServerTick logic;

	private final File worldStateFile;
	private World world;

	@Override
	public void validate(Configuration config)
	{
		if (config.getUpdateRate() <= 0) throw new IllegalArgumentException("Update rate cannot be zero or negative.");
		if (config.getNetworkTicks() <= 0) throw new IllegalArgumentException("Network ticks cannot be zero or negative.");
	}

	public WorldState(WorldObserver observer, Configuration config)
	{
		validate(config);

		worldStateFile = new File(config.getWorldStateFile());

		try
		{
			world = DataStorage.loadFile(worldStateFile);
			System.out.println("LOADING WORKED");
		}
		catch (Exception error)
		{
			Log.severe(error, COMPONENT, "Failed to load game world, generating default world.");
			world = WorldGenerator.generateWorld();
		}

		this.observer = observer;

		gameLogicScheduler = Executors.newScheduledThreadPool(1);
		logic = new ServerTick(config.getUpdateRate(), config.getNetworkTicks());
		gameLogicScheduler.scheduleAtFixedRate(logic, 0, (long)(config.getUpdateRate() * 1000), TimeUnit.MILLISECONDS);
	}

	/**
	 * Called when a new player logs in.
	 * 
	 * @param client The player's NetClient instance.
	 */
	public void addPlayer(NetClient client)
	{
		logic.addClient(client);
	}

	/**
	 * Called when a player disconnects.
	 * 
	 * @param client The player's NetClient instance.
	 */
	public void removePlayer(NetClient client)
	{
		logic.removeClient(client);
	}

	/**
	 * Called when a player action is received.
	 * 
	 * @param client The client the action is from.
	 * @param action The description of the action.
	 */
	public void processAction(NetClient client, ActionPacket action)
	{
		logic.submitAction(new ClientAction(client, action));
	}

	/**
	 * Returns a view into the game world.
	 */
	public World getWorld()
	{
		return world;
	}

	/**
	 * Returns the current logic tick.
	 */
	public long getTick()
	{
		return logic.tick;
	}

	/**
	 * Returns the time the server started.
	 */
	public long getStartTime()
	{
		return logic.startTime;
	}

	/**
	 * Returns true if there are no active player
	 * entities left in the game state.
	 */
	private boolean noActivePlayers()
	{
		synchronized(world)
		{
			for (Location location : world.getLocations())
				for (Entity entity : location.getEntities())
					if (entity.getType() == EntityType.PLAYER)
						return false;

			return true;
		}
	}

	/**
	 * Stops the game logic loop and saves the game state.
	 * 
	 * @param failed Whether the server failed and had
	 * to shut down (currently ignored).
	 */
	public void close(boolean failed) throws InterruptedException
	{
		Log.info(COMPONENT, "Waiting for all clients to drop.");
		while (!noActivePlayers()) { /* Wait for a bit. */ }
		Log.info(COMPONENT, "Network confirmed down.");

		Log.info(COMPONENT, "Shutting down game logic loop, awaiting confirmation.");

		gameLogicScheduler.shutdown();
		if (!gameLogicScheduler.awaitTermination(10, TimeUnit.SECONDS))
			Log.warning(COMPONENT, "Failed to shut down in time, aborting.");
		else
			Log.info(COMPONENT, "Game logic loop successfully terminated.");

		Log.info(COMPONENT, "Saving world state.");
		DataStorage.saveFile(worldStateFile, world);
		Log.info(COMPONENT, "World has been saved.");
	}
}
