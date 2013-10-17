package ecs.entity;

import java.io.Serializable;
import java.util.*;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ecs.components.*;

import dataStorage.EntityAdapter;

/** An entity doesn't use inheritance. Rather, it is a construction
 * of components. For example, a horse is not an equine, a horse is
 * two hooves, a head, a torso, etc.
 *
 * @author mumforpatr
 *
 */
@XmlJavaTypeAdapter(EntityAdapter.class)
@XmlTransient
public class Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>(); // An entity is a sum of its parts

	private final long ID;

	private transient boolean toRemove = false; // If it's marked for removal, there should be some system to clear it up after processing

	//@XmlElement
	public long getID(){
		return ID;
	}

	/**
	 * Returns the entity's name, if it has a Name component (for instance, players, weapons).
	 * @return
	 */
	public String getName()
	{
		if (hasAll(Name.class)) return getComponent(Name.class).getName();
		else return null;
	}

	public Entity()
	{
		ID = IDProvider.generateID();
	}

	// bypass method for client to insert sent entities
	public Entity(long ID)
	{
		this.ID = ID;
	}

	public <T extends Component> T getComponent(Class<T> component)
	{
		return component.cast(components.get(component));
	}

	public void addComponent(Component comp) {
		components.put(comp.getClass(), comp);
	}

	//@XmlElement(name="component")
	public Set<Component> getComponents()
	{
		return new HashSet<Component>(components.values());
	}

	public HashMap<Class<? extends Component>, Component> getComponentMap(){
		return (HashMap<Class<? extends Component>, Component>)components;
	}

	public void addComponentMap(Map<Class<? extends Component>, Component> comp){
		components = comp;
	}

	public void clearRemoved()
	{
		toRemove = false;
	}

	/**
	 * Returns the components which have been modified since
	 * this method was last called, then marks them as unchanged.
	 */
	public Set<Component> clearModifiedComponents()
	{
		Set<Component> modified = new HashSet<Component>();

		for (Component component : components.values())
			if (component.hasChanged())
			{
				modified.add(component);
				component.clearChanges();
			}

		return modified;
	}

	/**
	 * Returns the components of this entity marked for deletion
	 * and then removes them from the entity.
	 */
	public Set<Class<? extends Component>> clearRemovedComponents()
	{
		Set<Class<? extends Component>> removed = new HashSet<Class<? extends Component>>();

		Iterator<Component> iter = components.values().iterator();

		while (iter.hasNext())
		{
			Component component = iter.next();
			if (component.isRemoved())
			{
				removed.add(component.getClass());
				iter.remove();
			}
		}

		return removed;
	}

	public boolean isRemoved()
	{
		return toRemove;
	}

	public void setRemoved()
	{
		toRemove = true;
	}

	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();
		string.append(getType());
		string.append(" <");
		string.append(Long.toHexString(ID));
		string.append(">");
		string.append("\n----------\n");

		for (Component component : components.values())
		{
			string.append(component);
			string.append("\n");
		}

		return string.toString();
	}

	/**
	 * <b>DO NOT USE THIS METHOD SERVER-SIDE TO REMOVE COMPONENTS!</b>
	 * Instead mark the component for deletion via component.setRemoved()
	 * and it will be deleted on the next tick.
	 */
	public void removeComponent(Class<? extends Component> toRemove) {
		components.remove(toRemove);
	}

	/**
	 * Checks an entity for  the existence of a
	 * set of components.
	 *
	 * @param classes Components to check for.
	 *
	 * @return Returns {@code true} if and only
	 * if this entity has  an instance of every
	 * component in {@code classes}.
	 */
	public boolean hasAll(Class<? extends Component>... classes)
	{
		for (Class<? extends Component> componentClass : classes)
			if (!components.containsKey(componentClass)) return false;

		return true;
	}

	/**
	 * Checks an entity for  the existence of a
	 * set of components.
	 *
	 * @param classes Components to check for.
	 *
	 * @return Returns {@code true} if and only
	 * if  this entity  has an  instance of  at
	 * least one component in {@code classes}.
	 */
	public boolean hasOne(Class<? extends Component>... classes)
	{
		for (Class<? extends Component> componentClass : classes)
			if (components.containsKey(componentClass)) return true;

		return false;
	}

	public Type.EntityType getType()
	{
		if (hasAll(Type.class))
			return getComponent(Type.class).getType();
		else
			return null;
	}
}
