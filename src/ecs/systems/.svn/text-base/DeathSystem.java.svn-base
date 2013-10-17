package ecs.systems;

import java.util.*;

import ecs.world.*;
import ecs.entity.*;
import ecs.components.*;
import ecs.components.Type.*;
import ecs.helpers.EntityFactory;

/** Determines what happens when an entity dies. This only happens to entities
 * that have a health component, and the current health is less than 0
 *
 * @author mumforpatr
 *
 */
public class DeathSystem implements EntitySystem {

	private Set<Entity> toAdd; // If entities are added after death, i.e dropping coins, they are added here

	public DeathSystem(){
		toAdd = new HashSet<Entity>();
	}

	@Override
	public void process(World world, Location location, double delta) {
		toAdd.clear(); // toAdd is cleared to avoid duplication
		for(Entity ent : location.getEntities()){
			if(ent.hasAll(Health.class) && ent.getComponent(Health.class).getCurrent()<=0){ // If has health and health <0
				if(ent.hasAll(Type.class)){
					EntityType type = ent.getType();
					switch(type){
						case ZOMBIE:
							zombieDeath(toAdd, ent);
							break;
						case PLAYER:
							playerDeath(world, ent);
							break;
						case BULLET:
							break; // Maybe if you wanted particle animation or something
						case NPC:
							npcDeath(ent);
							break;

						default:
							// nothing

					}
				}
			}

		}
		for(Entity ent : toAdd){
			location.addEntity(ent);
		}
	}

	/** When a player dies, it isn't removed here, as it is transported to the start location thus the client
	 * is changed. This merely penalises the player for dying by stripping some stuff
	 *
	 * @param world World the player is in
	 * @param player Player entity
	 */
	private void playerDeath(World world, Entity player) {
		player.getComponent(Worth.class).setWorth(0);
		player.getComponent(Equipped.class).equip(EntityFactory.genDefaultWeapon("None"));

	}

	/** When a zombie dies, it is set for removal, and it has a chance of dropping a weapon
	 * and drops a random amount of money
	 *
	 * @param toAdd Entities (drops) to add to the location
	 * @param zombie Zombie
	 */
	private void zombieDeath(Set<Entity> toAdd, Entity zombie) {
		zombie.setRemoved();
		if(zombie.hasAll(Position.class)){
			Position pos = zombie.getComponent(Position.class);
			toAdd.add(EntityFactory.genMoney((float)pos.getX(), (float)pos.getY(), (int)(Math.random()*50)));
			int RNG = (int)(Math.random()*0);
			if(RNG==0){
				Entity weapon = EntityFactory.genRandomWeapon();
				weapon.addComponent(new Position(pos.getX(), pos.getY()));
				weapon.addComponent(Volume.makeCircle(0.002f));
				toAdd.add(weapon);
			}
		}
	}

	/** When an npc dies, it is just removed
	 *
	 * @param npc NPC to remove
	 */
	private void npcDeath(Entity npc) {
		npc.setRemoved();
	}
}



