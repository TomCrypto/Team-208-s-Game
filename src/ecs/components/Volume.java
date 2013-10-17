package ecs.components;

import javax.xml.bind.annotation.XmlElement;


/** Bounding shape can at present be a circle (in which case it requires
 * a radius), or a rectangle
 *
 * @author Lord Mumford
 *
 */
public class Volume extends Component
{
	private static final long serialVersionUID = 1L;

	public enum VolumeType
	{
		BOX,
		CIRCLE,
	}

	private float width, height;
	private VolumeType type;

	public Volume()
	{

	}

	@XmlElement
	public VolumeType getType()
	{
		return type;
	}

	public void setType(VolumeType type)
	{
		this.type = type;
	}

	@XmlElement
	public float getWidth()
	{
		return width;
	}

	@XmlElement
	public float getHeight()
	{
		return height;
	}

	public void setWidth(float width)
	{
		this.width = width;
		markChanged();
	}

	public void setHeight(float height)
	{
		this.height = height;
		markChanged();
	}

	public Volume(float width, float height, VolumeType type)
	{
		this.type = type;
		this.width = width;
		this.height = height;
	}

	public static Volume makeCircle(float radius)
	{
		return new Volume(radius * 2, radius * 2, VolumeType.CIRCLE);
	}

	public static Volume makeBox(float width, float height)
	{
		return new Volume(width, height, VolumeType.BOX);
	}

	@Override
	public String toString()
	{
		return String.format("Volume -> [width = %.2f, height = %.2f, type = %s]", width, height, type);
	}
}
