package network.server.front_end.commands;

import network.server.back_end.*;
import java.io.IOException;

import log.Log;

/**
 * Prints out the server's uptime, as well as some
 * performance statistics about the logic loop.
 */
public class UptimeCommand implements ServerCommand
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
		long elapsed = System.currentTimeMillis() - server.getWorldState().getStartTime();
		double updateRate = server.getConfiguration().getUpdateRate();
		long tick = server.getWorldState().getTick();
		
		System.out.printf("\n");
		System.out.printf("  Server processing tick %d, uptime approximately %s.\n", tick, formatTime(tick * updateRate));
		System.out.printf("  Tick statistics: %.2f ms target, %.2f ms average, %.0f%% saturated.\n",
						  updateRate * 1000, (double)elapsed / tick,
						  Math.max(0, 100 * (((double)elapsed / tick) / updateRate / 1000 - 1)));
		System.out.printf("\n");
		
		return false;
	}
	
	private String formatTime(double seconds)
	{
		return Log.getFormattedDate(0, (long)(seconds * 1000));
	}
}
