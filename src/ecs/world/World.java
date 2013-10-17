package ecs.world;

import java.util.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dataStorage.SavedAdapter;

import ecs.entity.*;
import ecs.helpers.LocationFactory;
import ecs.systems.*;

import network.*;

@XmlRootElement
public class World {

	private Set<Location> locations = new HashSet<Location>();
	private List<EntitySystem> systems = new ArrayList<EntitySystem>();
	//private Location startLocation; // Where all players start

	private Map<String, PlayerRecord> savedPlayers = new HashMap<String, PlayerRecord>();

	private transient final PhysicsSystem physics = new PhysicsSystem();
	private transient final TargetSystem target = new TargetSystem();
	private transient final DeathSystem death = new DeathSystem();
	private transient final AISystem ai = new AISystem();

	public World()
	{
		addSystem(physics);
		addSystem(target);
		addSystem(ai);
		addSystem(death);
		//startLocation = LocationFactory.startingLocation();
		//locations.add(startLocation);
		locations.add(LocationFactory.startingLocation());
	}

	public PhysicsSystem getPhysics()
	{
		return physics;
	}

	public void add(Location location) {
		locations.add(location);
	}

	public void addToLoc(String location, Entity entity){
		for(Location loc: locations){
			loc.addEntity(entity);
		}
	}

	@XmlElement(name="location")
	public Set<Location> getLocations(){
		return locations;
	}

	public void addSystem(EntitySystem system){
		systems.add(system);
	}

	public void process(Location location, double delta) {
		for(EntitySystem system : systems){
			system.process(this, location, delta);
		}
	}

	public String toString(){
		if(locations.isEmpty()) return "no locations";
		else return "some locations";
		//return locations.toString();
	}

	public Location getStartLocation() {
		//return startLocation;

		for (Location location : locations)
			if (location.getName().equals("Starting location"))
				return location;

		return null;
	}

	public void savePlayer(NetClient client)
	{
		if (savedPlayers.containsKey(client.getName())) System.out.println("Player already saved!");

		savedPlayers.put(client.getName(), new PlayerRecord(client.getLocation().getName(), client.getPlayer()));
		client.getPlayer().setRemoved();
	}

	private Location findLocation(String name)
	{
		for (Location location : locations)
			if (location.getName().equals(name))
				return location;

		return null;
	}

	public boolean loadPlayer(NetClient client)
	{
		if (savedPlayers.containsKey(client.getName()))
		{
			PlayerRecord record = savedPlayers.get(client.getName());
			Location location = findLocation(record.getLocation());
			if (location != null)
			{
				client.setPlayerEntity(record.getPlayer());
				savedPlayers.remove(client.getName());
				client.getPlayer().clearRemoved();
				client.changeLocation(location);
				return true;
			}
		}

		return false;
	}
	
	@XmlElement
	@XmlJavaTypeAdapter(SavedAdapter.class)
	public Map<String, PlayerRecord> getSavedPlayers(){
		return savedPlayers;
	}
	
	public void setSavedPlayers(Map<String, PlayerRecord> players){
		savedPlayers = players;
	}
}
