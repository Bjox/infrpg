package game.infrpg.client.world;

import game.infrpg.server.map.Chunk;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PlainChunkProcessor extends AbstractChunkProcessor
{
	@Inject
	public PlainChunkProcessor()
	{
	}
	
	@Override
	public IMapChunk process(Chunk chunk)
	{
		return new MapChunk(chunk);
	}
	
}
