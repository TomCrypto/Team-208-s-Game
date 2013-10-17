package network.server.back_end.logic_tests;

import static org.junit.Assert.*;
import network.NetClient;
import network.packet.ActionPacket;
import network.packet.action.InteractAction;
import network.server.back_end.logic_handlers.InteractionHandler;

import org.junit.Test;

import ecs.components.Equipped;
import ecs.components.Inventory;
import ecs.components.Target;
import ecs.entity.Entity;
import ecs.helpers.EntityFactory;
import ecs.world.Location;
import ecs.world.World;

public class InteractionHandlerTests {

	@Test
	public void testEmptyInventory(){
		NetClient client = getClient(); // Set up
		ActionPacket packet = new InteractAction();
		World world = getWorld();
		Target target = client.getPlayer().getComponent(Target.class);
		Inventory inv = client.getPlayer().getComponent(Inventory.class);
		assertEquals("Inventory should be empty", 0, inv.getCurrent());
		assertEquals("Inventory should have no items",0,  inv.getInventory().size());
	}

	@Test
	public void takeHealthTest(){
		NetClient client = getClient(); // Set up
		ActionPacket packet = new InteractAction();
		World world = getWorld();
		Target target = client.getPlayer().getComponent(Target.class);
		Inventory inv = client.getPlayer().getComponent(Inventory.class);
		Entity potion = EntityFactory.genPotion();
		target.setTarget(potion); // Set target to potion
		// Handle it
		InteractionHandler.handleEvent(client, packet, world);
		// Now check the player inventory to see if it's there, the size, etc
		assertEquals("Size should be 2",2, inv.getCurrent());
		assertEquals("Should have 1 item", 1, inv.getInventory().size());
		assertNotNull("Should have retrieved an item", inv.getItem(potion.getID()));
	}

	@Test
	public void takeItemTest(){
		NetClient client = getClient(); // Set up
		ActionPacket packet = new InteractAction();
		World world = getWorld();
		Target target = client.getPlayer().getComponent(Target.class);
		Inventory inv = client.getPlayer().getComponent(Inventory.class);
		Entity cheese = EntityFactory.genItemEntity("Cheese");
		target.setTarget(cheese); // Set target to potion
		// Handle it
		InteractionHandler.handleEvent(client, packet, world);
		// Now check the player inventory to see if it's there, the size, etc
		assertEquals("Size should be 10", 10, inv.getCurrent());
		assertEquals("Should have 1 item", 1, inv.getInventory().size());
		assertNotNull("Should have retrieved an item", inv.getItem(cheese.getID()));
	}

	@Test
	public void takeWeaponTest(){ // Player starts with no weapon
		NetClient client = getClient(); // Set up
		ActionPacket packet = new InteractAction();
		World world = getWorld();
		Target target = client.getPlayer().getComponent(Target.class);
		Inventory inv = client.getPlayer().getComponent(Inventory.class);
		Entity weapon = EntityFactory.genRandomWeapon();
		target.setTarget(weapon); // Set target to potion
		// Handle it
		InteractionHandler.handleEvent(client, packet, world);
		// Since this is the first weapon, there should be nothing added to inventory
		assertEquals("Inventory should be empty", 0, inv.getCurrent());
		assertEquals("Inventory should have no items", 0, inv.getInventory().size());
		assertNull("Should not retrieve the weapon", inv.getItem(weapon.getID()));
	}

	@Test
	public void takeWeaponTest2(){ // Player starts with no weapon
		NetClient client = getClient(); // Set up
		ActionPacket packet = new InteractAction();
		World world = getWorld();
		client.getPlayer().getComponent(Equipped.class).equip(EntityFactory.genRandomWeapon());
	//	System.out.println(client.getPlayer());
		Target target = client.getPlayer().getComponent(Target.class);
		Inventory inv = client.getPlayer().getComponent(Inventory.class);
		Entity weapon = EntityFactory.genRandomWeapon();
		target.setTarget(weapon); // Set target to potion
		// Handle it
		InteractionHandler.handleEvent(client, packet, world);
		//System.out.println(client.getPlayer());
		// Since this is the first weapon, there should be nothing added to inventory
		assertEquals("Inventory should be at 60 (size of weapon)", 60, inv.getCurrent());
		assertEquals("Inventory should have 1 item", 1, inv.getInventory().size());
		assertNotNull("Should be able to retrieve weapon", inv.getItem(weapon.getID()));
	}

	///////////////////////
	// HELPERS  ///////////
	///////////////////////

	public NetClient getClient(){
		Location location = new Location();
		NetClient client = new DummyClient("Ace", location);
		client.setPlayerEntity(getPlayer());
		return client;
	}

	public Entity getPlayer(){
		Entity player = EntityFactory.genPlayerEntity("Ace");
		player.getComponent(Equipped.class).equip(EntityFactory.genDefaultWeapon("Shotgun"));
		return player;
	}

	public World getWorld(){
		return new World();
	}

}
