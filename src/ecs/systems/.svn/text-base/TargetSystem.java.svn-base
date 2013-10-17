package ecs.systems;

import ecs.components.Position;
import ecs.components.Target;
import ecs.components.TargetRadius;
import ecs.components.Type;
import ecs.components.Worth;
import ecs.components.Type.EntityType;
import ecs.entity.Entity;
import ecs.world.*;

/** Targetting system to determine what entities are targetting if they have a target component
 * They will target whatever is in their radius, and prioritise things that are closest to them.
 * If money is targetted, it is added to the player's worth
 *
 * @author mumforpatr
 *
 */
public class TargetSystem implements EntitySystem {

	@Override
	public void process(World world, Location location, double delta) {
		for(Entity ent : location.getEntities()){
			if (ent.hasAll(Target.class, TargetRadius.class)){
				Target target = ent.getComponent(Target.class);
				double radius = (ent.getComponent(TargetRadius.class)).getRadius();
				// Retrieve the targetting radius, then target the closest targetable entity (has position)
				//if(!target.hasTarget()){
				double dist = Double.MAX_VALUE; // Max distance
				for(Entity targEnt : location.getEntities()){
					if(targEnt.hasAll(Type.class, Position.class) && targEnt.getID()!=ent.getID()){
						EntityType type = targEnt.getType();
						if(target.targets(type)){ // If the entity can target this type of entity

							Position pos = targEnt.getComponent(Position.class);
							double disBetween = pos.getDistance(ent.getComponent(Position.class));
							if(disBetween < dist && disBetween < radius){
								dist = disBetween;
								target.setTarget(targEnt);
								target.hasChanged();
							}
							if (disBetween < radius && targEnt.getType()==EntityType.MONEY && ent.getType()==EntityType.PLAYER){
								// Pick up the money automatically
								takeMoney(ent, targEnt);
							}
						}
					}
				}
				if(dist>radius){ // i.e, Double.MAX_VALUE, nothing in range
					target.setTarget(null);
					target.hasChanged();
				}
			}


		}
	}

	/** If money is in range of a player, it is automatically added to the player's wealth
	 *
	 * @param player Player picking up
	 * @param coin Money
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
		coin.setRemoved();
	}
}

	// Add a map of types of entity to what they can or want to target

