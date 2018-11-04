package game.infrpg.client.world;

import game.infrpg.server.map.Chunk;
import lib.cache.Cache;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ChunkCache extends Cache<Long, IMapChunk>
{
	@Inject
	public ChunkCache(ILogger logger)
	{
		super(logger);
	}

	public boolean containsChunk(int x, int y)
	{
		return containsKey(getKey(x, y));
	}

	public IMapChunk getChunk(int x, int y)
	{
		return get(getKey(x, y));
	}

	public void putChunk(IMapChunk chunk)
	{
		put(getKey(chunk), chunk);
	}

	private long getKey(IMapChunk chunk)
	{
		return chunk.getId();
	}
	
	private long getKey(int x, int y)
	{
		return Chunk.calculateChunkId(x, y);
	}
}
