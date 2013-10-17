package network.packet.transport.estimators;

import network.packet.transport.Estimator;

public class ThroughputEstimator extends Estimator
{
	/**
	 * Samples which have been measured further
	 * back  than this  amount  of seconds  are
	 * discarded as out of date.
	 * <p>
	 * Set this parameter  to positive infinity
	 * to disable it.
	 */
	private static final double RELEVANCE = 15;
	
	public synchronized double estimate()
	{
		double maxTimeDelta = 0;
		double time = getTime();
		double estimate = 0;
		
		for (Sample sample : samples)
		{
			if (time - sample.time > RELEVANCE) continue;
			if (time - sample.time > maxTimeDelta)
				maxTimeDelta = time - sample.time;
			estimate += sample.sample;
		}

		return (maxTimeDelta < 1e-5 ? Double.NaN : estimate / maxTimeDelta);
	}
}
