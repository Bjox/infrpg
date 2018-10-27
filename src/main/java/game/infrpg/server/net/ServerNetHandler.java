package game.infrpg.server.net;

import com.esotericsoftware.kryonet.Connection;
import game.infrpg.common.net.AbstractNetHandler;
import game.infrpg.common.net.packets.ChunkRequest;
import game.infrpg.common.net.NetPacket;
import game.infrpg.common.net.packets.ChunkResponse;
import game.infrpg.common.util.Constants;
import game.infrpg.server.map.Chunk;
import game.infrpg.server.service.map.IMapService;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ServerNetHandler extends AbstractNetHandler {
	
	private final ILogger logger;
	private final IMapService mapService;

	@Inject
	public ServerNetHandler(ILogger logger, IMapService mapService) {
		this.mapService = mapService;
		this.logger = logger;
	}
	
	@Override
	public void handle(NetPacket packet, Connection connection) {
		if (Constants.NET_TRACE)
			logger.debug("Handling netpacket: " + packet.toString());
		
		switch (packet.type) {
			case CHUNK_REQUEST: handle((ChunkRequest)packet, connection); break;
			default: logger.error("Unhandled packet type " + packet.type);
		}
	}
	
	public void handle(ChunkRequest request, Connection connection)  {
		Chunk chunk = mapService.getChunk(request.x, request.y);
		tcpResponse(connection, new ChunkResponse(chunk));
	}
	
}
