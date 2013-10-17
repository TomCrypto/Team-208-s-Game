package dataStorage;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.Test;

import ecs.entity.Entity;
import ecs.helpers.EntityFactory;
import ecs.helpers.LocationFactory;
import ecs.world.*;
import ecs.components.*;
import ecs.components.Type.EntityType;

/**
 * JUnit testing for the saving/loading
 * @author Nainesh Patel (300232221)
 *
 */
public class DataStorageTest {

	public Location getLocation(World world, String name){
		Location location = null;
		for(Location loc: world.getLocations()){
			if(loc.getName().equals(name)){
				location = loc;
			}
		}
		return location;
	}

	public Entity getEntity(Location loc, long id){
		Entity entity = null;
		for(Entity ent: loc.getEntities()){
			if(ent.getID() == id){
				entity = ent;
			}
		}
		return entity;
	}

	public Entity save(String type){
		World world = new World();
		Location loc = LocationFactory.randomLocation();
		loc.setName("testRoom");
		world.add(loc);
		Entity entity = null;
		if(type.equals("player")){
			entity = EntityFactory.genPlayerEntity("testPlayer");
			entity.getComponent(Inventory.class).addEntity(EntityFactory.genItemEntity("cheese"));
		}
		else if(type.equals("portal")){
			entity = EntityFactory.genPortal(loc.getName(), false, 100, 100, 100, 100);
		}
		else if(type.equals("money")){
			entity = EntityFactory.genMoney(10, 10, 100);
		}
		world.addToLoc("testRoom", entity);
		DataStorage.saveFile(new File("testSave.xml"), world);
		return entity;
	}

	public Entity load(long testID) throws IOException{
		File saveFile = new File("testSave.xml");
		World loadedWorld = DataStorage.loadFile(saveFile);
		Location loadedLoc = getLocation(loadedWorld, "testRoom");
		Entity loadedEntity = getEntity(loadedLoc, testID);
		saveFile.delete();
		return loadedEntity;
	}

	@Test
	public void testHealth() throws IOException{
		Entity testPlayer = save("player");
		Health health = testPlayer.getComponent(Health.class);

		Entity loadedEntity = load(testPlayer.getID());
		Health loadedHealth = loadedEntity.getComponent(Health.class);

		assertEquals(health.getCurrent(), loadedHealth.getCurrent());
		assertEquals(health.getMax(), loadedHealth.getMax());
	}

	@Test
	public void testName() throws IOException{
		Entity testPlayer = save("player");
		Name name = testPlayer.getComponent(Name.class);

		Entity loadedEntity = load(testPlayer.getID());
		Name loadedName = loadedEntity.getComponent(Name.class);

		assertEquals(name.getName(), loadedName.getName());
	}

	@Test
	public void testDamageFactor() throws IOException{
		Entity testPlayer = save("player");
		DamageFactor damageFactor = testPlayer.getComponent(DamageFactor.class);

		Entity loadedEntity = load(testPlayer.getID());
		DamageFactor loadedFactor = loadedEntity.getComponent(DamageFactor.class);

		assertEquals(damageFactor.getDamageFactor(), loadedFactor.getDamageFactor());
	}

	@Test
	public void testEquipped() throws IOException{
		Entity testPlayer = save("player");
		Equipped equipped = testPlayer.getComponent(Equipped.class);

		Entity loadedEntity = load(testPlayer.getID());
		Equipped loadedEquipped = loadedEntity.getComponent(Equipped.class);
		if(equipped.hasEquipped()){
			EntityType equippedType = equipped.getEquipped().getComponent(Type.class).getType();
			EntityType loadedEquippedType = loadedEquipped.getEquipped().getComponent(Type.class).getType();
			assertEquals(equippedType, loadedEquippedType);
		}
		else{
			assertEquals(equipped.hasEquipped(), loadedEquipped.hasEquipped());
		}
	}


	@Test
	public void testExit() throws IOException{
		Entity testPortal = save("portal");
		Exit exit = testPortal.getComponent(Exit.class);

		Entity loadedPortal = load(testPortal.getID());
		Exit loadedExit = loadedPortal.getComponent(Exit.class);

		assertEquals(exit.getExit(), loadedExit.getExit());
	}

	@Test
	public void testInventory() throws IOException{
		Entity testPlayer = save("player");
		Inventory inv = testPlayer.getComponent(Inventory.class);
		List<Entity> invList = inv.getInventory();

		Entity loadedPlayer = load(testPlayer.getID());
		Inventory loadedInv = loadedPlayer.getComponent(Inventory.class);
		List<Entity> loadedInvList = loadedInv.getInventory();
		for(int i = 0; i < invList.size(); i++){
			assertEquals(invList.get(i).getID(), loadedInvList.get(i).getID());
		}

	}

	@Test
	public void testID() throws IOException{
		Entity testPlayer = save("player");

		Entity loadedPlayer = load(testPlayer.getID());

		assertEquals(testPlayer.getID(), loadedPlayer.getID());
	}

	/*
	@Test
	public void testModelData() throws IOException{
		Entity testPlayer = save("player");
		ModelData modelData = testPlayer.getComponent(ModelData.class);

		Entity loadedPlayer = load(testPlayer.getID());
		ModelData loadedModelData = loadedPlayer.getComponent(ModelData.class);

		assertEquals(modelData.getName(), loadedModelData.getName());
	}*/

	@Test
	public void testPosition() throws IOException{
		Entity testPlayer = save("player");
		Position position = testPlayer.getComponent(Position.class);

		Entity loadedPlayer = load(testPlayer.getID());
		Position loadedPosition = loadedPlayer.getComponent(Position.class);

		assertEquals(position.getX(), loadedPosition.getX(), 0.1);
		assertEquals(position.getY(), loadedPosition.getY(), 0.1);

	}

	@Test
	public void testTarget() throws IOException{
		Entity testPlayer = save("player");
		Target target = testPlayer.getComponent(Target.class);
		//Set<EntityType> targettable = target.getTargettable();

		Entity loadedPlayer = load(testPlayer.getID());
		Target loadedTarget = loadedPlayer.getComponent(Target.class);
		//Set<EntityType> loadedTargettable = loadedTarget.getTargettable();

		assertEquals(target.getTarget(), loadedTarget.getTarget());
	}

	@Test
	public void testTargetRadius() throws IOException{
		Entity testPlayer = save("player");
		TargetRadius targetRadius = testPlayer.getComponent(TargetRadius.class);

		Entity loadedPlayer = load(testPlayer.getID());
		TargetRadius loadedTargetRadius = loadedPlayer.getComponent(TargetRadius.class);

		assertEquals(targetRadius.getRadius(), loadedTargetRadius.getRadius(), 0.1);
	}

	@Test
	public void testText() throws IOException{
		Entity testMoney = save("money");
		Text text = testMoney.getComponent(Text.class);

		Entity loadedMoney = load(testMoney.getID());
		Text loadedText = loadedMoney.getComponent(Text.class);

		assertEquals(text.getText(), loadedText.getText());
	}

	@Test
	public void testType() throws IOException{
		Entity testPortal = save("portal");
		Type type = testPortal.getComponent(Type.class);

		Entity loadedPortal = load(testPortal.getID());
		Type loadedType = loadedPortal.getComponent(Type.class);

		assertEquals(type.getType(), loadedType.getType());
	}

	@Test
	public void testVelocity() throws IOException{
		Entity testPlayer = save("player");
		Velocity velocity = testPlayer.getComponent(Velocity.class);

		Entity loadedPlayer = load(testPlayer.getID());
		Velocity loadedVelocity = loadedPlayer.getComponent(Velocity.class);

		assertEquals(velocity.getX(), loadedVelocity.getX(), 0.1);
		assertEquals(velocity.getY(), loadedVelocity.getY(), 0.1);
	}

	@Test
	public void testVolume() throws IOException{
		Entity testPlayer = save("player");
		Volume volume = testPlayer.getComponent(Volume.class);

		Entity loadedPlayer = load(testPlayer.getID());
		Volume loadedVolume = loadedPlayer.getComponent(Volume.class);

		assertEquals(volume.getType(), loadedVolume.getType());
		assertEquals(volume.getHeight(), loadedVolume.getHeight(), 0.1);
		assertEquals(volume.getWidth(), loadedVolume.getWidth(), 0.1);
	}

	@Test
	public void testWorth() throws IOException{
		Entity testMoney = save("money");
		Worth worth = testMoney.getComponent(Worth.class);

		Entity loadedMoney = load(testMoney.getID());
		Worth loadedWorth = loadedMoney.getComponent(Worth.class);

		assertEquals(worth.getWorth(), loadedWorth.getWorth());
	}

}
