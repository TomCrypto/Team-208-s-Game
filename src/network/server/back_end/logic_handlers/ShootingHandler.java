package network.server.back_end.logic_handlers;

import java.util.HashSet;
import java.util.Set;

import org.jbox2d.dynamics.Body;

import network.NetClient;
import network.packet.action.PlayerShootAction;
import ecs.components.DamageFactor;
import ecs.components.Equipped;
import ecs.components.EventTrigger;
import ecs.components.Name;
import ecs.components.Position;
import ecs.components.Target;
import ecs.components.TargetRadius;
import ecs.components.Velocity;
import ecs.components.Volume;
import ecs.components.Type.EntityType;
import ecs.entity.Entity;
import ecs.helpers.EntityFactory;
import ecs.world.World;

/** Handles player shooting
 *
 * @author mumforpatr
 *
 */
public class ShootingHandler {

	/** Handles the player shooting. Creates bullets and determines whether or not a player can fire, how powerful it is etc
	 *
	 * @param client
	 * @param packet
	 * @param world
	 */
	public static void handleEvent(NetClient client, PlayerShootAction packet, World world) {
		// Create a bullet entity based on weapon
		Entity player = client.getPlayer(); // Player shooting

		Equipped equip = player.getComponent(Equipped.class); // Player weapon
		if(!equip.hasEquipped()){ return; } // If no weapon equipped, do nothing
		Entity weapon = equip.getEquipped();
		if(equip.getEquipped().getName().equals("None")){ return; }
		if((player.getComponent(EventTrigger.class)).canFire(System.currentTimeMillis())){ // If can fire
			Position playerPos = player.getComponent(Position.class);
			double mag = playerPos.getDistance(new Position(packet.x, packet.y)); // Get magnitude from player to click
			// Get directional unit vector (to find where bullet is going)
			double x = (packet.x - playerPos.getX())/mag;
			double y = (packet.y - playerPos.getY())/mag;
			Velocity bulletV = new Velocity(x*1.5f, y*1.5f); // Set velocity of bullet
			double offset = (player.getComponent(Volume.class).getWidth() / 2);
			// Create bullet position relative to player
			Position bulletPos = new Position(playerPos.getX() + (offset + 0.013)*x, playerPos.getY() + (offset + 0.013)*y);
			Entity bullet = getBullet(weapon, player, bulletV, bulletPos); // Generate a bullet based on weapon and player
			client.getLocation().addEntity(bullet); // Add bullet to the location
		}

	}

	/** Generates a bullet based on the player and what he/she has equipped
	 *
	 * @param weapon
	 * @param player
	 * @param vel
	 * @param pos
	 * @return
	 */
	private static Entity getBullet(Entity weapon, Entity player, Velocity vel, Position pos) {
		// Calculate bullet damage
		int damage = weapon.getComponent(DamageFactor.class).getDamageFactor() + player.getComponent(DamageFactor.class).getDamageFactor();
		// Generate a new bullet based on this damage, position and velocity
		Entity bullet = EntityFactory.genBulletEntity(new DamageFactor(damage), 0.005f, pos, vel);
		// Check if the weapon it spawns from has a suffix to give bullet special behaviour
		if(weapon.getComponent(Name.class).hasSuffix()){
			String suffix = weapon.getComponent(Name.class).getSuffix();
			bullet.addComponent(weapon.getComponent(Name.class)); // Add a name to th ebullet
			if(suffix.equalsIgnoreCase("Homing")){ // Homing bullets need a targetting component
				Set<EntityType> targets = new HashSet<EntityType>();
				targets.add(EntityType.ZOMBIE);
				bullet.addComponent(new TargetRadius(0.5f));
				bullet.addComponent(new Target(targets));
			}
		}
		return bullet;

	}

}
