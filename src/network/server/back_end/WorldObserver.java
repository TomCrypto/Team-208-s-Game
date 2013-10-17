package network.server.back_end;

import ecs.world.*;

/**
 * This is  an observer interface  for game
 * state updates to broadcast to players.
 *
 * @author Thomas Beneteau (300250968)
 */
public interface WorldObserver
{
	/**
	 * Commits the current world's state to the
	 * connected clients. What  happens here is
	 * the game  server decides  which entities
	 * and  components have  changed and  sends
	 * them  to  the  clients, and  also  finds
	 * out if  any player changed  location and
	 * synchronizes them  with the  server side
	 * location.
	 * <p>
	 * This method must be called once and only
	 * once <b>at the end</b> of a server tick.
	 * After this method  is called, there will
	 * be no sane way to reverse changes to the
	 * world client side.
	 */
	public void commitWorldState(World world);
}
