package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** An exit is an indication as to where an entity may lead to and whether or not it
 * requires a key. It can also be attached to a key so that the key can open the exit
 * with the same location exit (exit)
 *
 * @author mumforpatr
 *
 */
public class Exit extends Component {

	private static final long serialVersionUID = 1L;

	private String exit; // Where the exit leads to
	private boolean requiresKey; // If it requires a key or not
	private Position exitPos;

	public Exit(String string, boolean locked, Position exitPos){
		this.exit = string;
		this.requiresKey = locked;
		this.exitPos = exitPos;
	}

	public Exit(){
		exit = "exiting";
	}

	@XmlElement
	public String getExit(){
		return exit;
	}

	public void setExit(String location){
		this.exit = location;
	}

	public boolean requiresKey() {
		return requiresKey;
	}

	@XmlElement
	public boolean getLocked(){
		return requiresKey;
	}

	public Position getExitPos(){
		return exitPos;
	}

	public void setExitPos(Position exitPos){
		this.exitPos = exitPos;
	}

	public void setLocked(boolean locked){
		requiresKey = locked;
	}

	@Override
	public String toString()
	{
		return String.format("Exit -> [exit = %s, requiresKey = %b]", exit, requiresKey);
	}
}
