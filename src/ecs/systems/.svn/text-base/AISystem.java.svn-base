package ecs.systems;

import ecs.world.*;
import ecs.entity.*;
import ecs.components.*;
import ecs.components.Type.*;

/** Processes how AI behaves. Very basic AI that responds to what they have targetted
 *
 * @author Lord Mumford
 *
 */
public class AISystem implements EntitySystem {

	@Override
	public void process(World world, Location location, double delta) {
		for(Entity ent : location.getEntities()){ // Go through the location
			if(ent.hasAll(Type.class)){ // Check type
				EntityType type = ent.getType();
				if(type==EntityType.ZOMBIE || type==EntityType.NPC){ // If it's an NPC or Zombie
					doAI(world, location, ent); // Perform zombie AI
				} else if (type==EntityType.BULLET && ent.hasAll(Name.class) && ent.getComponent(Name.class).hasSuffix()){
					// If it's a bullet with the 'Homing' suffix
					if(ent.getComponent(Name.class).getSuffix().equalsIgnoreCase("Homing")){
						doAI(world, location, ent);
					}
				}
			}
		}
	}

	/** Perform the AI
	 *
	 * @param world World that AI is in
	 * @param location Location that the entity is in
	 * @param entity The Entity
	 */
	private void doAI(World world, Location location, Entity entity) {
		Position pos = entity.getComponent(Position.class);
		Target target = entity.getComponent(Target.class);

		if(target.hasTarget()){ // Check if entity has a target
			Entity ent2 = target.getTarget();
			// If the target doesn't have a position, remove the target and return
			if(!ent2.hasAll(Position.class)){ target.removeTarget(); return; }
			Position pos2 = ent2.getComponent(Position.class);

			double x = pos2.getX() - pos.getX();
			double y = pos2.getY() - pos.getY();
			double mag = Math.sqrt( (x*x) + (y*y) ); // Magnitude of vector between the two positions
			x /= mag;
			y /= mag;

			/*if(entity.getType()==EntityType.BULLET){ // HOMING BULLET
				x = x/1000;
				y = y/1000;
			}*/

			if (entity.getType() == EntityType.ZOMBIE) zombieAI(world, location, entity, target.getTarget(), mag, (float)x, (float)y);
			if (entity.getType() == EntityType.NPC) npcAI(world, location, entity, target.getTarget(), mag, (float)x, (float)y);
			if (entity.getType() == EntityType.BULLET) bulletAI(world, location, entity, target.getTarget(), mag, (float)x, (float)y);
		}
		else
		{
			//if (entity.getType() == EntityType.ZOMBIE) zombieAI(world, location, entity, null, 0, 0, 0);
			if (entity.getType() == EntityType.NPC) npcAI(world, location, entity, null, 0, 0, 0);
		}
	}



	private void bulletAI(World world, Location location, Entity bullet,
			Entity target, double mag, float x, float y) {
		if(bullet.getComponent(Name.class).hasSuffix() && bullet.getComponent(Name.class).getSuffix().equals("Homing")){
			world.getPhysics().displaceEntity(location, bullet, (float)(x * 0.00002f), (float)(y * 0.00002f));
		}
	}

	private void zombieAI(World world, Location location, Entity entity, Entity target, double distance, float x, float y)
	{
		if (target == null) return;

		if(distance<0.1)
		{
			attackTarget(world, location, entity, target);
		}else if(distance<0.2){
			world.getPhysics().displaceEntity(location, entity, (float)(x * 0.0002f), (float)(y * 0.0002f));
		} else {
			world.getPhysics().displaceEntity(location, entity, (float)(x * 0.0001f), (float)(y * 0.0001f));
		}
	}

	private void npcAI(World world, Location location, Entity entity, Entity target, double distance, float x, float y)
	{
		if (target == null) randomMovement(entity, world, location);
		else
		{
			if (target.getType() != EntityType.ZOMBIE) return;

			if (distance < 0.1)
			{
				world.getPhysics().displaceEntity(location, entity, x * 0.0007f, y * 0.0007f);
			}
			else
			{
				x = -x;
				y = -y;
				world.getPhysics().displaceEntity(location, entity, x * 0.0003f, y * 0.0003f);
			}
		}
	}

	/** After a certain interval an NPC will move in a random direction
	 *
	 * @param entity NPC
	 * @param world World that entity is in
	 * @param location Location entity is in
	 */
	private void randomMovement(Entity entity, World world, Location location) {
		if(entity.hasAll(EventTrigger.class)){
			EventTrigger trigger = entity.getComponent(EventTrigger.class);
			if(trigger.canFire(System.currentTimeMillis())){
				double x = (Math.random()*2) - 1;
				double y = (Math.random()*2) - 1;
				world.getPhysics().displaceEntity(location, entity, (float)(x * 0.0008f), (float)(y * 0.0008f));
			}
		}
	}

	/** Attacks its target if the event trigger determines it can (rate of fire).
	 * If attacking an NPC, it will try to get away
	 *
	 * @param world World zombie is in
	 * @param location Location zombie is in
	 * @param zombie The zombie
	 * @param other What it's attacking
	 */
	private void attackTarget(World world, Location location, Entity zombie, Entity other) {
		EventTrigger trigger = zombie.getComponent(EventTrigger.class);
		if(trigger.canFire(System.currentTimeMillis())){
			if(other.getType()==EntityType.PLAYER){
				other.getComponent(Health.class).decrease(10);
			} else if (other.getType()==EntityType.NPC){
				other.getComponent(Health.class).decrease(10);
				world.getPhysics().displaceEntity(location, other,
						(float)(((Math.random()*2)-1) * 0.003f), (float)(((Math.random()*2)-1) * 0.003f));
			}
		}
	}

}




