package network.server.back_end.logic_handlers;

import java.util.HashSet;
import java.util.Set;

import network.NetClient;
import network.packet.ActionPacket;
import network.packet.message.PrivateMessage;
import ecs.components.Equipped;
import ecs.components.EventTrigger;
import ecs.components.Exit;
import ecs.components.Health;
import ecs.components.Inventory;
import ecs.components.Multiplier;
import ecs.components.Position;
import ecs.components.Size;
import ecs.components.Target;
import ecs.components.Text;
import ecs.components.Type;
import ecs.components.Worth;
import ecs.components.Type.EntityType;
import ecs.entity.Entity;
import ecs.helpers.EntityFactory;
import ecs.world.Location;
import ecs.world.World;

/** Handlers user interactions with objects in the world
 * It works by working on what is targetted
 *
 * @author mumforpatr
 *
 */
public class InteractionHandler {

	/** Handle event based on a packet that a client has sent
	 *
	 * @param client
	 * @param packet
	 * @param world
	 */
	public static void handleEvent(NetClient client, ActionPacket packet, World world) {
		Entity player = client.getPlayer();


		Target target = player.getComponent(Target.class);
		if(target.getTarget()!=null && target.getTarget().hasAll(Type.class)){
			// TODO: Change this to be more detailed. add more logic
			EntityType type = target.getTarget().getType();
			if(type==EntityType.ITEM || type==EntityType.KEY || type==EntityType.HEALTH_POTION){
				takeItem(client, player, target.getTarget());
			} else if (type==EntityType.PORTAL && target.getTarget().hasAll(Exit.class)){
				usePortal(player, target.getTarget(), world, client);
			} else if (type==EntityType.MONEY && target.getTarget().hasAll(Position.class)){
				// This shouldn't be called since money is automatically picked up. This is just in case
				takeMoney(player, target.getTarget());
			} else if (type==EntityType.WEAPON){ //&& target.getTarget().hasAll(Position.class)){
				takeWeapon(client, player, target.getTarget());
			} else if (type==EntityType.CONTAINER){
				openContainer(client, player, target.getTarget());
			} else if (type==EntityType.NPC){
				talk(client, player, target.getTarget());
			}
		}
	}

	/** Take a targetted item and put it in the inventory
	 *
	 * @param client
	 * @param player
	 * @param target
	 * @return
	 */
	private static boolean takeItem(NetClient client, Entity player, Entity target) {
		Inventory inv = player.getComponent(Inventory.class);
		if(!target.hasAll(Size.class)){
			return false;
			}
		if(inv.getInventory().size()>=6){
			client.sendPacket(new PrivateMessage(client.getName(), client.getName(), "Your inventory is full!"));
			return false;
		}
		boolean added = inv.addEntity(target);
		if(added && target.hasAll(Position.class)){
			target.getComponent(Position.class).setRemoved();
		} else if (!added){
			client.sendPacket(new PrivateMessage(client.getName(), client.getName(), "The item is too heavy!"));
		}
		return added;
	}

	/** Use a portal and go to a new location
	 *
	 * @param player
	 * @param target
	 * @param world
	 * @param client
	 */
	private static void usePortal(Entity player, Entity target, World world, NetClient client) {
		Exit exit = target.getComponent(Exit.class);
		String loc = exit.getExit();
		// Find the location
		Location exitLoc = null; // Where the portal leads to
		for(Location loca : world.getLocations()){
			if(loca.getName().equals(loc)){
				exitLoc = loca;
			}
		}
		if(exitLoc!=null){
			// Check if the player has a key
			if(exit.requiresKey()){
				Inventory inv = player.getComponent(Inventory.class);
				boolean hasKey = false;
				for(Entity item : inv.getInventory()){
					if(item.hasAll(Type.class) && item.getType()==EntityType.KEY){ // Check player has key
						String opens = item.getComponent(Exit.class).getExit();
						if(opens.equals(exit.getExit())){
							hasKey= true;
							exit.setLocked(false);
						}
					}
				}
				if(!hasKey){
					client.sendPacket(new PrivateMessage(client.getName(), client.getName(), "You require the correct key"));
					return;
				}
			}
			Location oldLoc = client.getLocation();
			client.changeLocation(exitLoc);
			world.getPhysics().teleportEntity(client.getLocation(), player, (float)exit.getExitPos().getX(), (float)exit.getExitPos().getY());

			if(exitLoc.getName().equals("Starting location")){
				player.getComponent(Health.class).setCurrent(player.getComponent(Health.class).getMax());
				if(player.getComponent(Equipped.class).getEquipped().getName().equals("None")){
					player.getComponent(Equipped.class).equip(EntityFactory.genDefaultWeapon("Shotgun"));
				}
			}

			client.sendPacket(new PrivateMessage(client.getName(), client.getName(), "You have moved to : " + exitLoc.getName()));
			//client.sendPacket(new PrivateMessage(client.getName(), client.getName(), exitLoc.getDescription()));

			if(oldLoc.getName().equals("Key Room")){
				return; // Don't want to change the key room
			}
			// If all players leave, clear the room
			boolean hasPlayers = false;
			for(Entity ent : oldLoc.getEntities()){
				if(ent.getType()==EntityType.PLAYER && !ent.isRemoved()){
					hasPlayers = true;
				}
			}
			if(!hasPlayers){
				for(Entity ent : oldLoc.getEntities()){
					if(ent.getType()==EntityType.ZOMBIE || ent.getType()==EntityType.NPC){
						ent.setRemoved();
					}
				}
			}
			for(Entity ent : exitLoc.getEntities()){
				if(ent.getType()==EntityType.PLAYER){
					return;
				}
			}
			exitLoc.spawnEntities();

		}
	}

	/** Take money from the ground. Shouldn't be necessary
	 *
	 * @param player
	 * @param coin
	 */
	private static void takeMoney(Entity player, Entity coin) {
		int coinWorth = coin.getComponent(Worth.class).getWorth();
		if(!player.hasAll(Worth.class)){ // If it doesn't have any money, add 0 money
			player.addComponent(new Worth(coinWorth));
		} else {
			Worth playerCoin = player.getComponent(Worth.class);
			playerCoin.addWorth(coinWorth);

		}
		if(coin.hasAll(Position.class)){
			coin.getComponent(Position.class).setRemoved();
		}
		//coin.setRemoved();
	}

	/** Take a weapon. If no weapon equipped, then equip one
	 *
	 * @param client
	 * @param player
	 * @param target
	 * @return
	 */
	private static boolean takeWeapon(NetClient client, Entity player, Entity target) {
		Equipped equip = player.getComponent(Equipped.class);
		if(equip.getEquipped().getName().equals("Shotgun")){
			if(target.hasAll(Position.class)){
				target.getComponent(Position.class).setRemoved();
			}
			equip.equip(target);
			EventTrigger playerROF = player.getComponent(EventTrigger.class);
			EventTrigger weapROF = target.getComponent(EventTrigger.class);
			Multiplier multiplier = player.getComponent(Multiplier.class);
			playerROF.setInterval((long) (weapROF.getInterval()*multiplier.getMultiplier()));
			return true;
		} else {
			return takeItem(client, player, target);
		}
	}

	/** Open a container and retrieve its contents
	 *
	 * @param client
	 * @param player
	 * @param container
	 */
	private static void openContainer(NetClient client, Entity player, Entity container) {
		Inventory inv = container.getComponent(Inventory.class);
	//	System.out.println(inv);
		Set<Entity> toRemove = new HashSet<Entity>();
		for(Entity item : inv.getInventory()){
			EntityType type = item.getType();
			if(type==EntityType.MONEY){
				takeMoney(player, item);
				toRemove.add(item);
			} else if (type==EntityType.WEAPON){
				if (takeWeapon(client, player, item)){
					toRemove.add(item);
				}
			} else if (type==EntityType.ITEM || type==EntityType.KEY || type==EntityType.HEALTH_POTION){

				if(takeItem(client, player, item)){
					toRemove.add(item);
				}
			}
		}
		inv.removeEntities(toRemove);
	}

	/** Talk to an NPC
	 *
	 * @param client
	 * @param player
	 * @param target
	 */
	private static void talk(NetClient client, Entity player, Entity target) {
		String message = target.getName() + ": " + target.getComponent(Text.class).getText();
		client.sendPacket(new PrivateMessage(client.getName(), client.getName(), message));
	}


}
