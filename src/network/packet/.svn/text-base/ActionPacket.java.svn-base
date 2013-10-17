package network.packet;

/**
 * Represents an action packet, which is an abstract
 * action performed by the player (e.g. move, shoot)
 *
 * @author Thomas Beneteau (300250968)
 */
public interface ActionPacket extends NetPacket
{
	public enum ActionType
	{
		MOVEMENT, // when the player moves
		INTERACT, // When player interacts with another entity
		PLAYER_SHOOT, // When player shoots
		USE,			// When player uses an item
		DROP,
		UPGRADE
	}

	public ActionType getActionType();
}
