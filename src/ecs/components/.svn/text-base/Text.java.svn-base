package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Text can be anything, from a description of an item to NPC dialog. The behavior is determined
 * by the systems that process the components and entities
 *
 * @author mumforpatr
 *
 */
public class Text extends Component {

	private static final long serialVersionUID = 1L;

	private String text;

	public Text(){
		text = "";
	}

	public Text(String text){
		this.text = text;
	}

	@XmlElement
	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text = text;
	}

	@Override
	public String toString()
	{
		return String.format("Text -> [text = %s]", text);
	}

	/** Some random dialog that an NPC might say
	 *
	 * @return Random dialog
	 */
	public static String genDialog() {
		String[] name = new String[]{
				"Careful. I hear there are zombies around",
				"Hello my friend. What would you like?",
				"Wanna see something special?",
				"Please don't talk to me right now",
				"I hate this!",
				"Bit cold out, isn't it?",
				"The 'd' is silent",
				"...",
				"I think zombies are kinda cute",
				"Please don't shoot me",
				"           CHEAT ENABLED" };
		int i = (int) (Math.random()*name.length);
		return name[i];
	}
}
