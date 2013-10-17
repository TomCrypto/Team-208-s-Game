package dataStorage;

import java.io.*;

import ecs.world.*;
import ecs.entity.*;
import ecs.components.*;


import javax.xml.bind.*;

import userinterface.states.UpgradesState;

/**
 * Handles the saving and loading of game states
 * Uses the JAXB package to perform the saving and loading functions
 *
 * @author Nainesh Patel (300232221)
 */

public class DataStorage{
	/**
	 * Used to save the world and all its entities/location.
	 * Saves to an xml file
	 *
	 * @param file - the file to save to
	 * @param world - the world which the player was using
	 */
	public static void saveFile(File file, World world){
		try{
			JAXBContext context = JAXBContext.newInstance(World.class, Component.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, ""); // makes the XML infinitely more readable
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(world, file);
		}catch(JAXBException e){
			e.printStackTrace();
		}
	}

	/**
	 * Handles the loading of an xml file into a World
	 *
	 * @param file - the file to load from
	 * @return World
	 * @throws IOException
	 */
	public static World loadFile(File file) throws IOException{
		try {
			if(!file.exists()) throw new IOException();
			JAXBContext context = JAXBContext.newInstance(World.class, Component.class);
			Unmarshaller unmarshal = context.createUnmarshaller();
			World world = (World) unmarshal.unmarshal(file);
			
			return world;

		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Used for testing.
	 * Prints the world including all its locations and entities to the system
	 * @param world
	 */
	public static void worldToString(World world){
		for(Location loc: world.getLocations()){
			System.out.println("Location: " + loc.getName());
			for(Entity ent: loc.getEntities()){
				System.out.println("Components:");
				for(Component comp: ent.getComponents()){
					System.out.println(comp.toString());
				}
				System.out.println("endOfComponent");
				System.out.println();
			}
		}
	}

	/**
	 * Used for checking if a component exists within the entity
	 * @param Entity
	 * @param Class
	 * @return boolean
	 */
	public boolean checkComponent(Entity e, Class<? extends Component> c){
		if(e.getComponent(c) != null){
			return true;
		}
		return false;
	}

	/*
	public static void main(String[] args) throws IOException{
		ArrayList<Entity> entities = new ArrayList<Entity>();
		entities.add(EntityFactory.genPlayerEntity("nain"));
		//new DataStorageTest(entities);
		World world = DataStorage.loadFile(new File("c:\\s222.xml"));
	}*/
}
