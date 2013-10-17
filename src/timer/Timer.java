package timer;

import java.util.ArrayList;
import org.lwjgl.Sys;

/**
 * @author Marc Sykes
 * An object in which a reference to all objects that must have their game state updated
 * every tick shold be stored.
 */
public class Timer extends Thread {
	
	//the list of objects that need to be executed every game tick
	private ArrayList<Tick> objects;
	
	//The time a game tick should take
	private static final int TICK_TIME = 1000 / 30;
	
	@Override
	public void start(){
		
		long beginTime;
		long diffTime;
		long sleepTime;
		
		while(true){
			
			beginTime = getTime();
			
			//Lock and execute the tick command in all the stored objects
			synchronized(objects){
				for(Tick t: objects){ t.tick(); }
			}
			
			// calculate how long did the cycle take
			diffTime = getTime() - beginTime;

			// calculate sleep time
			sleepTime = (int)(TICK_TIME-diffTime);

			if(sleepTime > 0){
				try { Thread.sleep(sleepTime); }
				catch(InterruptedException e){ e.printStackTrace(); }
			}
			
		}
	}
	
	public void add(Tick t){
		synchronized(objects){ objects.add(t); }
	}
	
	public long getTime(){ return (Sys.getTime()*1000)/Sys.getTimerResolution(); }
	
}