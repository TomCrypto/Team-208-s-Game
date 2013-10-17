package network.client.observers;

import network.*;
import network.packet.message.LoginMessage.*;

/**
 * This observer listens to login events.
 * 
 * @author Thomas Beneteau (300250968)
 */
public interface LoginObserver
{
	/**
	 * Called when the  player has successfully
	 * logged into the game server.
	 * 
	 * @param  client   The  {@code  NetClient}
	 * instance.
	 */
	public void loginSucceeded(NetClient client);

	/**
	 * Called when the player has failed to log
	 * in (his  name was already taken,  or was
	 * too long, etc..)
	 * 
	 * @param  client   The  {@code  NetClient}
	 * instance.
	 * @param status  The login status.
	 */
	public void loginFailed(NetClient client, LoginStatus status);
}
