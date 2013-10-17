package network.server.back_end.logic_handlers;

import network.NetClient;
import network.packet.ActionPacket;
import network.packet.ActionPacket.ActionType;
import network.packet.action.DropAction;
import network.packet.action.UseAction;
import network.packet.message.PrivateMessage;
import ecs.components.*;
import ecs.components.Type.*;
import ecs.entity.Entity;
import ecs.world.World;

/** Handles inventory actions, such as use and drop
 *
 * @author mumforpatr
 *
 */
public class InventoryHandler {

	/** Handles a packet and behaves appropriately
	 *
	 * @param client
	 * @param packet
	 * @param world
	 */
	public static void handleEvent(NetClient client, ActionPacket packet, World world) {
		if(packet.getActionType()==ActionType.USE){
			handlePlayerUse(client, (UseAction) packet, world);
		} else if (packet.getActionType()==ActionType.DROP){
			handlePlayerDrop(client, (DropAction) packet, world);
		}
	}

	/** Drops an item on the ground
	 *
	 * @param client
	 * @param packet
	 * @param world
	 */
	private static void handlePlayerDrop(NetClient client, DropAction packet,
			World world) {
		Entity player = client.getPlayer();
		Inventory inv = player.getComponent(Inventory.class);
		int index = -1;
		Entity item = inv.getItem(packet.ID);
		if(item!=null){
			Position playerPos = player.getComponent(Position.class);
			item.addComponent(new Position(playerPos.getX(), playerPos.getY()));
			inv.removeEntity(item);
		}
	}

	/** Uses an item if possible
	 *
	 * @param client
	 * @param packet
	 * @param world
	 */
	private static void handlePlayerUse(NetClient client, UseAction packet,
			World world) {
		Entity player = client.getPlayer();
		Inventory inv = player.getComponent(Inventory.class);
		Entity item = inv.getItem(packet.ID);

		if(item.hasAll(Type.class)){
			EntityType type = item.getType();
			switch (type){
				case WEAPON:
					equipNew(client, player, item);
					break;
				case ITEM:
					client.sendPacket(new PrivateMessage(client.getName(), client.getName(), "You can't do anything with this"));
					break;
				case HEALTH_POTION:
					drinkPotion(client, player, item);
					break;

				default:
					// nothing
			}
		}

		// Do a check here for items that aren't removed
	//	item.setRemoved();
	//	Set<Entity> toRemove = new HashSet<Entity>();
	//	toRemove.add(item);
	//	inv.removeEntities(toRemove);
	}


	/** Equips a new weapon from inventory
	 *
	 * @param client
	 * @param player
	 * @param entity
	 */
	private static void equipNew(NetClient client, Entity player, Entity entity) {
		Equipped equip = player.getComponent(Equipped.class);
		Inventory inv = player.getComponent(Inventory.class);

		Entity oldEquipped = equip.getEquipped();
		inv.removeEntity(entity);
		equip.equip(entity);
		takeItem(client, player, oldEquipped);

		EventTrigger playerROF = player.getComponent(EventTrigger.class); // Change Rate of fire based on new weapon
		EventTrigger weapROF = entity.getComponent(EventTrigger.class);
		Multiplier multiplier = player.getComponent(Multiplier.class);
		playerROF.setInterval((long) (weapROF.getInterval()*multiplier.getMultiplier()));

	}

	/** Take an item and put in inventory
	 *
	 * @param client
	 * @param player
	 * @param target
	 * @return
	 */
	public static boolean takeItem(NetClient client, Entity player, Entity target) {
		Inventory inv = player.getComponent(Inventory.class);
		if(!target.hasAll(Size.class)){  // Can only add things with a size
			return false;
			}
		boolean added = inv.addEntity(target);
		if (!added) client.sendPacket(new PrivateMessage(client.getName(), client.getName(), "Your inventory is full"));
		else if (target.hasAll(Position.class)) target.getComponent(Position.class).setRemoved();

		return added;
	}

	/** Drink a potion and replinish health if not at full
	 *
	 * @param client
	 * @param player
	 * @param potion
	 */
	private static void drinkPotion(NetClient client, Entity player, Entity potion) {
		Health health = player.getComponent(Health.class);
		if(health.getCurrent() == health.getMax()){
			client.sendPacket(new PrivateMessage(client.getName(), client.getName(), "Your health is already full!"));
		} else {
			health.increase(10);
			player.getComponent(Inventory.class).removeEntity(potion);
			potion.setRemoved();
		}
	}


}
