package network.packet.transport;

import java.util.*;

/**
 * This class implements a sample averaging
 * algorithm designed to take as an input a
 * sequence  of  time-weighted samples  and
 * calculate  a  time-averaged estimate  of
 * the  relevant  quantity  appropriate  to
 * network communications.
 * 
 * @author Thomas Beneteau (300250968)
 */
public abstract class Estimator
{
	private final int LENGTH = 64;
	
	protected final PriorityQueue<Sample> samples = new PriorityQueue<Sample>(LENGTH);
	
	/**
	 * Gets the current time, in seconds.
	 * 
	 * @return Returns the current time.
	 */
	protected static double getTime()
	{
		return (double)(System.currentTimeMillis()) / 1000;
	}
	
	/**
	 * This  method  offers  a  sample  to  the
	 * estimator.
	 * 
	 * @param  time  The  time the  sample  was
	 * measured.
	 * @param sample The measured sample.
	 */
	public synchronized void offer(double sample)
	{
		if (samples.size() >= LENGTH) samples.poll();	
		samples.add(new Sample(getTime(), sample));
	}
	
	/**
	 * This  method returns  the best  estimate
	 * that can be made with the current sample
	 * data.
	 * <p>
	 * If no  reasonable estimate can  be made,
	 * returns {@code NaN}.
	 * 
	 * @return Returns the estimate so far.
	 */
	public abstract double estimate();
	
	protected static class Sample implements Comparable<Sample>
	{
		public final double time, sample;
		
		public Sample(double time, double sample)
		{
			this.sample = sample;
			this.time = time;
		}

		@Override
		public int compareTo(Sample o)
		{
			return Double.compare(time, ((Sample)o).time);
		}
		
		@Override
		public boolean equals(Object o)
		{
			if ((o == null) || (!(o instanceof Sample))) return false;
			return ((Sample)o).time == time;
		}
		
		@Override
		public int hashCode()
		{
			return Double.valueOf(time).hashCode();
		}
	}
}
