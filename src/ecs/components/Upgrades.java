package ecs.components;

import java.util.*;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dataStorage.UpgradesAdapter;

import userinterface.components.UpgradeButton;
import userinterface.states.UpgradesState;

/**
 * A component held by a player entity to indicate how many upgrades they have
 * @author Nainesh Patel (300232221)
 *
 */
public class Upgrades extends Component{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, Integer> upgrades = new HashMap<String, Integer>();
	
	public Upgrades(){
		upgrades.put("Health", 0);
		upgrades.put("Weapon Strength", 0);
		upgrades.put("Rate of Fire", 0);
		upgrades.put("Inventory Capacity", 0);
	}
	
	/**
	 * Adds 1 to the specified key/value in the map
	 * @param upgraded
	 */
	public void upgrade(String upgraded){
		upgrades.put(upgraded, upgrades.get(upgraded)+1);
	}
	
	@XmlElement
	@XmlJavaTypeAdapter(UpgradesAdapter.class)
	public Map<String, Integer> getUpgrades(){
		return upgrades;
	}
	
	public void setUpgrades(Map<String, Integer> map){
		upgrades = map;
	}
	
	/**
	 * Called when the game and upgrade screen initially load.
	 * Sets each upgrade by however many upgrades the player had before they quit.
	 */
	public void doUpgrades(){
		ArrayList<UpgradeButton> buttons = UpgradesState.getButtons();
		for(Entry<String, Integer> upgrade: upgrades.entrySet()){
			for(UpgradeButton b: buttons){
				if(b.getUpgradeName().equalsIgnoreCase(upgrade.getKey())){
					for(int i = 0; i < upgrade.getValue(); i++){
						b.upgrade();
					}
				}
			}
		}
	}
	
	
}
