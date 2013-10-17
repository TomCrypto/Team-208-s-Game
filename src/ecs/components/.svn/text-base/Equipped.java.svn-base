package ecs.components;

import javax.xml.bind.annotation.XmlElement;

import ecs.entity.Entity;

public class Equipped extends Component {

	private static final long serialVersionUID = 1L;

	private Entity equipped;

	/** Returns equipped entity
	 *
	 * @return
	 */
	@XmlElement
	public Entity getEquipped(){
		return equipped;
	}
	
	/** Set Equipped. For use with JAXB
	 * 
	 * @param equip
	 */
	public void setEquipped(Entity equip){
		equipped = equip;
	}

	/** Equip an entity
	 *
	 * @param ent
	 */
	public void equip(Entity ent){
		this.equipped = ent;
		markChanged();
	}

	/** Check if the entity has something equipped
	 *
	 * @return True if equipped, false otherwise
	 */
	public boolean hasEquipped() {
		return equipped!=null;
	}

	@Override
	public String toString()
	{
		if(hasEquipped()){
			if (equipped.getName() != null)
				return String.format("Equipped -> [equipped :: %s (%s)]", equipped.getType(), equipped.getName());
			else
				return String.format("Equipped -> [equipped :: %s]", equipped.getType());
		}
		else
			return "Equipped -> Nothing";
	}
}
