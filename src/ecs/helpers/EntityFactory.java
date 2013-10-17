package ecs.helpers;


import java.util.HashSet;
import java.util.Set;

import renderer.ModelType;
import ecs.components.DamageFactor;
import ecs.components.Equipped;
import ecs.components.EventTrigger;
import ecs.components.Exit;
import ecs.components.Health;
import ecs.components.Inventory;
import ecs.components.ModelData;
import ecs.components.Multiplier;
import ecs.components.Name;
import ecs.components.Position;
import ecs.components.Size;
import ecs.components.Target;
import ecs.components.TargetRadius;
import ecs.components.Text;
import ecs.components.Type;
import ecs.components.Type.EntityType;
import ecs.components.Upgrades;
import ecs.components.Velocity;
import ecs.components.Volume;
import ecs.components.Worth;
import ecs.entity.Entity;

/** Creates predefined entities
 *
 * @author Lord Mumford
 *
 */
public class EntityFactory {

	/** Create a player entity
	 *
	 */
	public static Entity genPlayerEntity(final String name){
		final Entity ent = new Entity();
		ent.addComponent(new Type(EntityType.PLAYER));
		ent.addComponent(new Position(0.3,0.7));
		ent.addComponent(new Velocity(0,0));
		ent.addComponent(new TargetRadius(0.2));
		ent.addComponent(Volume.makeCircle(0.02f));
		ent.addComponent(new Health(100));
		ent.addComponent(new Name(name));

		final Set<EntityType> targets = new HashSet<EntityType>();
		targets.add(EntityType.PLAYER);
		targets.add(EntityType.MONEY);
		targets.add(EntityType.HEALTH_POTION);
		targets.add(EntityType.ITEM);
		targets.add(EntityType.PORTAL);
		targets.add(EntityType.WEAPON);
		targets.add(EntityType.CONTAINER);
		targets.add(EntityType.NPC);
		ent.addComponent(new ModelData(ModelType.PLAYER));
		ent.addComponent(new Target(targets));
		ent.addComponent(new Multiplier(1));
		ent.addComponent(new Equipped());
		ent.addComponent(new DamageFactor(10));
		ent.addComponent(new EventTrigger(500));
		final Inventory inv = new Inventory(100);
		ent.addComponent(inv); // Inventory with max size 100 (total sizes must be less than 100)
		ent.addComponent(new Upgrades());
		return ent;
	}

	/** Creates a zombie entity
	 *
	 * @param location
	 * @return
	 */
	public static Entity genZombieEntity(final float posX, final float posY){
		final Entity ent = new Entity();
		ent.addComponent(new Type(EntityType.ZOMBIE));
		ent.addComponent(new Position(posX,posY));
		ent.addComponent(new Health(100));
		ent.addComponent(new Velocity(0,0));
		ent.addComponent(Volume.makeCircle(0.01f));
		ent.addComponent(new EventTrigger(1000));
		ent.addComponent(new DamageFactor(10));
		ent.addComponent(new ModelData(ModelType.ZOMBIE));
		final Set<EntityType> targets = new HashSet<EntityType>();
		targets.add(EntityType.PLAYER);
		targets.add(EntityType.NPC);
		ent.addComponent(new Target(targets));
		ent.addComponent(new TargetRadius(0.3));
		return ent;
	}

	/** Creates an item entity
	 *
	 * @param location
	 * @param name
	 * @return
	 */
	public static Entity genItemEntity(final String name){
		final Entity ent = new Entity();

		ent.addComponent(new Position(3,3));
		ent.addComponent(Volume.makeCircle(0.01f));
		ent.addComponent(new Type(EntityType.ITEM));
		ent.addComponent(new Size(10));
		ent.addComponent(new Name(name));
		ent.addComponent(new ModelData(ModelType.BOX));
		return ent;
	}

	/** Creates a default weapon entity with no suffix
	 *
	 * @param name Name of weapon
	 * @return
	 */
	public static Entity genDefaultWeapon(final String name){
		final Entity ent = new Entity();
		ent.addComponent(new Type(EntityType.WEAPON));
		ent.addComponent(new DamageFactor(10));
		ent.addComponent(new EventTrigger(100));
		ent.addComponent(new Name(name));
		return ent;
	}

	/** Creates a randomized weapon
	 *
	 * @return
	 */
	public static Entity genRandomWeapon() {
		final Entity ent = new Entity();
		ent.addComponent(new Type(EntityType.WEAPON));
		ent.addComponent(new Size(60));
		ent.addComponent(new DamageFactor(10));
		ent.addComponent(new EventTrigger(100 + (long)(Math.random()*200)));
		final Name name = new Name(Name.genWeaponName());
		name.setSuffix(Name.genWeaponSuffix());
		ent.addComponent(name);
		return ent;
	}

	/** Creates bullet entity
	 * Specify a radius (size of bullet), as well as direction (x, y), and position (x, y)
	 *
	 */
	public static Entity genBulletEntity(final DamageFactor damage, final float radius, final Position pos, final Velocity vel){
		final Entity ent = new Entity();
		ent.addComponent(new Type(EntityType.BULLET));
		ent.addComponent(new Position(pos.getX(), pos.getY()));
		ent.addComponent(Volume.makeCircle(radius));
		ent.addComponent(damage);
		ent.addComponent(vel);
		ent.addComponent(new Health(10));
		ent.addComponent(new ModelData(ModelType.BULLET));
		return ent;
	}

	/** Creates a single wall which is pretty much just a bounding box. It does not have
	 * an associated graphic, so it can be used for any sort of obstruction
	 *
	 * @param x X position
	 * @param y Y position
	 * @param width Width
	 * @param height Height
	 *
	 * @return An obstruction box
	 */
	public static Entity genWallEntity(final float x, final float y, final float width,
			final float height) {
		final Entity ent = new Entity();
		ent.addComponent(new Type(EntityType.WALL));
		ent.addComponent(new Position(x, y));
		//ent.addComponent(new Volume(width, height));
		ent.addComponent(Volume.makeBox(width, height));
		return ent;
	}

	/** Generates some money
	 *
	 * @param x X position
	 * @param y Y position
	 * @param amount Amount of this money
	 * @return Money entity
	 */
	public static Entity genMoney(final float x, final float y, final int amount){
		final Entity ent = new Entity();
		ent.addComponent(new Type(EntityType.MONEY));
		ent.addComponent(new Position(x, y));
		ent.addComponent(new Worth(amount));
		ent.addComponent(Volume.makeCircle(0.002f));
		ent.addComponent(new Text("This is money"));
		ent.addComponent(new Size(0));
		return ent;
	}

	/** Can be a door or a portal. Anything that that leads to another location
	 *
	 * @param string Location it leads to
	 * @param x X position
	 * @param y Y position
	 * @return A portal or door
	 */
	public static Entity genPortal(final String string, boolean locked, final float x, final float y, final float x2, final float y2){
		final Entity ent = new Entity();
		ent.addComponent(new Position(x, y));
		//ent.addComponent(new Volume(0.005,0.005));
		ent.addComponent(Volume.makeBox(0.005f, 0.005f));
		ent.addComponent(new Type(EntityType.PORTAL));
		ent.addComponent(new Exit(string, locked, new Position(x2, y2)));
		return ent;
	}

	/** Generates a key entity
	 *
	 * @param access The location name that it accesses
	 * @return A key
	 */
	public static Entity genKey(final String access, Position exitPos){
		final Entity key = new Entity();
		key.addComponent(new Exit(access, false, exitPos));
		key.addComponent(new Type(EntityType.KEY));
		key.addComponent(new Name("Key"));
		key.addComponent(new Text("A key that leads to " + access));
		key.addComponent(new Size(1));
		return key;
	}

	/** Returns a container of a set size
	 * Containers can hold other entities
	 *
	 * @param x X position
	 * @param y Y position
	 * @return A container
	 */
	public static Entity getContainer(final double x, final double y) {
		final Entity container = new Entity();
		container.addComponent(Volume.makeBox(0.05f, 0.08f));
		container.addComponent(new Inventory(200));
		container.addComponent(new Type(EntityType.CONTAINER));
		container.addComponent(new Position(x,y));
		container.addComponent(new ModelData(ModelType.BOX));
		return container;
	}

	/** Generates an NPC based on name, dialog and position
	 * An NPC will be able to target Zombies
	 *
	 * @param name Name of NPC
	 * @param dialog What NPC says
	 * @param posX Position x
	 * @param posY Position y
	 * @return
	 */
	public static Entity genNPC(final String name, final String dialog, final float posX, final float posY){
		final Entity nPC = new Entity();
		nPC.addComponent(new Type(EntityType.NPC));
		nPC.addComponent(new Position(posX,posY));
		nPC.addComponent(new Health(100));
		nPC.addComponent(new Velocity(0,0));
		nPC.addComponent(Volume.makeCircle(0.01f));
		nPC.addComponent(new EventTrigger(2000));
		nPC.addComponent(new ModelData(ModelType.NPC));

		final Set<EntityType> targets = new HashSet<EntityType>();
		targets.add(EntityType.ZOMBIE);
		nPC.addComponent(new Target(targets));
		nPC.addComponent(new TargetRadius(0.3));

		nPC.addComponent(new Name(name)); // NPC name
		nPC.addComponent(new Text(dialog)); // NPC dialog

		return nPC;
	}

	/** Creates an NPC with a random name and dialog
	 *
	 * @param x X position
	 * @param y Y position
	 * @return
	 */
	public static Entity genNPC(final float x, final float y) {
		// Creates a random NPC
		return genNPC(Name.genNPCName(), Text.genDialog(), x, y);
	}

	/** Generate a health poition the restores 10 health
	 *
	 * @return A health potion
	 */
	public static Entity genPotion() {
		final Entity potion = new Entity();
		potion.addComponent(new Name("Potion"));
		potion.addComponent(new Text("Replenishes 10 health"));
		potion.addComponent(new Type(EntityType.HEALTH_POTION));
		potion.addComponent(Volume.makeCircle(0.005f));
		potion.addComponent(new Size(2));

		return potion;
	}

	/**
	 * Generates an entity with a ModelType.
	 * Used by the renderer to reference a model to use when drawing
	 * the scene.
	 *
	 *  @param ModelType to be used.
	 *  @author Marc Sykes
	 */
	public static Entity genModel(final ModelType type){
		final Entity model = new Entity();
		model.addComponent(new Type(EntityType.MODEL));
		model.addComponent(new ModelData(type));
		return model;
	}

}

