package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** A health component for entities that can be damaged or regenerate health
 *
 * @author Lord Mumford
 *
 */
public class Health extends Component {

	private static final long serialVersionUID = 1L;

	private int maxHealth;
	private int currentHealth;

	/** Creates a health component with maximum health
	 *
	 * @param maxHealth
	 */
	public Health(int maxHealth){
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
	}

	/** Creates a health component with default maximum health as 100
	 *
	 */
	public Health(){
		maxHealth = 100;
		currentHealth = maxHealth;
	}

	/** Creates a health component with a specified currentHealth
	 *
	 * @param maxHealth
	 * @param currentHealth
	 */
	public Health(int maxHealth, int currentHealth){
		this.maxHealth = maxHealth;
		this.currentHealth = currentHealth;
	}

	/** Returns the maximum health of this component
	 *
	 * @return Max health
	 */
	@XmlElement
	public int getMax(){
		return maxHealth;
	}

	public void setMax(int max){
		maxHealth = max;
	}

	/** Returns the current health of this component
	 *
	 * @return getCurrent
	 */
	@XmlElement
	public int getCurrent(){
		return currentHealth;
	}

	/** Sets the current health of this component
	 * Used for loading the game
	 */
	public void setCurrent(int health){
		currentHealth = health;
		markChanged();
	}

	public void decrease(int i) {
		this.currentHealth = this.currentHealth - i;
		if(this.currentHealth < 0){ this.currentHealth = 0; }
		markChanged();
	}

	@Override
	public String toString()
	{
		return String.format("Health -> [maxHealth = %d, currentHealth = %d]", maxHealth, currentHealth);
	}

	public void increase(int i) {
		if(this.currentHealth + i < maxHealth){
			this.currentHealth = this.currentHealth + i;
			markChanged();
		}
	}
}
