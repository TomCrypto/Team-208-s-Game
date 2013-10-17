package network.server.front_end;

import log.*;

import java.io.*;
import java.util.*;

import network.server.*;
import network.server.back_end.*;
import network.server.front_end.commands.*;

/**
 * This  is  the game  server's  front-end,
 * which  will manage  the   server's  user
 * interface (control panel) as well as the
 * server  configuration,  which is  loaded
 * from an XML file.
 *
 * @author Thomas Beneteau (300250968)
 */
public class ControlPanel
{
	private static final String COMPONENT = "Control Panel";

	/**
	 * The command map for the control panel.
	 */
	private final CommandMap commandMap = new CommandMap();

	/**
	 * The game server instance.
	 */
	private final GameServer server;

	/**
	 * Starts   the   server's  front-end   and
	 * initializes the underlying game server.
	 *
	 * @param config The server configuration.
	 */
	public ControlPanel(Configuration config) throws IOException
	{
		server = new GameServer(config);
	}

	/**
	 * This method starts the front-end control
	 * panel and lets  the user input commands.
	 * It  will  block   until  the  server  is
	 * terminated, either on  purpose or due to
	 * a fatal exception.
	 * <p>
	 * The  game  server  has  already  started
	 * prior to  this call, and may  already be
	 * actively serving players.
	 * <p>
	 * Before this method is called, all server
	 * components  should  be in  a  consistent
	 * and  usable  state,   for  instance  the
	 * server instance should be ready to start
	 * accepting new players,  the data storage
	 * package  should  have  loaded  the  game
	 * state from disk, and so on.
	 */
	public void startControlPanel() throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		final String commandSeparator = " ";
		final String prompt = "# ";
		System.out.print(prompt);
		boolean aborted = false;

		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			String[] tokens = line.split(commandSeparator);
			String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
			if ((!line.equals("")) && (processCommand(tokens[0], args)))
			{
				aborted = true;
				break;
			}
			else System.out.print(prompt); /* Next command. */
		}

		if (!aborted) System.out.printf("\n");
		scanner.close();
	}

	/**
	 * This method  is called after  the server
	 * has terminated, and is designed to allow
	 * server  components  to  write  back  any
	 * state they need to maintain to the disk,
	 * or in general clean up after themselves.
	 * <p>
	 * The  {@code  failed}  parameter  conveys
	 * whether the  server failed, or  was shut
	 * down  on  purpose  (for instance,  by  a
	 * server command). Each  component can use
	 * this information  to make a  decision on
	 * whether  or  not  to  save  their  state
	 * (which may or may  not be corrupted). If
	 * {@code  failed}  is {@code  true},  user
	 * input  will  still be  available  during
	 * this method  , therefore  components can
	 * prompt the user if needed.
	 *
	 * @param failed  This parameter  is {@code
	 * true}  if  the   server  encountered  an
	 * error, and {@code false} otherwise.
	 */
	public void finish(boolean failed) throws InterruptedException
	{
		Log.info(COMPONENT, "Finishing (failed = %b).", failed);
		server.close(failed);
	}

	/**
	 * This  method  takes  a command  and  any
	 * additional  arguments,  and attempts  to
	 * parse and execute it.
	 *
	 * @param name The command.
	 * @param args The arguments.
	 *
	 * @return  Returns  {@code  true}  if  the
	 * command  indicated   the  server  should
	 * terminate, {@code false} otherwise.
	 *
	 * @see ServerCommand
	 */
	private boolean processCommand(String name, String[] args) throws IOException
	{
		if (commandMap.hasCommand(name))
		{
			ServerCommand command = commandMap.getCommand(name);
			if ((args.length == 1) && ((args[0].equals("-h")) || (args[0].equals("--help"))))
			{
				printUsage(name, command);
			}
			else
			{
				if (!command.validate(args)) printUsage(name, command);
				else return command.execute(args, server);
			}
		}
		else System.out.printf("\n  Unrecognized command '%s'.\n\n", name);

		return false;
	}

	/**
	 * This method prints  a command's usage to
	 * standard output.
	 *
	 * @param name The command name.
	 * @param command Its implementation.
	 */
	private void printUsage(String name, ServerCommand command)
	{
		System.out.printf("\n  Usage: %s\n\n", command.usage(name));
	}

	/**
	 * Takes as arguments a  single path to the
	 * server configuration XML file.
	 * <p>
	 * This  method  is normally  invoked  from
	 * the  game's  global entry  point  {@code
	 * Main.main}  by  stripping  the  required
	 * {@code --server} argument, but it can be
	 * called separately if needed.
	 *
	 * @param  args The  command-line arguments
	 * to the server's front end.
	 */
	public static void main(String[] args)
	{
		if (args.length == 0) System.out.println("No configuration file provided.");
		else if (args.length > 1) System.out.println("Expected a single argument.");
		else
		{
			try
			{
				Configuration config = new Configuration(new File(args[0]));
				ControlPanel controlPanel = null;

				try
				{
					controlPanel = new ControlPanel(config);
					controlPanel.startControlPanel();
					controlPanel.finish(false);
				}
				catch (Exception error)
				{
					System.out.printf("An error occurred: %s\n", fmtError(error.getMessage()));
					Log.severe(error, COMPONENT, "Caught server error, shutting down.");
					if (controlPanel != null)
					{
						try
						{
							controlPanel.finish(true);
						}
						catch (Exception exception)
						{
							System.out.printf("Error while shutting down: %s\n", fmtError(exception.getMessage()));
							Log.severe(exception, COMPONENT, "Catastrophic error while shutting down.");
						}
					}
				}
			}
			catch (Configuration.ConfigurationException error)
			{
				System.out.printf("Failed to parse configuration file: %s\n", fmtError(error.getMessage()));
				Log.severe(error, COMPONENT, "Server configuration error.");
			}
			catch (IOException error)
			{
				System.out.printf("Failed to open configuration file: %s\n", fmtError(error.getMessage()));
				Log.severe(error, COMPONENT, "Server configuration I/O error.");
			}
		}
	}

	/**
	 * Utility  function  used to  pretty-print
	 * error  strings for  display to  standard
	 * output.
	 *
	 * @param error The error string.
	 */
	private static String fmtError(String error)
	{
		if (error == null) return "no details available, check the log.";
		error = Character.toLowerCase(error.charAt(0)) + error.substring(1);
		return (error.charAt(error.length() - 1) != '.') ? error + "." : error;
	}
}
