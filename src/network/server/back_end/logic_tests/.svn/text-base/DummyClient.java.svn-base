package network.server.back_end.logic_tests;

import ecs.entity.Entity;
import ecs.world.Location;
import network.NetClient;
import network.packet.NetPacket;

public class DummyClient extends NetClient {

	private Entity player;

	public DummyClient(String name, Location location) {
		super(name, location);
	}

	@Override
	public Entity getPlayer(){
		return player;
	}

	public void setPlayerEntity(Entity player){
		this.player = player;
	}

	@Override
	public void sendPacket(NetPacket packet) {
		System.out.println("Sending packet");
	}


}
