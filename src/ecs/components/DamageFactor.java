package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** How much damage is done to something else is determined by damage factor
 *
 * @author Lord Mumford
 *
 */
public class DamageFactor extends Component {

	private static final long serialVersionUID = 1L;

	private int damage;

	/** Default damage constructor with dmg = 10
	 *
	 */
	public DamageFactor(){
		damage = 10;
	}

	public DamageFactor(int damage) {
		this.damage = damage;
	}

	@XmlElement
	public int getDamageFactor(){
		return damage;
	}

	public void setDamageFactor(int damageFactor){
		damage = damageFactor;
	}

	@Override
	public String toString()
	{
		return String.format("DamageFactor -> [damage = %d]", damage);
	}
}
