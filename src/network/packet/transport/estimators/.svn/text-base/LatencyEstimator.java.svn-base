package network.packet.transport.estimators;

import network.packet.transport.Estimator;

/**
 * This class is actually not used, it works fine
 * but we are not able to accurately compute the
 * latency of clients in the code, so we just
 * aren't using it.
 */
public class LatencyEstimator extends Estimator
{
	/**
	 * This estimator uses a simple exponential
	 * smoothing   algorithm   to  smooth   out
	 * latency  measurements.   This  parameter
	 * says  that  the  weight  assigned  to  a
	 * measurement decays by a factor of {@code
	 * 1/e} every {@code ALPHA} seconds.
	 * <p>
	 * This  parameter  ranges  from  exclusive
	 * zero to positive infinity.
	 */
	private static final double ALPHA = 1.25;
	
	/**
	 * Samples which have been measured further
	 * back  than this  amount  of seconds  are
	 * discarded as out of date.
	 * <p>
	 * Set this parameter  to positive infinity
	 * to disable it.
	 */
	private static final double RELEVANCE = 10;
	
	private static double weight(double delta)
	{
		return Math.exp(-delta / ALPHA); 
	}
	
	public synchronized double estimate()
	{
		double time = getTime();
		double normalized = 0;
		double estimate = 0;
		
		for (Sample sample : samples)
		{
			if (time - sample.time > RELEVANCE) continue;
			double weight = weight(time - sample.time);
			estimate += sample.sample * weight;
			normalized += weight;
		}
		
		return (normalized < 1e-5 ? Double.NaN : estimate / normalized);
	}
}
