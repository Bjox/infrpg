package game.infrpg.client.world;

import game.infrpg.server.map.Chunk;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IChunkProcessor
{
	IMapChunk process(Chunk chunk);
}
