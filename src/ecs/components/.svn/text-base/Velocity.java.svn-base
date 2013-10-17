package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Specifies a velocity. Helper classes for calculating things
 * such as magnitude, unit vector etc will be contained here
 *
 * @author Lord Mumford
 *
 */
public class Velocity extends Component {

	private static final long serialVersionUID = 1L;

	private double x;
	private double y;

	public Velocity(double x, double y){
		this.x = x;
		this.y = y;
	}

	public Velocity(){
		x = 0;
		y = 0;
	}

	@XmlElement
	public double getX(){
		return x;
	}

	@XmlElement
	public double getY(){
		return y;
	}

	public void setX(double x){
		boolean changed = (Math.abs(this.x - x) > 1e-5);
		this.x = x;
		if (changed) markChanged();
	}

	public void setY(double y){
		boolean changed = (Math.abs(this.y - y) > 1e-5);
		this.y = y;
		if (changed) markChanged();
	}

	public void addX(double i){
		x += i;
		markChanged();
	}

	public void addY(double i){
		y += i;
		markChanged();
	}
	
	@Override
	public String toString()
	{
		return String.format("Velocity -> [x = %.2f, y = %.2f]", x, y);
	}
}
