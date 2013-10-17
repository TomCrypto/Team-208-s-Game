package network.packet.transport;

import network.packet.transport.estimators.*;
import java.nio.ByteBuffer;
import naga.PacketReader;
import naga.exception.*;
import log.Log;

/**
 * This specialized  packet reader  is used
 * to  decode  game   network  packets  and
 * provide network metrics  such as latency
 * and  throughput  for  both  clients  and
 * server.
 * <p>
 * More specifically, this class implements
 * the   Naga  "packet   reading"  strategy
 * pattern,  by  taking  as  an  input  the
 * current network buffer,  parsing it, and
 * waiting until a fully formed game packet
 * is  received before  passing  it to  the
 * game client or server.
 * 
 * @author Thomas Beneteau (300250968)
 */
public class TransportPacketReader implements PacketReader
{
	private static final String COMPONENT = "Network Transport Layer";
	
	/**
	 * This keeps track of the measured network
	 * latency (in units of seconds).
	 */
	private Estimator latency = new LatencyEstimator();
	
	/**
	 * This keeps track of the measured network
	 * throughput (in units of bytes/second).
	 */
	private Estimator throughput = new ThroughputEstimator();
	
	/**
	 * Returns   the  best   estimated  latency
	 * between the  server and the  client this
	 * packet reader is serving, in seconds.
	 */
	public double getLatency()
	{
		return latency.estimate();
	}
	
	/**
	 * Returns  the  best estimated  throughput
	 * from the remote end in bytes per second.
	 */
	public double getThroughput()
	{
		return throughput.estimate();
	}
	
	@Override
	public byte[] nextPacket(ByteBuffer buffer) throws ProtocolViolationException
	{
		if (buffer.remaining() < TransportUtils.HEADER_SIZE) return null;
		
		buffer.mark(); /* Start of header. */
		int packetSize = buffer.getInt();
		long time = buffer.getLong();
		
		if ((packetSize <= 0) || (packetSize > TransportUtils.MAX_PACKET_SIZE))
		{
			Log.warning(COMPONENT, "Rejecting invalid network packet (%d bytes).", packetSize);
			throw new ProtocolViolationException("Received network packet with invalid size.");
		}
		
		if (buffer.remaining() < packetSize)
		{
			buffer.reset();
			return null;
		}
		
		byte[] data = new byte[packetSize];
		buffer.get(data);
		
		double RTT = 2 * (double)(System.currentTimeMillis() - time) / 1000.0; /* Compute the round-trip time. */
		if (RTT < 0) Log.info(COMPONENT, "Packet yielded negative latency (%.3f seconds) - assuming zero.", RTT);
		
		throughput.offer(packetSize + TransportUtils.HEADER_SIZE);
		latency.offer(Math.max(0, RTT));
		return data;
	}
}
