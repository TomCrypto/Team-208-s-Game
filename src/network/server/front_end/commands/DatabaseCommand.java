package network.server.front_end.commands;

import ecs.world.*;
import ecs.entity.*;
import ecs.components.Type.*;

import network.server.back_end.*;
import java.io.IOException;

/**
 * Prints some stuff about the game state.
 */
public class DatabaseCommand implements ServerCommand
{
	@Override
	public boolean validate(String[] args)
	{
		return (args.length == 0);
	}

	@Override
	public String usage(String command)
	{
		return String.format("%s (no arguments)", command);				
	}

	@Override
	public boolean execute(String[] args, GameServer server) throws IOException
	{
		World world = server.getWorldState().getWorld();
		
		synchronized (world)
		{
			System.out.printf("\n");
			System.out.printf("  Game Database Status\n  ====================\n\n");
			System.out.printf("  > %d entities (%d components) spanning %d locations total:\n",
							  entityTotal(world), componentTotal(world), locationCount(world));
			
			for (Location location : world.getLocations())
				System.out.printf("    - '%s' with %d entities (%d saved, %d active)\n",
								  location.getName(), location.getEntities().size(),
								  savedPlayerCount(world, location),
								  activePlayerCount(world, location));
			
			if (world.getSavedPlayers().size() > 0)
			{
				System.out.printf("\n");
				System.out.printf("  > Saved players:\n");
				for (PlayerRecord record : world.getSavedPlayers().values())
					System.out.printf("    - '%s' in '%s'\n", record.getPlayer().getName(), record.getLocation());
			}
		}
		
		System.out.printf("\n");
		return false;
	}
	
	private int entityTotal(World world)
	{
		synchronized (world)
		{
			int count = 0;
			
			for (Location location : world.getLocations())
				count += location.getEntities().size();
			
			return count;
		}
	}
	
	private int componentTotal(World world)
	{
		synchronized (world)
		{
			int count = 0;
			
			for (Location location : world.getLocations())
				for (Entity entity : location.getEntities())
					count += entity.getComponents().size();
			
			return count;
		}
	}
	
	private int locationCount(World world)
	{
		synchronized (world)
		{
			return world.getLocations().size();
		}
	}
	
	private int activePlayerCount(World world, Location location)
	{
		synchronized (world)
		{
			int count = 0;
			
			for (Entity entity : location.getEntities())
				if (entity.getType() == EntityType.PLAYER)
					++count;
			
			return count;
		}
	}
	
	private int savedPlayerCount(World world, Location location)
	{
		synchronized (world)
		{
			int count = 0;
	
			for (PlayerRecord record : world.getSavedPlayers().values())
				if (record.getLocation().equals(location.getName()))
					++count;
			
			return count;
		}
	}
}
