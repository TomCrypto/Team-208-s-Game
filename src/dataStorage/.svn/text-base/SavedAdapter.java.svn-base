package dataStorage;

import java.util.*;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import ecs.world.PlayerRecord;

/**
 * Handles the marshalling and unmarshalling for the savedPlayers in the World class
 * 
 * @author Nainesh Patel
 *
 */
public class SavedAdapter extends XmlAdapter<SavedAdapter.AdaptedSaved, Map<String, PlayerRecord>>{
	@Override
	public AdaptedSaved marshal(Map<String, PlayerRecord> map) throws Exception {
		AdaptedSaved adapted = new AdaptedSaved();
		for(Map.Entry<String, PlayerRecord> entrySet: map.entrySet()){
			Entry entry = new Entry();
			entry.key = entrySet.getKey();
			entry.value = entrySet.getValue();
			adapted.entry.add(entry);
		}
		return adapted;
	}

	@Override
	public Map<String, PlayerRecord> unmarshal(AdaptedSaved adapted) throws Exception {
		Map<String, PlayerRecord> map = new HashMap<String, PlayerRecord>();
		for(Entry entry: adapted.entry){
			map.put(entry.key, entry.value);
		}
		return map;
	}
	
	/**
	 * Holds a set of Entry for easy access by SavedAdapter
	 * @author Nainesh Patel
	 *
	 */
	public static class AdaptedSaved{
		public List<Entry> entry = new ArrayList<Entry>();
	}
	
	/**
	 * Holds the map key and values from SavedPlayers in separate fields
	 * @author Nainesh Patel
	 *
	 */
	public static class Entry{
		public String key;
		public PlayerRecord value;
	}
}
