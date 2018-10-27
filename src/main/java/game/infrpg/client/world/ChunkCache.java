package game.infrpg.client.world;

import game.infrpg.server.map.Chunk;
import lib.cache.Cache;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ChunkCache extends Cache<Long, MapChunk>
{
	@Inject
	public ChunkCache(ILogger logger)
	{
		super(logger, 100, 1000);
	}

	public boolean containsChunk(int x, int y)
	{
		return containsKey(getKey(x, y));
	}

	public MapChunk getChunk(int x, int y)
	{
		return get(getKey(x, y));
	}

	public void putChunk(MapChunk chunk)
	{
		put(getKey(chunk), chunk);
	}

	private long getKey(Chunk chunk)
	{
		return getKey(chunk.position.getX(), chunk.position.getY());
	}

	private long getKey(int x, int y)
	{
		return Integer.toUnsignedLong(x) << 32 | Integer.toUnsignedLong(y);
	}
}
