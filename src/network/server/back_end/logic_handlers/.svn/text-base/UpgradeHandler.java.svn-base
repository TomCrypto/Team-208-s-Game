package network.server.back_end.logic_handlers;

import network.NetClient;
import network.packet.action.UpgradeAction;
import ecs.components.DamageFactor;
import ecs.components.Equipped;
import ecs.components.EventTrigger;
import ecs.components.Health;
import ecs.components.Inventory;
import ecs.components.Multiplier;
import ecs.components.TargetRadius;
import ecs.components.Upgrades;
import ecs.components.Worth;
import ecs.entity.Entity;
import ecs.world.World;

/** Handles upgrade requests
 *
 * @author mumforpatr
 *
 */
public class UpgradeHandler {

	/** Depending on what upgrade is chosen, perform the upgrade on the player
	 *
	 * @param client
	 * @param packet
	 * @param world
	 */
	public static void handleEvent(NetClient client, UpgradeAction packet, World world) {
		Entity player = client.getPlayer();
	//	System.out.println(packet.cost);
		player.getComponent(Worth.class).decreaseWorth(packet.cost);
		if(packet.type.equals("Health")){ // Upgrade health by ten
			Health health = player.getComponent(Health.class);
			health.setMax(health.getMax() + 10);
			player.getComponent(Upgrades.class).upgrade("Health");
		} else if (packet.type.equals("Weapon Strength")){ // Increase weapon strength by 2
			DamageFactor dmg = player.getComponent(DamageFactor.class);
			dmg.setDamageFactor(dmg.getDamageFactor() + 2);
			player.getComponent(Upgrades.class).upgrade("Weapon Strength");
		} else if (packet.type.equals("Rate of Fire")){ // Increase rate of fire
			Multiplier multi = player.getComponent(Multiplier.class);
			multi.setMultiplier(multi.getMultiplier()-0.05);
			// If a player has something equipped, have to update it with the multiplier
			Equipped equip = player.getComponent(Equipped.class);
			if(equip.hasEquipped()){
				EventTrigger playerROF = player.getComponent(EventTrigger.class);
				EventTrigger weapROF = equip.getEquipped().getComponent(EventTrigger.class);
				Multiplier multiplier = player.getComponent(Multiplier.class);
				playerROF.setInterval((long) (weapROF.getInterval()*multiplier.getMultiplier()));
			}
			player.getComponent(Upgrades.class).upgrade("Rate of Fire");
		} else if (packet.type.equals("Inventory Capacity")){
			player.getComponent(Inventory.class).setMaxSize(
					player.getComponent(Inventory.class).getMaxSize() +10);
		}
	}



}
