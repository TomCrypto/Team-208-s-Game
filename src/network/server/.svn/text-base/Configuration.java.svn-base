package network.server;

import java.io.*;
import java.util.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;

/**
 * This   describes   the   game   server's
 * configuration,  such  as   the  port  on
 * which to  listen, the maximum  number of
 * players, and so  on. This information is
 * parsed from an XML file.
 * <p>
 * This class is immutable.
 * 
 * @author Thomas Beneteau (300250968)
 */
public final class Configuration
{
	private final Set<String> blacklist;

	/**
	 * A  set  of  IP addresses  which  are  to
	 * be  blacklisted   by  the   server.  Any
	 * connection attempt  from these addresses
	 * will be unconditionally rejected.
	 */
	public Set<String> getBlacklist()
	{
		return Collections.unmodifiableSet(blacklist);
	}
	
	private final int maxPlayers;
	
	/**
	 * The maximum number of players the server
	 * can handle concurrently.
	 */
	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	private final int port;
	
	/**
	 * The  network port  on  which the  server
	 * is   to  listen   for  incoming   player
	 * connections.
	 */
	public int getPort()
	{
		return port;
	}

	private final int tcpBufferSize;
	
	/**
	 * The server's per-client  TCP queue size,
	 * in  kilobytes.  Recommended 1  MB  (1024
	 * kB), cannot  be zero or greater  than 32
	 * MB.
	 */
	public int getBufferSize()
	{
		return tcpBufferSize;
	}
	
	private final String worldStateFile;
	
	/**
	 * The  location of  the  game state  file,
	 * which contains an  XML representation of
	 * the game  at some point in  time (useful
	 * to  save and  resume games  even if  the
	 * server shuts down).
	 */
	public String getWorldStateFile()
	{
		return worldStateFile;
	}
	
	private final double updateRate;
	
	/**
	 * The  update rate  of  the server,  which
	 * defines how often the server updates its
	 * game state (and hence how fast - at most
	 * - clients receive network packets).
	 * <p>
	 * This is in units of seconds/update.
	 */
	public double getUpdateRate()
	{
		return updateRate;
	}
	
	private final int networkTicks;
	
	/**
	 * This indicates the tick interval between
	 * network updates. For instance, if {@code
	 * updateRate}  is  set   to  1/60th  of  a
	 * second, and {@code  networkTicks} is set
	 * to  3, then  the server  will send  game
	 * state updates every 1/20ths of a second.
	 */
	public int getNetworkTicks()
	{
		return networkTicks;
	}
	
	/**
	 * Attempts  to  load   the  game  server's
	 * configuration from an XML file.
	 * 
	 * @param   configFile  The   configuration
	 * file.
	 */
	public Configuration(File configFile) throws IOException, ConfigurationException
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder xmlBuilder = factory.newDocumentBuilder();
			
			/* This is to disable the automatic logging of errors,
			 * we are already doing our own error handling so not
			 * doing this prints out every single error twice. */
			xmlBuilder.setErrorHandler(null);
			
			Document xmlDocument = xmlBuilder.parse(configFile);
			Element root = xmlDocument.getDocumentElement();
			root.normalize();
				
			if (root.getNodeName().equals("server"))
			{
				this.port = parseInt(valueOf(getNode(root, "port")));
				this.maxPlayers = parseInt(valueOf(getNode(getNode(root, "players"), "max")));
				this.worldStateFile = valueOf(getNode(getNode(root, "world"), "savefile"));
				this.blacklist = valueOf(getNodes(getNode(root, "blacklist"), "ip"));
				this.tcpBufferSize = parseInt(valueOf(getNode(getNode(root, "tcp"), "buffer")));
				this.updateRate = parseDouble(valueOf(getNode(getNode(root, "world"), "updaterate")));
				this.networkTicks = parseInt(valueOf(getNode(getNode(root, "world"), "networkticks")));
			}
			else
			{
				throw new ConfigurationException("could not locate server configuration");
			}
		}
		catch (ParserConfigurationException e)
		{
			throw new ConfigurationException(e.getMessage());
		}
		catch (SAXException e)
		{
			throw new ConfigurationException(e.getMessage());
		}
	}
	
	private Element getNode(Element parent, String tag) throws ConfigurationException
	{
		NodeList list = parent.getElementsByTagName(tag);
		if (list.getLength() == 0) throw new ConfigurationException("server parameter '%s' was not located", tag);
		if (list.getLength()  > 1) throw new ConfigurationException("duplicate server parameter '%s' located", tag);
		return (Element)list.item(0);
	}
	
	private Set<Element> getNodes(Element parent, String tag)
	{
		NodeList list = parent.getElementsByTagName(tag);
		Set<Element> elements = new HashSet<Element>(list.getLength());
		for (int t = 0; t < list.getLength(); ++t) elements.add((Element)list.item(t));
		
		return elements;
	}
	
	private String valueOf(Element element)
	{
		return element.getTextContent();
	}
	
	private Set<String> valueOf(Set<Element> elements)
	{
		Set<String> values = new HashSet<String>(elements.size());
		for (Element element : elements) values.add(valueOf(element));
		
		return values;
	}
	
	private int parseInt(String value) throws ConfigurationException
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			throw new ConfigurationException("expected integer but found '%s'", value);
		}
	}
	
	private double parseDouble(String value) throws ConfigurationException
	{
		try
		{
			return Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			throw new ConfigurationException("expected double but found '%s'", value);
		}
	}
	
	/**
	 * This  exception is  thrown when  parsing
	 * the  server configuration  from the  XML
	 * file fails, either due to malformed XML,
	 * or malformed  configuration fields (such
	 * as  passing  a  string  as  the  network
	 * port).
	 * <p>
	 * This  exception  does  <b>not</b>  cover
	 * invalid   configuration   fields   (such
	 * as   a   negative   port),   which   are
	 * handled  in  their respective  component
	 * and    dealt     with    using    {@code
	 * IllegalArgumentException} exceptions.
	 */
	@SuppressWarnings("serial")
	public static class ConfigurationException extends Exception
	{
		public ConfigurationException(String msg, Object... args)
		{
			super(String.format(msg, args));
		}
	}
}