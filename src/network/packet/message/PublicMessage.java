package network.packet.message;

import network.packet.MessagePacket;

/**
 * A  "public message"  is  like a  private
 * message,  except that  there  is no  one
 * recipient. Instead, every  player in the
 * same location as the player who sent the
 * message will be sent the message.
 * 
 * @author Thomas Beneteau (300250968)
 */
public class PublicMessage implements MessagePacket
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
		return Channel.PUBLIC;
	}

	private final String message;
	private String source;

	public PublicMessage(String message)
	{
		this.message = message;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getSource()
	{
		return source;
	}

	public String getMessage()
	{
		return message;
	}
}
