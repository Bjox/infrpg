package game.infrpg.client.net;

import com.esotericsoftware.kryonet.Connection;
import game.infrpg.common.net.AbstractNetHandler;
import game.infrpg.common.net.NetPacket;
import game.infrpg.common.net.packets.ChunkResponse;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientNetHandler extends AbstractNetHandler {
	
	@Inject
	public ClientNetHandler() {
	}
	
	@Override
	public void handle(NetPacket packet, Connection connection) {
		switch (packet.type) {
			case CHUNK_RESPONSE: handle((ChunkResponse)packet, connection); break;
			default: throw new AssertionError(packet.type.name());
		}
	}
	
	public void handle(ChunkResponse packet, Connection connection) {
		
	}
	
}
