package network.client.observers;

/**
 * This observer listens to chat events.
 *
 * @author Thomas Beneteau (300250968)
 */
public interface ChatObserver
{
	public void noSuchRecipient(String recipient, String message);

	public void publicMessageReceived(String source, String message);
	public void privateMessageReceived(String source, String message);
}
