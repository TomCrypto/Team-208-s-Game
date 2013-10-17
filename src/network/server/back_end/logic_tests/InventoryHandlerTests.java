package network.server.back_end.logic_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import network.NetClient;
import network.packet.ActionPacket;
import network.packet.action.DropAction;
import network.packet.action.InteractAction;
import network.server.back_end.logic_handlers.InventoryHandler;
import ecs.components.Equipped;
import ecs.components.Inventory;
import ecs.entity.Entity;
import ecs.helpers.EntityFactory;
import ecs.world.Location;
import ecs.world.World;

public class InventoryHandlerTests {

	@Test
	public void dropTest(){
		NetClient client = getClient(); // Set up
		Entity potion = EntityFactory.genPotion();
		Inventory inv = client.getPlayer().getComponent(Inventory.class); // Add potion to drop
		inv.addEntity(potion);
		// Just check the potion is there to drop
		assertEquals("Should have 1 item in inventory", 1, inv.getInventory().size());
		assertEquals("Should have 2 weight in the inventory", 2, inv.getCurrent());
		ActionPacket packet = new DropAction(potion.getID());
		World world = getWorld();
		// The handler method
		InventoryHandler.handleEvent(client, packet, world);
		// Player should no longer have the potion
		assertEquals("Should have nothing in the inventory", 0, inv.getInventory().size());
		assertEquals("Should have 0 weight in the inventory", 0, inv.getCurrent());
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
