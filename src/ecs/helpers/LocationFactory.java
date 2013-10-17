package ecs.helpers;

import java.util.Random;

import renderer.ModelType;
import ecs.components.Inventory;
import ecs.components.Position;
import ecs.components.Text;
import ecs.entity.Entity;
import ecs.world.Location;
import ecs.world.SpawnPoint;

/** Generates some predefined locations
 *
 * @author Lord Mumford
 *
 */
public class LocationFactory {

	private transient static Random random = new Random();

	/** Create a starting location where players spawn. Contains a container that may
	 * hold things a player may find useful, as well as a key that lets them get outside
	 *
	 * @return
	 */
	public static Location startingLocation(){
		final Location location = new Location("Starting location");
		// Walls
		location.addEntity(EntityFactory.genWallEntity(0.5f, -0.05f, 1f, 0.1f)); // Top
		location.addEntity(EntityFactory.genWallEntity(0.5f, 1.05f, 1f, 0.1f)); // Bottom
		location.addEntity(EntityFactory.genWallEntity(-0.05f, 0.5f, 0.1f, 1f));
		location.addEntity(EntityFactory.genWallEntity(1.05f, 0.5f, 0.1f, 1f));
		// Add a helpful container to the starting area
		final Entity container = EntityFactory.getContainer(0.1f, 0.1f);
		final Inventory contInv = container.getComponent(Inventory.class);
	//	Entity money = EntityFactory.genMoney(1, 1, (int)(Math.random()*50));
		final Entity money = EntityFactory.genMoney(1, 1, 150);
		money.removeComponent(Position.class);
		contInv.addEntity(money);
		contInv.addEntity(EntityFactory.genRandomWeapon());
		contInv.addEntity(EntityFactory.genKey("Streets", new Position(0.5, 0.5)));
		final Entity cheese = EntityFactory.genItemEntity("Cheese");
		cheese.addComponent(new Text("This is a tasty piece of cheddar"));
		contInv.addEntity(EntityFactory.genPotion());
		contInv.addEntity(cheese);

		location.addEntity(container);
		// Model to be used
		location.setModelType(ModelType.ROOM);
		// Add some spawn points
		location.addSpawn(new SpawnPoint(0.8f, 0.2f));
		location.addSpawn(new SpawnPoint(0.01f, 0.2f));
		return location;
	}

	/** Creates a zombie room that is filled with zombies!
	 *
	 * @return
	 */
	public static Location zombieRoom(){
		final Location location = new Location("Zombie Room");
		location.addEntity(EntityFactory.genZombieEntity(0, 0.1f));
		location.addEntity(EntityFactory.genZombieEntity(0, 0.2f));
		location.addEntity(EntityFactory.genZombieEntity(0, 0.3f));
		location.setModelType(ModelType.ROOM);

		// Boundary walls
		final Entity[] walls = getWalls(0,0,1f,0.5f);
		for(int i=0;i < walls.length; i++){

			location.addEntity(walls[i]);
		}
	//	location.addEntity(EntityFactory.genWallEntity(0, 0, 2f, 0.05f));
		return location;
	}

	/** Generates walls
	 *
	 * @param x X position
	 * @param y Y position
	 * @param width Width
	 * @param height Height
	 * @return
	 */
	private static Entity[] getWalls(final float x, final float y, final float width, final float height) {
		final Entity[] walls = new Entity[4];
		walls[0] = EntityFactory.genWallEntity(x, y, width, 0.05f);
		walls[1] = EntityFactory.genWallEntity(x, y, 0.05f, height);
		walls[2] = EntityFactory.genWallEntity(x, height, width, 0.05f);
		walls[3] = EntityFactory.genWallEntity(width, y, 0.05f, height);
		return walls;
	}

	/** Genetates a random location
	 *
	 * @return
	 */
	public static Location randomLocation(){
		final long ID = random.nextLong() % 10000;
		final Location location = new Location("Random Room" + ID);
		// Boundaries
		location.addEntity(EntityFactory.genWallEntity(0.5f, -0.05f, 1f, 0.1f)); // Top
		location.addEntity(EntityFactory.genWallEntity(0.5f, 1.05f, 1f, 0.1f)); // Bottom
		location.addEntity(EntityFactory.genWallEntity(-0.05f, 0.5f, 0.1f, 1f));
		location.addEntity(EntityFactory.genWallEntity(1.05f, 0.5f, 0.1f, 1f));
		location.setModelType(ModelType.ROOM);

		location.addSpawn(new SpawnPoint(0.33f, 0.71f));
		location.addSpawn(new SpawnPoint(0.26f, 0.43f));
		location.addSpawn(new SpawnPoint(0.47f, 0.22f));
		location.addSpawn(new SpawnPoint(0.75f, 0.36f));
		location.addSpawn(new SpawnPoint(0.62f, 0.73f));




		return location;
	}

	/** Generates a room with the boss key
	 * Also has an NPC that tells you what to do
	 *
	 * @return
	 */
	public static Location keyRoom() {
		final Location location = new Location("Key Room");
		location.addEntity(EntityFactory.genWallEntity(0.5f, -0.05f, 1f, 0.1f)); // Top
		location.addEntity(EntityFactory.genWallEntity(0.5f, 1.05f, 1f, 0.1f)); // Bottom
		location.addEntity(EntityFactory.genWallEntity(-0.05f, 0.5f, 0.1f, 1f));
		location.addEntity(EntityFactory.genWallEntity(1.05f, 0.5f, 0.1f, 1f));
		location.addEntity(EntityFactory.genModel(ModelType.ROOM));
		location.addEntity(EntityFactory.genNPC("Marco", "Congratulations! You found me!", 0.53f, 0.41f));

		location.setModelType(ModelType.ROOM);

		return location;
	}

	/** Generates the streets that leads to all the random rooms, key room and boss room
	 *
	 * @return
	 */
	public static Location getStreet() {
		final Location street = new Location("Streets");
		street.setModelType(ModelType.OUTSIDE);

		// Starter Room one
		street.addEntity(EntityFactory.genWallEntity((-52+25.4f) * 0.02f, (-92.294f+28.4f) * 0.02f, 52.8f * 0.02f, 52.8f * 0.02f));
		street.addEntity(EntityFactory.genPortal("Starting Location", false,
				(-52+25.4f) * 0.02f, (-92.294f+52.8f) * 0.02f, 0.5f, 1.05f));



		// Room two
		street.addEntity(EntityFactory.genWallEntity((-52+25.4f) * 0.02f, (-2.061f+28.4f) * 0.02f, 52.8f * 0.02f, 52.8f * 0.02f));

		// Room three
		street.addEntity(EntityFactory.genWallEntity((-52+25.4f) * 0.02f,  (88.172f+28.4f) * 0.02f, 52.8f * 0.02f, 52.8f * 0.02f));

		// Room four
		street.addEntity(EntityFactory.genWallEntity((51.997f+25.4f) * 0.02f, (-92.294f+28.4f) * 0.02f, 52.8f * 0.02f, 52.8f * 0.02f));

		// Room five
		street.addEntity(EntityFactory.genWallEntity((51.997f+25.4f) * 0.02f, (-2.061f+28.4f)  * 0.02f, 52.8f * 0.02f, 52.8f * 0.02f));

		// Room sixe
		street.addEntity(EntityFactory.genWallEntity((51.997f+25.4f) * 0.02f, (88.172f+28.4f) * 0.02f, 52.8f * 0.02f, 52.8f * 0.02f));

		street.addEntity(EntityFactory.genWallEntity((51.997f+25.4f) * 0.02f, (88.172f+28.4f) * 0.02f, 52.8f * 0.02f, 52.8f * 0.02f));



		return street;
	}

}
