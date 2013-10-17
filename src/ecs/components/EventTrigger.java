package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Basically a test to see if an event can fire. This component doesn't specify the event
 * or anything. Pretty much like a stop watch. Examples of firing events could be random movement after
 * a period of time, or how quickly you can shoot a gun
 *
 * @author Lord Mumford
 *
 */
public class EventTrigger extends Component {

	private static final long serialVersionUID = 1L;

	private long interval; // The interval after which the event can fire
	private long lastFired; // The time when the event last fired

	public EventTrigger(long interval){
		this.interval = interval;
		lastFired = System.currentTimeMillis();
	}

	public EventTrigger(){}

	/** Checks if the event can fire. If it can, then the lastFire is updated
	 *
	 * @param time The current time
	 * @return If the event can fire
	 */
	public boolean canFire(long time){
		if((time - lastFired) > interval){
			lastFired = time;
			return true;
		}
		return false;
	}

	@XmlElement
	public long getInterval(){
		return interval;
	}

	public void setInterval(long interval){
		this.interval = interval;
	}

	@XmlElement
	public long getLastFired(){
		return lastFired;
	}

	public void setLastFired(long last){
		lastFired = last;
	}

	@Override
	public String toString()
	{
		return String.format("EventTrigger -> [interval = %d, lastFired = %d]", interval, lastFired);
	}
}
