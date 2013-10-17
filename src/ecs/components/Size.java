package ecs.components;

import javax.xml.bind.annotation.XmlElement;

/** Specifies a size. Only an object with a size can be put into an inventory,
 * if you choose to have the game work like that
 *
 *
 * @author Lord Mumford
 *
 */
public class Size extends Component{

	private static final long serialVersionUID = 1L;

	private int size;

	public Size(int size){
		this.size = size;
	}

	public Size(){
		size = 1;
	}

	@XmlElement
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	@Override
	public String toString()
	{
		return String.format("Size -> [size = %d]", size);
	}
}
