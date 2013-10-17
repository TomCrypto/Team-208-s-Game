package ecs.world;

import javax.xml.bind.annotation.XmlElement;

public class SpawnPoint {

	private float x;
	private float y;
	private boolean occupied;

	public SpawnPoint() { }
	
	public SpawnPoint(float x, float y){
		this.x = x;
		this.y = y;
		this.occupied = false;
	}

	@XmlElement
	public float getX(){
		return x;
	}

	@XmlElement
	public float getY(){
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public boolean isOccupied(){
		return occupied;
	}

	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
	
	@XmlElement
	public boolean getOccupied()
	{
		return occupied;
	}
}
