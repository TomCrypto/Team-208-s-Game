package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Most, if not all entities should have an entity type
 * More types can be added here
 * Types are required for interaction behaviour between different entities
 *
 * @author Lord Mumford
 *
 */
public class Type extends Component {

	private static final long serialVersionUID = 1L;

	public enum EntityType{
		ZOMBIE,
		PLAYER,
		HEALTH_POTION,
		BULLET,
		WEAPON,
		WALL,
		MONEY,
		ITEM,
		PORTAL,
		KEY,
		CONTAINER,
		NPC,
		MODEL
	}

	private EntityType type;

	public Type(EntityType type){
		this.type = type;
	}

	public Type(){
		type = EntityType.PLAYER;
	}

	public EntityType getType(){
		return type;
	}

	public void setTypeString(String type){
		this.type = EntityType.valueOf(type);
	}

	@XmlElement(name="type")
	public String getTypeString(){
		return type.toString();
	}

	@Override
	public String toString()
	{
		return String.format("Type -> [type = %s]", type);
	}
}
