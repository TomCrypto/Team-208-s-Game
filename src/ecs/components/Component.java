package ecs.components;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/** A component that makes up an entity. Components are serialized in order to be passed through the network.
 *
 * @author Lord Mumford
 *
 */
@XmlSeeAlso({Volume.class, DamageFactor.class, Equipped.class, EventTrigger.class, Exit.class, Health.class, Inventory.class,
	ModelData.class, Multiplier.class, Name.class, Position.class, Size.class, Target.class, TargetRadius.class,
	Text.class, Type.class, Upgrades.class, Velocity.class, Worth.class})
@XmlTransient
public abstract class Component implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient boolean changed = true, removed = false;

	/** To indicate a component has changed and needs to be updated
	 * it is marked as changed
	 *
	 */
	public void markChanged()
	{
		changed = true;
	}

	/** Returns whether or not this component has been modified
	 *
	 * @return Whether or not it's changed
	 */
	public boolean hasChanged()
	{
		return changed;
	}

	/** Set the component as unchanged
	 *
	 */
	public void clearChanges()
	{
		changed = false;
	}

	/** Set the component to removed, so that it is cleaned up elsewhere
	 *
	 */
	public void setRemoved()
	{
		removed = true;
	}

	/** Indicate if a component is marked for removal
	 *
	 * @return If marked for removal
	 */
	public boolean isRemoved()
	{
		return removed;
	}
}
