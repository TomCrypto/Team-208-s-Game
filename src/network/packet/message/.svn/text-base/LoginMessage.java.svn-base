package network.packet.message;

import network.packet.MessagePacket;

/**
 * A  login  message  is used  to  let  the
 * player  log  into  the game  server  (by
 * choosing  his player  name and  possibly
 * recovering his character from a previous
 * session).    A   {@code    LoginMessage}
 * instance   is   sent  to   the   server,
 * manipulated by the server, and then sent
 * back  over  to  the player,  along  with
 * the result  of the message  (whether the
 * player was successfully logged in).
 * 
 * @author Thomas Beneteau (300250968)
 */
public class LoginMessage implements MessagePacket
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
		return Channel.SERVER;
	}
	
	/**
	 * Represents the status  of a given {@code
	 * LoginMessage} instance.
	 */
	public enum LoginStatus
	{
		/**
		 * The  server has  not  yet processed  the
		 * login message.
		 */
		PENDING,
		
		/**
		 * The client was successfully logged in.
		 */
		LOGGED_IN,
		
		/**
		 * The  requested player  name was  already
		 * taken by another client.
		 */
		NAME_TAKEN,
		
		/**
		 * The requested name is not a valid player
		 * name.
		 */
		INVALID_NAME,
		
		/**
		 * The client is already logged in.
		 */
		ALREADY_LOGGED_IN,
	}
	
	private LoginStatus status;
	private final String name;
	
	/**
	 * Creates  a LoginMessage instance with  a
	 * given player name to log in as.
	 * 
	 * @param name The player name.
	 */
	public LoginMessage(String name)
	{
		status = LoginStatus.PENDING;
		this.name = name;
	}
	
	/**
	 * Returns  the  player name  contained  in
	 * this  LoginMessage  instance.   This  is
	 * unchanged by the server.
	 * 
	 * @return Returns the player name.
	 */
	public String getPlayerName()
	{
		return name;
	}
	
	/**
	 * Sets the new login status for this login
	 * request.
	 * 
	 * @param status The new status.
	 */
	public void setLoginStatus(LoginStatus status)
	{
		this.status = status;
	}
	
	/**
	 * Gets the current login status.
	 * 
	 * @return  Returns  the login  status  for
	 * this login request.
	 */
	public LoginStatus getLoginStatus()
	{
		return status;
	}
}
