package ecs.world;

import java.io.*;
import java.util.*;

import javax.xml.bind.annotation.XmlElement;

import renderer.ModelType;

import ecs.entity.*;
import ecs.helpers.*;

public class Location implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Set<Entity> entities = new HashSet<Entity>();
	private String name;
	private String description;
	private transient Set<SpawnPoint> spawnPoints = new HashSet<SpawnPoint>();
	private ModelType modelType;
	
	@XmlElement
	public Set<SpawnPoint> getSpawnPoints()
	{
		return spawnPoints;
	}
	
	public void setSpawnPoints(Set<SpawnPoint> spawnPoints)
	{
		this.spawnPoints = spawnPoints;
	}

	public Location(String name){
		this.name = name;
		this.description = "A location";
	}

	public Location(){
		this.name = "Location";
		this.description = "A location";
	}

	@XmlElement(name="entity")
	public Set<Entity> getEntities(){
		return entities;
	}

	public void addEntity(Entity e){
		entities.add(e);
	}

	public void setModelType(ModelType mType){
		modelType = mType;
	}

	public ModelType getModelType(){ return modelType; }

	public void removeEntity(long id)
	{
		Iterator<Entity> iter = entities.iterator();

		while (iter.hasNext())
		{
			if (iter.next().getID() == id)
			{
				iter.remove();
				break;
			}
		}
	}

	public Set<Entity> clearRemovedEntities()
	{
		Iterator<Entity> iter = entities.iterator();
		Set<Entity> removed = new HashSet<Entity>();

		while (iter.hasNext())
		{
			Entity entity = iter.next();

			if (entity.isRemoved())
			{
				entity.clearRemoved();
				removed.add(entity);
				iter.remove();
			}
		}

		return removed;
	}

	public void addSpawn(SpawnPoint point){
		spawnPoints.add(point);
	}

	/**
	 * <b>DO NOT USE THIS METHOD SERVER-SIDE TO REMOVE ENTITIES!</b>
	 * Instead mark the entity for deletion via entity.setRemoved()
	 * and it will be deleted on the next tick.
	 */
	public void removeEntity(Entity e){
		entities.remove(e);
	}

	public Entity getEntity(long id)
	{
		for (Entity entity : entities)
			if (entity.getID() == id)
				return entity;

		return null;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void spawnEntities() {
		for(SpawnPoint point : spawnPoints){
			if(!point.isOccupied()){
				double RNG = Math.random()*2;
				if(RNG>=1){ this.addEntity(EntityFactory.genZombieEntity(point.getX(), point.getY()));
				} else { this.addEntity(EntityFactory.genNPC(point.getX(), point.getY())); }
			}
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
