package network.server.back_end.logic_tests;

import static org.junit.Assert.*;
import network.NetClient;
import network.packet.ActionPacket;
import network.packet.action.InteractAction;
import network.server.back_end.logic_handlers.InteractionHandler;

import org.junit.Test;

import ecs.components.*;
import ecs.entity.Entity;
import ecs.helpers.EntityFactory;
import ecs.world.Location;
import ecs.world.World;

public class UpgradeHandlerTests {



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
