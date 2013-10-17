package network.packet.action;

import network.packet.*;

public class UpgradeAction implements ActionPacket {

	private static final long serialVersionUID = 1L;

	public final int cost;
	public final String type;

	public UpgradeAction(int cost, String type) {
		this.cost = cost;
		this.type = type;
	}

	@Override
	public Type getType() {
		return Type.ACTION;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.UPGRADE;
	}





}
