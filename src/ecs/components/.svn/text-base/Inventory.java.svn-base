package ecs.components;

import java.util.*;

import javax.xml.bind.annotation.XmlElement;

import ecs.entity.*;

/** A component that holds Entities (such that can be stored)
 * Might change it to a list of meta components or something
 *
 * @author Lord Mumford
 *
 */
public class Inventory extends Component {

	private static final long serialVersionUID = 1L;

	private List<Entity> inv; // A set of entities
	private int maxSize; // The max capacity of the inventory
	private int current; // current capacity

	/** Creates an inventory component, specifiying the max size
	 *
	 * @param maxSize
	 */
	public Inventory(int maxSize){
		inv = new ArrayList<Entity>();
		this.maxSize = maxSize;
		current = 0;
	}

	/** Default inventory with size 100
	 *
	 */
	public Inventory(){
		inv = new ArrayList<Entity>();
		this.maxSize = 100;
		current = 0;
	}

	/** Checks if an item can be added based on its size
	 *
	 * @return True if can fit, false otherwise
	 */
	public boolean canAdd(int size){
		return current + size < maxSize;
	}


	public int getCurrent() {
		return current;
	}

	/** Attempts to add an entity to inventory. Returns false if unsuccessful, true if
	 * successful. Depends on how full the inventory is, as well as an entity must have
	 * a Size component
	 *
	 * @param e Entity to add
	 * @return True if successful, false otherwise
	 */
	public boolean addEntity(Entity e){
		// Invariants for entity e
		if(!e.hasAll(Size.class)){
			return false; // Must have a size component
		}
		int size = e.getComponent(Size.class).getSize();
		if(canAdd(size)){
			inv.add(e); // Add to the inventory, increase current
			current = current + size;
			markChanged();
			return true;
		} else {
			return false;
		}
	}

	/** Returns the inventory
	 *
	 * @return Inventory of entities (list)
	 */
	@XmlElement(name="inv")
	public List<Entity> getInventory() {
		return inv;
	}

	/** Remove a set of entities and decreases the current size
	 *
	 * @param ents Entities to remove
	 */
	public void removeEntities(Set<Entity> ents){
		int i = 0;
		for(Entity ent : ents){
			if(ent.hasAll(Size.class)){
				i+= ent.getComponent(Size.class).getSize();
			}
		}
		inv.removeAll(ents);
		current-=i;
		markChanged();
	}

	/** Remove a single entity
	 *
	 * @param ent Entity to remove
	 */
	public void removeEntity(Entity ent){
		int i = inv.indexOf(ent);
		inv.remove(i);
		current-=ent.getComponent(Size.class).getSize();
		markChanged();
	}


	public void setInventory(List<Entity> inv){
		this.inv = inv;
	}

	@XmlElement
	public int getMaxSize(){
		return maxSize;
	}

	public void setMaxSize(int max){

		maxSize = max;
		markChanged();

	}

	@Override
	public String toString()
	{
		return String.format("Inventory -> [maxSize = %d, current = %d]", maxSize, current) ;
	}

	public Entity getItem(float ID) {
		for(Entity e : inv){
			if(e.getID()==ID){

				return e;
			}
		}
		return null;
	}

}
