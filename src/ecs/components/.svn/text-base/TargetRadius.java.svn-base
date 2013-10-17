package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Target radius can be used as to how close an entity needs to be in order for
 * the entity with the target radius to target it
 *
 * @author Lord Mumford
 *
 */
public class TargetRadius extends Component {

	private static final long serialVersionUID = 1L;

	private double radius;

	public TargetRadius(double radius){
		this.radius = radius;
	}

	public TargetRadius(){}

	@XmlElement
	public double getRadius(){
		return radius;
	}
	
	public void setRadius(double r){
		this.radius = r;
	}
	
	@Override
	public String toString()
	{
		return String.format("TargetRadius -> [radius = %.2f]", radius);
	}
}
