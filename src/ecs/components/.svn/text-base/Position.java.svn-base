package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Specifies a position by x y co-ordinates
 *
 * @author Lord Mumford
 *
 */
public class Position extends Component{

	private static final long serialVersionUID = 1L;

	private double x;
	private double y;

	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}

	public Position(){}

	@XmlElement
	public double getX(){
		return x;
	}

	@XmlElement
	public double getY(){
		return y;
	}

	public void setX(double x)
	{
		boolean changed = (Math.abs(this.x - x) > 1e-5);
		this.x = x;
		if (changed) markChanged();
	}

	public void setY(double y)
	{
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

	/** Returns distance between two position (centers)
	 *
	 * @param other Other position
	 * @return
	 */
	public double getDistance(Position other){
		double dx = x - other.getX();
		double dy = y - other.getY();
		double mag = Math.sqrt( (dx * dx) + (dy * dy) );
		return mag;
	}

	@Override
	public String toString()
	{
		return String.format("Position -> [x = %.2f, y = %.2f]", x, y);
	}
}
