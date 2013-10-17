package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Not all entities need a name. This also has some helper classes
 * for creating a badass randomized weapon names and NPC names
 *
 * @author Lord Mumford
 *
 */
public class Name extends Component {

	private static final long serialVersionUID = 1L;

	private String name;
	private String suffix;

	public Name(String name){
		this.name = name;
	}

	/** Default name constructor
	 *
	 */
	public Name(){
		name = "Jon Snow";
	}

	@XmlElement
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setSuffix(String suffix){
		this.name = this.name + " of " + suffix;
		this.suffix = suffix;
	}

	public String getSuffix(){
		return suffix;
	}

	/** Generates a random weapon made up of two parts
	 *
	 * @return A random weapon name
	 */
	public static String genWeaponName(){
		String[] first = new String[]{ "Awesome", "Bombtastic", "Peow", "Heavy", "Daring", "Silly", "Thoughtful",
				"Angry", "Striking", "BoomBoom", "Dizzy" };
		String[] second = new String[]{ "Machine", "Gun", "Device", "Burnanator", "Destroyer", "Boom", "Craziness",
				"Avenger", "Peow Peow" };
		int i = (int)(Math.random()*first.length);
		int j = (int)(Math.random()*second.length);
		return first[i] + " " + second[j];
	}

	/** Piercing: Bullets have high health
	 *  Homing: Bullets target zombies
	 *  Soothing: Heals players!
	 *  Suffix determines what behavior a weapon has
	 *
	 * @return A random suffix
	 */
	public static String genWeaponSuffix(){
		String[] suffix = new String[]{ "Soothing", "Longevity", "Homing" }; // "Piercing", "Longevity",Homing",
		int i = (int) (Math.random()*suffix.length);
		return suffix[i];
	}

	@Override
	public String toString()
	{
		return String.format("Name -> [name = %s]", name);
	}

	public boolean hasSuffix() {
		return suffix!=null;
	}

	/** Generates a random NPC name
	 *
	 * @return A random NPC name
	 */
	public static String genNPCName() {
		String[] name = new String[]{ "Guybrush", "Casius", "Hoodini", "Manal", "Dog", "Sir Pow", "Nick",
				"Marc", "Nainesh", "Thomas", "Patrick" };
		int i = (int) (Math.random()*name.length);
		return name[i];
	}
}
