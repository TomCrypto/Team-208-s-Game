package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Worth can be anything (of worth). For instance, it's worth could represent
 * money, points, etc.
 *
 * @author Lord Mumford
 *
 */
public class Worth extends Component {

	private static final long serialVersionUID = 1L;

	private int amount;

	public Worth(int amount){
		this.amount = amount;
	}

	/** Default worth constructor of 10
	 *
	 */
	public Worth(){
		amount = 10;
	}

	@XmlElement
	public int getWorth(){
		return amount;
	}

	public void setWorth(int amount){
		this.amount = amount;
		markChanged();
	}

	/** Adds a certain amount of worth to this component
	 *
	 * @param cost Amount to add
	 */
	public void addWorth(int cost){
		this.amount+= cost;
		markChanged();
	}

	@Override
	public String toString()
	{
		return String.format("Worth -> [amount = %d]", amount);
	}

	/** Subtracts a certain amount of worth to this component
	 *
	 * @param cost Amount to subtract
	 */
	public void decreaseWorth(int cost) {
		this.amount-= cost;
		markChanged();
	}
}
