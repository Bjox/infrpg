package game.infrpg.client.net;

import com.esotericsoftware.kryonet.Connection;
import game.infrpg.common.net.AbstractNetHandler;
import game.infrpg.common.net.NetPacket;
import game.infrpg.common.net.packets.ChunkResponse;
import game.infrpg.common.util.Constants;
import game.infrpg.server.map.Chunk;
import java.util.concurrent.ConcurrentLinkedDeque;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientNetHandler extends AbstractNetHandler
{
	private final ILogger logger;
	public final ConcurrentLinkedDeque<Chunk> chunksToProcess;

	@Inject
	public ClientNetHandler(
		ILogger logger)
	{
		this.logger = logger;
		this.chunksToProcess = new ConcurrentLinkedDeque<>();
	}

	@Override
	public void handle(NetPacket packet, Connection connection)
	{
		if (Constants.NET_TRACE)
			logger.debug("Handling netpacket: " + packet.toString());

		switch (packet.type)
		{
			case CHUNK_RESPONSE: handle((ChunkResponse)packet, connection);
				break;
			default: logger.error("Unhandled packet type " + packet.type);
		}
	}

	public void handle(ChunkResponse packet, Connection connection)
	{
		chunksToProcess.offer(packet.chunk);
	}

}
