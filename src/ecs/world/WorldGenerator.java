package ecs.world;

import ecs.helpers.EntityFactory;
import ecs.helpers.LocationFactory;

public class WorldGenerator {

	/**
	 * This method is called  if no world could
	 * be loaded  from a save file,  and should
	 * generate  a default  world to  start off
	 * with.
	 */
	public static World generateWorld() {
		World world = new World();

		// For now, the whole world will be in here
		Location start = world.getStartLocation();

		Location street = LocationFactory.getStreet();


		Location rand1 = LocationFactory.randomLocation();
		Location rand2 = LocationFactory.randomLocation();
		Location rand3 = LocationFactory.randomLocation();
		Location rand4 = LocationFactory.randomLocation();
		Location rand5 = LocationFactory.randomLocation();
		Location keyRoom = LocationFactory.keyRoom();

		start.addEntity(EntityFactory.genPortal("Streets", true, 0.5f, 1.05f, (-52+25.4f) * 0.02f, (-92.294f+52.8f) * 0.02f));

		// Leads to the starter safe room ( west, top)
		street.addEntity(EntityFactory.genPortal(start.getName(), false, -0.57f, -0.72f, 0.50f, 0.97f));

		// Random room 1 ( west, middle)
		street.addEntity(EntityFactory.genPortal(rand1.getName(), false, -0.54f, 1.09f, 0.50f, 0.97f));
		rand1.addEntity(EntityFactory.genPortal(street.getName(), false, 0.50f, 0.97f, -0.54f, 1.09f));

		// Random room 2 (west, bottom)
		street.addEntity(EntityFactory.genPortal(rand2.getName(), false, -0.54f, 2.88f, 0.50f, 0.97f));
		rand2.addEntity(EntityFactory.genPortal(street.getName(), false, 0.50f, 0.97f, -0.54f, 2.88f));

		// Random room 3 (east, top)
		street.addEntity(EntityFactory.genPortal(rand3.getName(), false, 1.54f, -0.72f, 0.50f, 0.97f));
		rand3.addEntity(EntityFactory.genPortal(street.getName(), false, 0.50f, 0.97f, 1.54f, -0.72f));

		// Random room 4 (east, middle)
		street.addEntity(EntityFactory.genPortal(rand4.getName(), false, 1.54f, 1.14f, 0.50f, 0.97f));
		rand4.addEntity(EntityFactory.genPortal(street.getName(), false, 0.50f, 0.97f, 1.54f, 1.14f));

		// Key room
		street.addEntity(EntityFactory.genPortal(keyRoom.getName(), false, 1.54f, 2.93f, 0.50f, 0.97f));
		keyRoom.addEntity(EntityFactory.genPortal(street.getName(), false, 0.50f, 0.97f, 1.54f, 2.93f));


		/*
		start.addEntity(EntityFactory.genZombieEntity(0.01f, 0.5f));
		start.addEntity(EntityFactory.genNPC(0.8f, 0.5f));

		rand1.addEntity(EntityFactory.genZombieEntity(0.4f, 0.4f));
		rand1.addEntity(EntityFactory.genZombieEntity(1f, 1f));
		rand1.addEntity(EntityFactory.genZombieEntity(0.1f, 0.1f));
*/
		world.add(street);
		world.add(rand1);
		world.add(rand2);
		world.add(rand3);
		world.add(rand4);
		world.add(keyRoom);

		return world;
	}

}
