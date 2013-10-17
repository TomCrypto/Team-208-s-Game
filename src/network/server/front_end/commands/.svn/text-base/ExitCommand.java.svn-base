package network.server.front_end.commands;

import network.server.back_end.*;
import java.io.IOException;

/**
 * Closes the server.
 */
public class ExitCommand implements ServerCommand
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
		return true;
	}
}
