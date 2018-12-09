package game.infrpg.client.world;

import game.infrpg.server.map.Chunk;
import lib.cache.Cache;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ChunkCache extends Cache<Long, IMapChunk> implements IMapChunkStorage
{
	@Inject
	public ChunkCache(ILogger logger)
	{
		super(logger);
	}

	@Override
	public boolean containsChunk(int x, int y)
	{
		return containsKey(getKey(x, y));
	}

	@Override
	public IMapChunk getIMapChunk(int x, int y)
	{
		return get(getKey(x, y));
	}

	@Override
	public void storeMapChunk(IMapChunk chunk)
	{
		put(getKey(chunk), chunk);
	}
	
	@Override
	public int getCount()
	{
		return getElementCount();
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
