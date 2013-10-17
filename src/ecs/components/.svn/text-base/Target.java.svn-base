package ecs.components;

import java.util.*;

import javax.xml.bind.annotation.XmlElement;

import ecs.components.Type.EntityType;
import ecs.entity.Entity;


/** An entity may have a target (which will be another entity)
 * An example of an entity that has a target is a zombie, which may target a player
 * A zombie would walk toward it's target (via position)
 * This component also specifies what can be targetted
 *
 * @author Lord Mumford
 *
 *
 */
public class Target extends Component {

	private static final long serialVersionUID = 1L;

	private Entity ent;
	private Set<EntityType> targettable;

	public Target(){
		// Entity starts off null
		this.targettable = new HashSet<EntityType>();
	}

	public Target(Set<EntityType> targettable){
		this.targettable = targettable;
	}

	public void addTargettable(EntityType type){
		targettable.add(type);
	}

	/** Retrieves the target of the component
	 *
	 */
	@XmlElement(nillable=true)
	public Entity getTarget(){
		return ent;
	}

	/** Sets the target
	 *
	 * @param e Entity to target
	 * @return
	 */
	public void setTarget(Entity e)
	{
		/*
		if (e != this.ent)
		{
			this.ent = e;
			if(ent != null){
				if(ent.getComponent(Type.class).getType() == EntityType.PLAYER){
					targettable.add(EntityType.PLAYER);
					targettable.add(EntityType.MONEY);
					targettable.add(EntityType.HEALTH_POTION);
					targettable.add(EntityType.ITEM);
					targettable.add(EntityType.PORTAL);
					targettable.add(EntityType.WEAPON);
					targettable.add(EntityType.CONTAINER);
					targettable.add(EntityType.NPC);
				}
				else if(ent.getComponent(Type.class).getType() == EntityType.ZOMBIE){
					targettable.add(EntityType.PLAYER);
					targettable.add(EntityType.NPC);
				}
				else if(ent.getComponent(Type.class).getType() == EntityType.NPC){
					targettable.add(EntityType.ZOMBIE);
				}
			}
		 */
		ent = e;
			markChanged();

	}

	public Set<EntityType> getTargettable(){
		return targettable;
	}

	public void setTargettable(Set<EntityType> targets){
		targettable = targets;
	}

	/** This should be called before getTarget, so that null
	 * checks are done by the Target component and not within the game
	 *
	 * @return True if has target, false otherwise
	 */
	public boolean hasTarget(){
		return ent!=null;
	}

	/** Removes the target (sets it to null)
	 *
	 */
	public void removeTarget(){
		ent = null;
		markChanged();
	}

	public boolean targets(EntityType type) {
		return targettable.contains(type);
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("Target -> [targettable = {");

		Iterator<EntityType> iter = targettable.iterator();

		while (iter.hasNext())
		{
			EntityType type = iter.next();
			str.append(type);
			if (iter.hasNext()) str.append(", ");
		}

		str.append("}, ent :: ");
		if (ent == null) str.append("nil");
		else
		{
			str.append(ent.getType());
			if (ent.getName() != null)
			{
				str.append(" (");
				str.append(ent.getName());
				str.append(")");
			}
		}
		str.append("]");

		return str.toString();
	}
}
