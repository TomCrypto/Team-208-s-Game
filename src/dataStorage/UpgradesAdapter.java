package dataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import userinterface.components.UpgradeButton;
import userinterface.states.UpgradesState;

/**
 * Handles the marshalling and unmarshalling for the Upgrades class in ecs.components
 * @author Nainesh
 *
 */
public class UpgradesAdapter extends XmlAdapter<UpgradesAdapter.AdaptedUpgrades, Map<String, Integer>>{
	@Override
	public AdaptedUpgrades marshal(Map<String, Integer> map) throws Exception {
		AdaptedUpgrades adapted = new AdaptedUpgrades();
		for(Map.Entry<String, Integer> entrySet: map.entrySet()){
			UpgradesEntry entry = new UpgradesEntry();
			entry.key = entrySet.getKey();
			entry.value = entrySet.getValue();
			adapted.entry.add(entry);
		}
		return adapted;
	}

	@Override
	public Map<String, Integer> unmarshal(AdaptedUpgrades adapted) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(UpgradesEntry entry: adapted.entry){
			map.put(entry.key, entry.value);
		}
		return map;
	}
	
	/**
	 * Holds a set of Entry for easy access by UpgradesAdapter
	 * @author Nainesh Patel
	 *
	 */
	public static class AdaptedUpgrades{
		public List<UpgradesEntry> entry = new ArrayList<UpgradesEntry>();
	}
	
	/**
	 * Holds the map key and values from upgrades in separate fields
	 * @author Nainesh Patel
	 *
	 */
	public static class UpgradesEntry{
		public String key;
		public Integer value;
	}
}
