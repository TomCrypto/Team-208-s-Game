package dataStorage;

import java.util.*;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import ecs.entity.*;
import ecs.components.*;
/**
 * Handles the marshalling and unmarshalling of the Entity class
 * 
 * @author Nainesh Patel (300232221)
 *
 */
public class EntityAdapter extends XmlAdapter<AdaptedEntity, Entity>{

	@Override
	public AdaptedEntity marshal(Entity entity) throws Exception {
		if (entity == null) return null;
		AdaptedEntity adaptedEntity = new AdaptedEntity();
		if(entity!= null){
			adaptedEntity.setID(entity.getID());
			adaptedEntity.setComponentList(entity.getComponents());
		}
		return adaptedEntity;
	}

	@Override
	public Entity unmarshal(AdaptedEntity adaptedEntity) throws Exception {
		Entity entity = new Entity(adaptedEntity.getID());
		Collection<Component> components = adaptedEntity.getComponentList();
		Map<Class<? extends Component>, Component> map = adaptedEntity.getComponents();
		
		for(Component comp: components){
			entity.addComponent(comp);
		}

		return entity;
	}

}
