package network.server.front_end.commands;

import network.NetClient;
import network.server.back_end.*;
import java.io.IOException;

/**
 * Disconnects a specific player, by name or by IP,
 * or disconnects every connected player directly.
 */
public class KillCommand extends Object implements ServerCommand
{
	@Override
	public boolean validate(String[] args)
	{
		if (args.length == 0) return true;
		if (args.length == 1) return false;
		return (args[0].equals("ip")) || (args[0].equals("name"));
	}

	@Override
	public String usage(String command)
	{
		return String.format("%s ip/name [...]", command);				
	}

	@Override
	public boolean execute(String[] args, GameServer server) throws IOException
	{
		if (args.length == 0)
		{
			server.clientFilter(new AllFilter());
		}
		else
		{
			if (args[0].equals("ip"))
				for (int t = 1; t < args.length; ++t) server.clientFilter(new IPFilter(args[t]));
			else if (args[0].equals("name"))
				for (int t = 1; t < args.length; ++t) server.clientFilter(new NameFilter(args[t]));
		}
		
		return false;
	}
	
	private class AllFilter implements ClientList.ClientTask
	{
		@Override
		public boolean nextClient(NetClient client)
		{
			client.disconnect();
			return true;
		}
	}
	
	private class IPFilter implements ClientList.ClientTask
	{
		private String IP;
		
		public IPFilter(String IP)
		{
			this.IP = IP;
		}
		
		@Override
		public boolean nextClient(NetClient client)
		{
			if (client.getAddress().equals(IP)) client.disconnect();
			return true;
		}
	}
	
	private class NameFilter implements ClientList.ClientTask
	{
		private String name;
		
		public NameFilter(String name)
		{
			this.name = name;
		}
		
		@Override
		public boolean nextClient(NetClient client)
		{
			if (client.getName().equals(name)) client.disconnect();
			return true;
		}
	}
}
