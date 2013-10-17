package ecs.components;

import javax.xml.bind.annotation.XmlElement;

import renderer.ModelType;

/**
 * This component holds all the information about a 3D model required when it is drawn,
 * plus some other methods required by the renderer.
 *
 * @author Marc Sykes
 *
 */

public class ModelData extends Component{

	private static final long serialVersionUID = 1L;

	private ModelType type;

	public ModelData(ModelType type){ this.type = type; }
	public ModelData(){ type = ModelType.BOX; }

	@XmlElement
	public String getName(){
		return type.toString();
	}

	public void setType(ModelType type){
		this.type = type;
	}
	
	public ModelType getType(){ return type; }
	
	@Override
	public String toString(){
		return String.format("ModelData -> [name = %s]", type.toString());
	}
}