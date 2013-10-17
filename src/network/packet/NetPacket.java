package network.packet;

/**
 * Represents a  network packet  going over
 * the network.
 * <p>
 * Remember to change the serial version ID
 * every  time you  make  a  change to  the
 * class  which  would cause  deserializing
 * the old  version with  the new  class to
 * fail (or vice versa).
 * 
 * @author Thomas Beneteau (300250968)
 */
public interface NetPacket extends java.io.Serializable
{
	/**
	 * These  are  all  the  high-level  packet
	 * types a network packet can assume.
	 */
	public enum Type
	{
		/**
		 * The  Message packet  type  will be  used
		 * for   player/player  and   player/server
		 * communications.
		 */
		MESSAGE,
		
		/**
		 * The  Action  packet  type is  sent  when
		 * players perform  some action  which will
		 * cause the game state to change.
		 */
		ACTION,
		
		/**
		 * The Update  packet type  is sent  by the
		 * server  to  administer  changes  in  the
		 * players' local game states.
		 */
		UPDATE,
	}
	
	/**
	 * This  method is  used  to help  dispatch
	 * different   packet    types   to   their
	 * respective handlers.
	 * 
	 * @return  Returns  the  network  packet's
	 * type.
	 */
	public Type getType();
}
