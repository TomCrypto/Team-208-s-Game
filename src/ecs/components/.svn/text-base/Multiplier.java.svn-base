package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** A multiplier is a piece of data that multiplies.
 *
 * @author mumforpatr
 *
 */
public class Multiplier extends Component {

	private static final long serialVersionUID = 1L;

	private double multiplier;

	public Multiplier(double multiplier){
		this.multiplier = multiplier;
	}

	public Multiplier(){ // Multiplier set to 1
		this.multiplier = 1;
	}

	@XmlElement
	public double getMultiplier(){
		return multiplier;
	}

	public void setMultiplier(double multiplier){
		this.multiplier = multiplier;
		markChanged();
	}
}
