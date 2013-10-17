package dataStorage;

import java.util.*;

import javax.xml.bind.annotation.XmlElement;

import ecs.components.Component;

/**
 * An adapted Entity for use with JAXB
 * 
 * @author Nainesh Patel (300232221)
 */
public class AdaptedEntity {
	private long ID;
	private String name;
	private HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();
	private Set<Component> componentSet = new HashSet<Component>();

	@XmlElement(nillable = true)
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}

	@XmlElement
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setComponents(HashMap<Class<? extends Component>, Component> comp){
		components = comp;
	}

	public HashMap<Class<? extends Component>, Component> getComponents(){
		return components;
	}

	@XmlElement(name="component")
	public Collection<Component> getComponentList(){
		return componentSet;
	}

	public void setComponentList(Set<Component> comp){
		componentSet = comp;
	}
}
