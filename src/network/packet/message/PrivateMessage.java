package network.packet.message;

import network.packet.MessagePacket;

/**
 * This  message  is  for  sending  private
 * messages  to  other  players,  invisible
 * to  anyone  else.   They  will  be  sent
 * regardless of the location of the source
 * and recipient players.
 * <p>
 * If   the  message   goes  through   (the
 * recipient  exists),  then the  recipient
 * will    receive   the    proper   {@code
 * PrivateMessage} instance with the {@code
 * source}  and  {@code  recipient}  fields
 * properly  initialized. If  the recipient
 * does not  exist, the server  will bounce
 * back the message to the source.
 *
 * @author Thomas Beneteau (300250968)
 */
public class PrivateMessage implements MessagePacket
{
	private static final long serialVersionUID = 1L;

	@Override
	public Type getType()
	{
		return Type.MESSAGE;
	}

	@Override
	public Channel getChannel()
	{
		return Channel.PRIVATE;
	}

	private final String recipient, message;
	private String source;

	public PrivateMessage(String source, String recipient, String message)
	{
		this.recipient = recipient;
		this.message = message;
		this.source = source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getSource()
	{
		return source;
	}

	public String getRecipient()
	{
		return recipient;
	}

	public String getMessage()
	{
		return message;
	}
}
