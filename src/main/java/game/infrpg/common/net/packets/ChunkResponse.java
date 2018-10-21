package game.infrpg.common.net.packets;

import game.infrpg.common.net.NetPacket;
import game.infrpg.server.map.Chunk;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ChunkResponse extends NetPacket {
	
	public final Chunk chunk;

	public ChunkResponse(Chunk chunk) {
		this.chunk = chunk;
	}
	
}
