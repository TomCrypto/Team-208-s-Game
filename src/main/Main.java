package main;

import network.server.front_end.ControlPanel;	/* Server entry point. */
import userinterface.GameStateManager;			/* Client entry point. */

import log.Log;

/**
 * This   is  the   entry  point   for  the
 * game.  Launching   the  jar   file  with
 * no  command-line  arguments  will  start
 * the   client's  user   interface,  while
 * specifying   {@code  --server}   on  the
 * command-line  will run  the game  server
 * instead.
 */
public class Main
{
	/**
	 * If  the first  command-line argument  is
	 * {@code --server}, runs the built-in game
	 * server, otherwise runs the game client.
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args)
	{
		try
		{
			if ((args.length != 0) && (args[0].equals("--server")))
			{
				Log.setupLogger("Game Server");
				ControlPanel.main(strip(args));
			}
			else
			{
				Log.disableLogger();
				GameStateManager.main(args);
			}
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}
	}
	
	/**
	 * This  method strips  the first  argument
	 * off the command-line arguments array and
	 * returns the remaining arguments.
	 */
	private static String[] strip(String[] args)
	{
		return java.util.Arrays.copyOfRange(args, 1, args.length);
	}
}
