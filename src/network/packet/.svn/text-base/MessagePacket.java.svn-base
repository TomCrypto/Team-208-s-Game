package network.packet;

/**
 * A  {@code  MessagePacket}  represents  a
 * message sent either  to the server (e.g.
 * a login request) or to other players.
 * 
 * @author Thomas Beneteau (300250968)
 */
public interface MessagePacket extends NetPacket
{
	/**
	 * A   channel  is   an  abstract   message
	 * category,   used  to   simplify  message
	 * packet handling code.
	 */
	public enum Channel
	{
		/**
		 * The  message  is  to be  sent  to  every
		 * player in the player's location (general
		 * chat).
		 */
		PUBLIC,

		/**
		 * The  message  is  to   be  sent  to  the
		 * server,   for  administrative   purposes
		 * (e.g. setting the player's name).
		 */
		SERVER,

		/**
		 * The  message should  be sent  to another
		 * player privately.
		 */
		PRIVATE,
	}

	/**
	 * The channel where the message belongs.
	 * 
	 * @return Returns the message channel.
	 */
	public Channel getChannel();
}
