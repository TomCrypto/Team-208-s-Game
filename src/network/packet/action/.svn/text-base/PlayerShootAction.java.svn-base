package network.packet.action;

import network.packet.ActionPacket;

public class PlayerShootAction implements ActionPacket {

	private static final long serialVersionUID = 1L;
	
	public final double x;
	public final double y;
	
	public PlayerShootAction(double x, double y){
		this.x = x;
		this.y = y;
	}

	@Override
	public Type getType() {
		return Type.ACTION;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.PLAYER_SHOOT;
	}

}
