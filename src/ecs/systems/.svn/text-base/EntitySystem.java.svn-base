package ecs.systems;

import ecs.world.*;

/** An entity system processes enties in game logic. For instance, handle what happens during death,
 * handles AI, etc. A system should ideally be specific to a certain task, such as entity movement.
 *
 * @author mumforpatr
 *
 */
public interface EntitySystem {

	/** Process entities in the location specified
	 *
	 * @param world World that the entities are in
	 * @param location Location the entities are in
	 * @param delta Delta
	 */
	public void process(World world, Location location, double delta);

}
