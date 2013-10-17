package network.server.back_end;

import network.*;
import network.packet.message.LoginMessage.*;

/**
 * This interface is used as a notification
 * mechanism for when  clients log into the
 * game server, in order  for the server to
 * prepare their character  and add them to
 * the game world.
 * 
 * @author Thomas Beneteau (300250968)
 */
public interface LoginObserver
{
	/**
	 * This  method  is  called when  a  client
	 * sends a login request to the server, and
	 * after the request is processed.
	 * 
	 * @param client The client instance.
	 * @param status The login status (result).
	 */
	public void loginProcessed(NetClient client, LoginStatus status);
}
