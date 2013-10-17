package network.packet.action;

import network.packet.ActionPacket;

public class InteractAction implements ActionPacket {

	private static final long serialVersionUID = 1L;

	@Override
	public Type getType() {
		return Type.ACTION;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.INTERACT;
	}

}
