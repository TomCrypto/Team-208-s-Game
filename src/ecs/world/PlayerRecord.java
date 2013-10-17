package ecs.world;

import javax.xml.bind.annotation.XmlElement;

import ecs.entity.Entity;

/**
 * Represents a location-entity pair designed to store players in order to save and load them.
 */
public class PlayerRecord
{
	private String location;
	private Entity player;
	
	public PlayerRecord(String location, Entity player)
	{
		this.location = location;
		this.player = player;
	}
	
	public PlayerRecord(){}
	
	@XmlElement
	public Entity getPlayer(){
		return player;
	}
	
	public void setPlayer(Entity p){
		player = p;
	}
	
	@XmlElement
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String loc){
		location = loc;
	}
}

