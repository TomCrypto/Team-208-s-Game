package network.server.front_end.commands;

import network.server.back_end.*;
import network.server.back_end.ClientList.*;

import java.io.IOException;
import network.NetClient;

/**
 * Lists the currently connected players along
 * with their location, and some network stats.
 */
public class ListCommand implements ServerCommand
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
		synchronized (server.getWorldState().getWorld())
		{
			System.out.printf("\n");
			
			if (server.getClientCount() == 0)
			{
				System.out.printf("  No players currently connected - nothing to display.\n");
			}
			else
			{
				System.out.printf("  IP Address       Throughput      Player Name       Location                 \n");
				System.out.printf("  ===============  ==============  ================  =========================\n");
				
				server.clientFilter(new ClientTask()
				{
					@Override
					public boolean nextClient(NetClient client)
					{
						System.out.printf("  %15s  %14s  %16s  %25s\n",
								          formatAddress(client.getAddress()),
								          formatThroughput(client),
								          formatName(client),
								          formatLocation(client));
						return true;
					}
				});
			}
			
			System.out.printf("\n");
			return false;
		}
	}
	
	private String formatAddress(String address)
	{
		return address;
	}
	
	private String formatThroughput(NetClient client)
	{
		return String.format("%d ~ %d kB/s", (int)(client.getToThroughput() / 1024),
			                                 (int)(client.getFromThroughput() / 1024));
	}
	
	private String formatName(NetClient client)
	{
		if (client.isLoggedIn())
			return ellipsis(client.getName(), 16);
		else
			return "-";
	}
	
	private String formatLocation(NetClient client)
	{
		if (client.isLoggedIn() && (client.getLocation() != null))
			return ellipsis(client.getLocation().getName(), 25);
		else
			return "-";
	}
	
	private String ellipsis(String string, int maxLength)
	{
		if (string.length() < maxLength) return string;
		else return string.substring(0, maxLength - 3) + "...";
	}
	
	/*private String formatSpeed(double speed)
	{
		if (Double.isNaN(speed)) return "-";
		if (speed < 1024) return String.format("%d B/s", (int)speed);
		if (speed < 1024 * 1000.0) return String.format("%.1f kB/s", speed / 1024);
		if (speed < 1024 * 1024 * 1000.0) return String.format("%.1f MB/s", speed / (1024 * 1024));
		if (speed < 1024 * 1024 * 1024 * 1000.0) return String.format("%.1f GB/s", speed / (1024 * 1024 * 1024));
		
		return String.format("-");
	}*/
}
