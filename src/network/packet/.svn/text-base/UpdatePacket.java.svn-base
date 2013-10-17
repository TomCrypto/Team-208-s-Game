package network.packet;

import java.util.*;

import ecs.entity.*;
import ecs.components.*;

/**
 * An update packet encodes a change in an entity. For instance,
 * its components have been modified/deleted, or itself has been
 * deleted.
 *
 * @author Thomas Beneteau (300250968)
 */
public class UpdatePacket implements NetPacket
{
	private static final long serialVersionUID = 1L;

	@Override
	public Type getType()
	{
		return Type.UPDATE;
	}

	/**
	 * The kind of update to be performed.
	 */
	public enum Kind
	{
		/**
		 * This packet submits a new or modified entity.
		 */
		SUBMIT,

		/**
		 * This packet deletes an existing entity.
		 */
		DELETE,
	}

	public final Kind kind;
	public final Long entityID;
	public final Set<Component> modified;
	public final Set<Class<? extends Component>> removed;

	public UpdatePacket(Kind kind, Long entityID,
						Set<Component> modified, Set<Class<? extends Component>> removed)
	{
		this.kind = kind;
		this.entityID = entityID;
		this.modified = modified;
		this.removed = removed;
	}
	
	/* Helper methods below. */

	public static UpdatePacket entityCreated(Entity entity)
	{
		return new UpdatePacket(Kind.SUBMIT, entity.getID(), entity.getComponents(), null);
	}

	public static UpdatePacket entityDeleted(Entity entity)
	{
		return new UpdatePacket(Kind.DELETE, entity.getID(), null, null);
	}

	public static UpdatePacket componentsModified(Entity entity, Set<Component> modified)
	{
		return new UpdatePacket(Kind.SUBMIT, entity.getID(), modified, null);
	}

	public static UpdatePacket componentsRemoved(Entity entity, Set<Class<? extends Component>> removed)
	{
		return new UpdatePacket(Kind.SUBMIT, entity.getID(), null, removed);
	}

	public static UpdatePacket componentsUpdated(Entity entity, Set<Component> modified, Set<Class<? extends Component>> removed)
	{
		return new UpdatePacket(Kind.SUBMIT, entity.getID(), modified, removed);
	}
}
