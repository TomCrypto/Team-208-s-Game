package network.server.front_end;

import java.util.*;
import java.io.IOException;

import network.server.back_end.*;
import network.server.front_end.commands.*;

import log.Log;

/**
 * The  command  map   is  responsible  for
 * mapping a  server command to  a concrete
 * command implementation  (from the {@code
 * Command} interface). It provides methods
 * for registering new commands and looking
 * them up.
 * 
 * @author Thomas Beneteau (300250968)
 */
public class CommandMap
{
	private final static String COMPONENT = "Command Map";
	
	/**
	 * A  mapping  between command  names  such
	 * as  <i>{@code  exit}</i>  to  a  Command
	 * instance which implements said command.
	 */
	private final Map<String, ServerCommand> map = new HashMap<String, ServerCommand>();
	
	/**
	 * Registers a command implementation under
	 * a given command name.
	 * 
	 * @param name The command name.
	 * @param command Its implementation.
	 */
	public void registerCommand(String name, ServerCommand command)
	{
		Log.debug(COMPONENT, "Registered command '%s' (%s).", name, command.usage(name));
		map.put(name, command);
	}
	
	/**
	 * Registers an alias for a server command.
	 * 
	 * @param alias The  desired alias command.
	 * @param The original command.
	 */
	public void registerAlias(String alias, String command)
	{
		Log.debug(COMPONENT, "Registered alias '%s' to '%s'.", alias, command);
		if (hasCommand(command)) map.put(alias, map.get(command));
	}
	
	/**
	 * Returns the implementation of a command.
	 * If  the command  is not  in the  command
	 * map, returns {@code null}.
	 * 
	 * @param name  The name of the  command to
	 * retrieve.
	 * 
	 * @return     Returns      the     command
	 * implementation, or {@code  null} if none
	 * exists.
	 */
	public ServerCommand getCommand(String name)
	{
		return map.get(name);
	}
	
	/**
	 * Returns whether the command map contains
	 * a given command (or alias).
	 * 
	 * @param name  The name of the  command to
	 * check.
	 * 
	 * @return  Returns  {@code  true}  if  the
	 * command exists, {@code false} otherwise.
	 */
	public boolean hasCommand(String name)
	{
		return (getCommand(name) != null);
	}
	
	/**
	 * Creates a command map, and registers all
	 * known commands and aliases.
	 */
	public CommandMap()
	{
		registerCommand("exit", new ExitCommand());
		registerCommand("list", new ListCommand());
		registerCommand("kill", new KillCommand());
		registerCommand("status", new StatusCommand());
		registerCommand("uptime", new UptimeCommand());
		registerCommand("database", new DatabaseCommand());
		
		registerAlias("quit", "exit");
		registerAlias("up", "uptime");
		registerAlias("db", "database");
		
		registerHelpCommand();
	}
	
	/**
	 * Registers  the  special built-in  {@code
	 * help}   command,   which  displays   all
	 * available commands.
	 */
	private void registerHelpCommand()
	{
		registerCommand("help", new ServerCommand()
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
				System.out.printf("\n");
				System.out.printf("  Available commands:\n\n  \t");
				int index = 0;
				
				for (String command : new TreeSet<String>(map.keySet()))
				{
					System.out.printf(command);
					index += 1;
					
					if (index == map.size()) System.out.printf("\n");
					else if (index % 8 == 0) System.out.printf("\n  \t");
					else System.out.printf(", ");
				}
				
				System.out.printf("\n");
				return false;
			}
		});
	}
}
