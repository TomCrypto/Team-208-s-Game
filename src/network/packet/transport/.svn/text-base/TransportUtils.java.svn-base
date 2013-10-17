package network.packet.transport;

/**
 * This class contains  utility methods and
 * constants  for   packet  reading/writing
 * strategies.
 * 
 * @author Thomas Beneteau (300250968)
 */
class TransportUtils
{
	/**
	 * The absolute  maximum size of  a packet.
	 * Any packet larger than  this will trip a
	 * {@code ProtocolViolationException}.
	 * <p>
	 * This is in  bytes, and does not  include
	 * the size of the packet header.
	 */
	public static final int MAX_PACKET_SIZE = 65536;
	
	/**
	 * The  size  of   a  packet  header.  This
	 * includes  the packet  size, as  a 4-byte
	 * integer, and the time it was sent, as an
	 * 8-byte Unix timestamp (UTC, milliseconds
	 * since epoch).
	 * <p>
	 * This is in bytes.
	 */
	public static final int HEADER_SIZE = 12;
}
