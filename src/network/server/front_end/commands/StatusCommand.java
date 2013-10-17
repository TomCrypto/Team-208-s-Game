package network.server.front_end.commands;

import network.server.back_end.*;
import java.io.IOException;

/**
 * Prints out the server status (what it is doing).
 */
public class StatusCommand implements ServerCommand
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
		System.out.printf("  Listening on port %d, currently serving %d/%d player(s).\n",
				          server.getConfiguration().getPort(), server.getClientCount(),
				          server.getConfiguration().getMaxPlayers());
		System.out.printf("\n");
		return false;
	}
}
