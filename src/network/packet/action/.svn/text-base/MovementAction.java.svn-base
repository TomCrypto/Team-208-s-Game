package network.packet.action;

import network.packet.ActionPacket;

public class MovementAction implements ActionPacket
{
	private static final long serialVersionUID = 1L;

	@Override
	public Type getType()
	{
		return Type.ACTION;
	}
	
	@Override
	public ActionType getActionType()
	{
		return ActionType.MOVEMENT;
	}
	
	public enum Direction
	{
		UP, DOWN, LEFT, RIGHT
		
	}
	
	private Direction dir;
	
	public MovementAction(Direction dir)
	{
		this.dir = dir;
	}
	
	public Direction getDir()
	{
		return dir;
	}
}
