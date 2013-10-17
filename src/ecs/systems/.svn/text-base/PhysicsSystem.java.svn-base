package ecs.systems;

import java.util.*;

import ecs.entity.*;
import ecs.components.*;
import ecs.components.Volume.VolumeType;
import ecs.world.Location;
import ecs.components.Type.*;

import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.callbacks.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.contacts.Contact;

import log.Log;

/**
 * This is a  physics system, which handles
 * the  movement  of  entities  within  the
 * world and  handles any  collisions. This
 * system  is adaptive  in  the sense  that
 * while  it  maintains  state, it  can  be
 * destroyed  and  reconstructed as  needed
 * without needing to store anything beyond
 * the   position  and   velocity  of   all
 * involved  entities  (which implies  that
 * this system need  not be serialized upon
 * saving the world).
 * <p>
 * It  will correctly  respond to  entities
 * being  added to  the world,  or entities
 * which lost  their   Position  or  Volume
 * components,  however it  will <b>not</b>
 * respond   to   entities   losing   their
 * Velocity component  (so dynamic entities
 * cannot become static and vice versa).
 *
 * @author Thomas Beneteau (300250968)
 */
public class PhysicsSystem implements EntitySystem
{
	private final String COMPONENT = "Physics System";

	private class PhysicsWorld implements ContactListener
	{
		public final Map<Long, Body> bodies = new HashMap<Long, Body>();
		public final World world = new World(new Vec2(0.0f, 0.0f));
		private final Location location;

		public PhysicsWorld(Location location)
		{
			world.setContactListener(this);
			world.setSleepingAllowed(true);
			this.location = location;
		}

		/**
		 * Called when two physics bodies make contact.
		 */
		@Override
		public void beginContact(Contact contact)
		{
			Entity e1 = location.getEntity((Long)contact.getFixtureA().getBody().getUserData());
			Entity e2 = location.getEntity((Long)contact.getFixtureB().getBody().getUserData());

			if (isColliding(e1, e2, EntityType.BULLET))
			{
				bulletCollision(reorderL(e1, e2, EntityType.BULLET),
								reorderR(e1, e2, EntityType.BULLET));
			}
			else if (isColliding(e1, e2, EntityType.ZOMBIE))
			{
			//	zombieAttack(reorderL(e1, e2, EntityType.ZOMBIE), reorderR(e1, e2, EntityType.ZOMBIE));
			}
			// else if ...
		}

		/* Collision handlers, note first entity is guaranteed to be the "colliding" type,
		 * e.g. in the bulletCollision() handler, "bullet" is guaranteed to be a bullet. */

		private void bulletCollision(Entity bullet, Entity other)
		{
			int dmg = bullet.getComponent(DamageFactor.class).getDamageFactor();
			switch (other.getType())
			{
				case PLAYER:
					if(bullet.hasAll(Name.class)){
						Name name = bullet.getComponent(Name.class);
						if(name.hasSuffix() && name.getSuffix().equalsIgnoreCase("Soothing")){
							other.getComponent(Health.class).increase(2);
							break;
						}
					}
					other.getComponent(Health.class).decrease(dmg);
					break;

				case ZOMBIE:
					other.getComponent(Health.class).decrease(dmg);
					break;

				default:
					/* Do nothing. */
			}

			bullet.setRemoved();
		}

		/* Contact utility methods. */

		/**
		 * Returns whether a  collision between two
		 * entities  involves   a  specific  entity
		 * type.
		 */
		private boolean isColliding(Entity e1, Entity e2, EntityType type)
		{
			if(e1==null || e2==null){ return false; }
			return ((e1.getType() == type) || (e2.getType() == type));
		}

		/**
		 * Chooses which  entity between e1  and e2
		 * has the  specified entity type.  This is
		 * to reorder  contact entities  as contact
		 * order is unspecified.
		 */
		private Entity reorderL(Entity e1, Entity e2, EntityType type)
		{
			return (e1.getType() == type ? e1 : e2);
		}

		/**
		 * Chooses the entity  which was not chosen
		 * by {@code reorderL}.
		 */
		private Entity reorderR(Entity e1, Entity e2, EntityType type)
		{
			return (e1.getType() == type ? e2 : e1);
		}

		/* Unused collision listeners. */

		@Override
		public void endContact(Contact contact)
		{
			return;
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse)
		{
			return;
		}

		@Override
		public void preSolve(Contact contact, Manifold manifold)
		{
			return;
		}
	}

	private final Map<Location, PhysicsWorld> worlds = new HashMap<Location, PhysicsWorld>();

	private final int VELOCITY_ITERATIONS = 6;
	private final int POSITION_ITERATIONS = 3;

	@Override
	public void process(ecs.world.World dummy, Location location, double delta)
	{
		/* Synchronization phase: add new physics entities, remove those that are gone. */

		if (!worlds.containsKey(location))
			worlds.put(location, new PhysicsWorld(location));
		PhysicsWorld world = worlds.get(location);

		Iterator<Map.Entry<Long, Body>> iter = world.bodies.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry<Long, Body> entry = iter.next();

			Entity entity = location.getEntity(entry.getKey());

			if ((entity == null) || (!isActive(entity)))
			{
				world.world.destroyBody(entry.getValue());
				iter.remove();
			}
		}

		for (Entity entity : location.getEntities())
		{
			if ((isActive(entity)) && (!world.bodies.containsKey(entity.getID())))
				world.bodies.put(entity.getID(), physicsFactory(world.world, entity));
		}

		/* Simulation phase: step the physics simulation, and handle collisions. */

		world.world.step((float)delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

		/* Readback phase: get the bodies back from the simulation and update the entities. */

		for (Long ID : world.bodies.keySet())
		{
			Entity entity = location.getEntity(ID);
			Body body = world.bodies.get(ID);

			/* We never update static bodies, as they will not move. */
			if (body.isAwake() && (body.getType() == BodyType.DYNAMIC))
			{
				if (entity.hasAll(Position.class))
				{
					entity.getComponent(Position.class).setX(body.getPosition().x);
					entity.getComponent(Position.class).setY(body.getPosition().y);
				}

				if (entity.hasAll(Velocity.class))
				{
					entity.getComponent(Velocity.class).setX(body.getLinearVelocity().x);
					entity.getComponent(Velocity.class).setY(body.getLinearVelocity().y);
				}
			}
		}
	}

	/**
	 * Acquires the physical body of an entity,
	 * or  throws an  exception if  this entity
	 * has no associated physical body.
	 */
	public Body getBody(Location location, Entity entity)
	{
		if (!hasBody(location, entity))
			throw new IllegalArgumentException("This entity is not simulated by the physics system. : " + entity.getType());

		return worlds.get(location).bodies.get(entity.getID());
	}

	/**
	 * Returns  whether   a  given   entity  is
	 * currently being simulated by the physics
	 * system.
	 */
	public boolean hasBody(Location location, Entity entity)
	{
		PhysicsWorld world = worlds.get(location);
		if (world == null) return false;

		Body body = world.bodies.get(entity.getID());
		if (body == null) return false;

		return true;
	}

	/**
	 * Applies an  impulse to an  entity, which
	 * will accelerate it in a given direction.
	 */
	public void displaceEntity(Location location, Entity entity, Vec2 impulse)
	{
		Body body = getBody(location, entity);
		body.applyLinearImpulse(impulse, body.getPosition());
	}

	/**
	 * Applies an  impulse to an  entity, which
	 * will accelerate it in a given direction.
	 */
	public void displaceEntity(Location location, Entity entity, float x, float y)
	{
		Body body = getBody(location, entity);
		body.applyLinearImpulse(new Vec2(x, y), body.getPosition());
	}

	/**
	 * Teleports a given entity to an arbitrary position,
	 * going against every law of physics known to mankind.
	 */
	public void teleportEntity(Location location, Entity entity, float x, float y)
	{
		entity.getComponent(Position.class).setX(x);
		entity.getComponent(Position.class).setY(y);
		PhysicsWorld world = worlds.get(location);
		world.world.destroyBody(world.bodies.remove(entity.getID()));
	}

	/**
	 * Whether an entity should be part of this
	 * physics simulation or not.
	 */
	private boolean isActive(Entity entity)
	{
		return (entity.hasAll(Volume.class, Position.class));
	}

	/**
	 * Takes  an entity  and returns  a physics
	 * body   with  the   appropriate  physical
	 * constants.
	 */
	private Body physicsFactory(World world, Entity entity)
	{
		float NA = Float.NaN; /* For static objects where damping is not applicable. */
		float INF = Float.POSITIVE_INFINITY; /* To use infinite damping (if needed). */

		EntityType type = entity.getType();
		Body body = physicsBody(world, entity);
		FixtureDef params = new FixtureDef();
		params.shape = getShape(entity);

		switch (type)
		{
			/* D   = density, the higher the heavier the object is and the less it is affected by forces.  */
			/* F   = friction, the higher the more the object is slowed down by sliding with another.	   */
			/* R   = restitution, between 0 and 1, amount of energy restored upon bouncing off something.  */
			/* LD  = linear damping, rate at which the object bleeds off velocity while moving.			   */
			/* AD  = angular damping, same as above but for angular velocity i.e. rotation.				   */
			/* CCD = continuous collision detection, expensive but 100% accurate, for bullets.			   */

			/*   ENTITY TYPE                                       D     F     R    LD    AD    CCD        */
			case BULLET:			 setAttributes(body, params, 0.1f, 0.2f, 0.8f, .05f,  INF,  true); break;
			case PLAYER: 			 setAttributes(body, params, 8.0f, 0.2f, 0.8f, 9.5f, 0.0f, false); break;
			case ZOMBIE: 			 setAttributes(body, params, 8.0f, 0.2f, 0.8f, 8.5f, 0.0f, false); break;
			case NPC: 			 	 setAttributes(body, params, 8.0f, 0.2f, 0.8f, 8.5f, 0.0f, false); break;
			case HEALTH_POTION: 	 setAttributes(body, params, 1.0f, 0.2f, 0.8f,   NA,   NA, false); break;
			case ITEM: 				 setAttributes(body, params, 1.0f, 0.2f, 0.8f,   NA,   NA, false); break;
			case KEY: 				 setAttributes(body, params, 1.0f, 0.2f, 0.8f,   NA,   NA, false); break;
			case MONEY: 			 setAttributes(body, params, 1.0f, 0.2f, 0.8f,   NA,   NA, false); break;
			case PORTAL: 			 setAttributes(body, params, 1.0f, 0.2f, 0.8f,   NA,   NA, false); break;
			case WALL: 				 setAttributes(body, params, 1.0f, 0.4f, 0.8f,   NA,   NA, false); break;
			case WEAPON: 			 setAttributes(body, params, 1.0f, 0.2f, 0.8f,   NA,   NA, false); break;
			case CONTAINER:			 setAttributes(body, params, 1.0f, 0.2f, 0.8f,   NA,   NA, false); break;
			default: Log.warning(COMPONENT, "No physics parameters defined for the entity type '%s'!", type);
		}

		body.createFixture(params);
		return body;
	}

	/**
	 * Sets  the   physical  attributes   of  a
	 * fixture (a combination of  a body with a
	 * shape).
	 */
	private void setAttributes(Body body, FixtureDef params, float density, float friction, float restitution,
							   float linearDamping, float angularDamping, boolean CCD)
	{
		body.setAngularDamping(angularDamping);
		body.setLinearDamping(linearDamping);
		params.restitution = restitution;
		params.friction = friction;
		params.density = density;
		body.setBullet(CCD);
	}

	/**
	 * Returns   the   appropriate  body   type
	 * (static or dynamic) for a given entity.
	 */
	private BodyType getBodyType(Entity entity)
	{
		return (entity.hasAll(Velocity.class) ? BodyType.DYNAMIC : BodyType.STATIC);
	}

	/**
	 * Returns  a  physics  body  for  a  given
	 * entity  (and  adds  it  to  the  physics
	 * world).  Automatically  sets the  body's
	 * position and velocity (if applicable).
	 */
	private Body physicsBody(World world, Entity entity)
	{
		BodyDef definition = new BodyDef();
		definition.type = getBodyType(entity);

		Position pos = entity.getComponent(Position.class);
		definition.position = new Vec2((float)pos.getX(), (float)pos.getY());

		if (definition.type == BodyType.DYNAMIC)
		{
			Velocity vel = entity.getComponent(Velocity.class);
			definition.linearVelocity = new Vec2((float)vel.getX(), (float)vel.getY());
		}

		Body body = world.createBody(definition);
		body.setUserData(entity.getID());
		return body;
	}

	/**
	 * Returns the physics  shape of an entity,
	 * based on its BoundShape component.
	 */
	private Shape getShape(Entity entity)
	{
		Volume volume = entity.getComponent(Volume.class);

		if (volume.getType() == VolumeType.BOX)
		{
			PolygonShape box = new PolygonShape();
			box.setAsBox((float)volume.getWidth() / 2, (float)volume.getHeight() / 2);
			return box;
		}
		else
		{
			CircleShape circle = new CircleShape();
			circle.setRadius(volume.getWidth() / 2);
			return circle;
		}
	}
}
