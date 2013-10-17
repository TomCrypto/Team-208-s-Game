package network.packet.transport;

import network.packet.transport.estimators.*;
import java.nio.ByteBuffer;
import naga.PacketWriter;
import naga.NIOUtils;
import log.Log;

/**
 * This  packet  writer  is  used  to  send
 * game  network packets  over the  network
 * which   can   be   received   with   the
 * GamePacketReader.
 * <p>
 * This class  implements the  Naga "packet
 * writing" strategy  pattern, by  taking a
 * byte  array as  a raw  game packet,  and
 * converting it into a format suitable for
 * transport over the  internet and correct
 * reception  (the   so-called  application
 * transport layer).
 * 
 * @author Thomas Beneteau (300250968)
 */
public class TransportPacketWriter implements PacketWriter
{
	private static final String COMPONENT = "Network Transport Layer";
	
	/**
	 * This keeps track of the measured network
	 * throughput (in units of bytes/second).
	 */
	private Estimator throughput = new ThroughputEstimator();
	
	/**
	 * Returns  the  best estimated  throughput
	 * from the local end, in bytes per second.
	 */
	public double getThroughput()
	{
		return throughput.estimate();
	}
	
	@Override
	public ByteBuffer[] write(ByteBuffer[] data)
	{
		long time = System.currentTimeMillis();
		int packetSize = 0;
		
		ByteBuffer header = ByteBuffer.allocate(TransportUtils.HEADER_SIZE);
		for (ByteBuffer buffer : data) packetSize += buffer.remaining();
		header.putInt(packetSize);
		header.putLong(time);
		header.rewind();
		
		if (packetSize > TransportUtils.MAX_PACKET_SIZE)
			Log.warning(COMPONENT, "Outgoing network packet is too large (%d bytes).", packetSize);

		throughput.offer(packetSize + header.remaining());
		return NIOUtils.concat(header, data);
	}
}
