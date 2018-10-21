package game.infrpg.server.net;

import com.esotericsoftware.kryonet.Connection;
import game.infrpg.common.net.AbstractNetHandler;
import game.infrpg.common.net.packets.ChunkRequest;
import game.infrpg.common.net.NetPacket;
import game.infrpg.common.net.packets.ChunkResponse;
import game.infrpg.server.map.Chunk;
import game.infrpg.server.service.map.IMapService;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ServerNetHandler extends AbstractNetHandler {
	
	private final IMapService mapService;

	@Inject
	public ServerNetHandler(IMapService mapService) {
		this.mapService = mapService;
	}
	
	@Override
	public void handle(NetPacket packet, Connection connection) {
		switch (packet.type) {
			case CHUNK_REQUEST: handle((ChunkRequest)packet, connection); break;
			default: throw new AssertionError(packet.type.name());
		}
	}
	
	public void handle(ChunkRequest request, Connection connection)  {
		Chunk chunk = mapService.getChunk(request.x, request.y);
		tcpResponse(connection, new ChunkResponse(chunk));
	}
	
}
