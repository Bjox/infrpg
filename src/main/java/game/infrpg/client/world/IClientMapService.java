package game.infrpg.client.world;

import game.infrpg.server.map.Chunk;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IClientMapService
{
	void processChunk(Chunk chunk);
}
